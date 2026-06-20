package mod.emt.enderzoo.misc;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;

import mod.emt.enderzoo.entity.TeleportHelper;

public class EnderExplosion extends Explosion {
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final float size;

    public EnderExplosion(World world, Entity entity, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain) {
        super(world, entity, x, y, z, size, causesFire, damagesTerrain);
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
    }

    public static void create(World world, Entity entity, double x, double y, double z, float strength, boolean causesFire, boolean damagesTerrain) {
        EnderExplosion explosion = new EnderExplosion(world, entity, x, y, z, strength, causesFire, damagesTerrain);
        if (!ForgeEventFactory.onExplosionStart(world, explosion)) {
            explosion.doExplosionA();
            explosion.doExplosionB(true);
        }
    }

    @Override
    public void doExplosionA() {
        if (!this.world.isRemote) {
            double radius = this.size + 2.0D;
            AxisAlignedBB bb = new AxisAlignedBB(this.x - radius, this.y - radius, this.z - radius, this.x + radius, this.y + radius, this.z + radius);
            List<EntityLivingBase> ents = this.world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
            for (EntityLivingBase ent : ents) {
                boolean done = false;
                for (int i = 0; i < 20 && !done; i++) {
                    done = TeleportHelper.teleportRandomly(ent, 64);
                }
            }

            int minX = (int) Math.floor(this.x - radius);
            int maxX = (int) Math.ceil(this.x + radius);
            int minY = (int) Math.floor(this.y - radius);
            int maxY = (int) Math.ceil(this.y + radius);
            int minZ = (int) Math.floor(this.z - radius);
            int maxZ = (int) Math.ceil(this.z + radius);

            for (int cx = minX; cx <= maxX; cx++) {
                for (int cy = minY; cy <= maxY; cy++) {
                    for (int cz = minZ; cz <= maxZ; cz++) {
                        BlockPos pos = new BlockPos(cx, cy, cz);
                        IBlockState state = this.world.getBlockState(pos);
                        Block block = state.getBlock();
                        if (block instanceof BlockTNT) {
                            block.onBlockExploded(this.world, pos, this);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void doExplosionB(boolean spawnParticles) {
        this.world.playSound(null, this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 3.0F, 1.4F + ((this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F));
        this.world.playSound(null, this.x, this.y, this.z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 4.0F, 1.0F + ((this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F));

        if (spawnParticles && !this.world.isRemote && this.world instanceof WorldServer) {
            WorldServer worldServer = (WorldServer) this.world;
            Random random = this.world.rand;

            if (this.size >= 2.0F) {
                worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.x, this.y, this.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            } else {
                worldServer.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.x, this.y, this.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            }

            for (int i = 0; i < 100; ++i) {
                double d = random.nextDouble() * 2.0D;
                double mag = 2.0D;
                double motionX = (0.5D - random.nextDouble()) * mag * d;
                double motionY = (0.5D - random.nextDouble()) * mag;
                double motionZ = (0.5D - random.nextDouble()) * mag * d;
                worldServer.spawnParticle(EnumParticleTypes.PORTAL, this.x + motionX * 0.1D, this.y + motionY * 0.1D, this.z + motionZ * 0.1D,
                        1, motionX, motionY, motionZ, 0.0D
                );
            }
        }
    }
}
