package mod.emt.enderzoo.misc;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;

import mod.emt.enderzoo.entity.TeleportHelper;

public class ConcussionExplosion extends Explosion {
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final float size;
    private static final int MAX_DURATION = 300;

    public ConcussionExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain) {
        super(worldIn, entityIn, x, y, z, size, causesFire, damagesTerrain);
        this.world = worldIn;
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
    }

    public static void create(World world, Entity entity, double x, double y, double z, float strength, boolean causesFire, boolean damagesTerrain) {
        ConcussionExplosion explosion = new ConcussionExplosion(world, entity, x, y, z, strength, causesFire, damagesTerrain);
        if (!ForgeEventFactory.onExplosionStart(world, explosion)) {
            explosion.doExplosionA();
            explosion.doExplosionB(true);
        }
    }

    @Override
    public void doExplosionA() {
        if (!this.world.isRemote) {
            double radius = this.size + 2.0D;
            double maxDistanceSq = radius * radius;
            AxisAlignedBB bb = new AxisAlignedBB(this.x - radius, this.y - radius, this.z - radius, this.x + radius, this.y + radius, this.z + radius);
            List<EntityLivingBase> entities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
            for (EntityLivingBase entity : entities) {
                double distSq = entity.getDistanceSq(this.x, this.y, this.z);
                if (distSq < maxDistanceSq) {
                    double scale = 1.0D - (distSq / maxDistanceSq);
                    scale = Math.exp(scale) / Math.E;
                    int duration = (int) Math.ceil(MAX_DURATION * scale);
                    entity.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, duration, 0));
                    boolean done = false;
                    for (int i = 0; i < 20 && !done; i++) {
                        done = TeleportHelper.teleportRandomly(entity, 64);
                    }
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
            for (int i = 0; i < 50; ++i) {
                double d = random.nextDouble() * 2.0D;
                double mag = 2.0D;
                double motionX = (0.5D - random.nextDouble()) * mag * d;
                double motionY = (0.5D - random.nextDouble()) * mag;
                double motionZ = (0.5D - random.nextDouble()) * mag * d;
                worldServer.spawnParticle(EnumParticleTypes.PORTAL, this.x + motionX * 0.1D, this.y + motionY * 0.1D, this.z + motionZ * 0.1D, 1, motionX, motionY, motionZ, 0.0D);
            }

            int nauseaColor = MobEffects.NAUSEA.getLiquidColor();
            float nR = (nauseaColor >> 16 & 255) / 255.0F;
            float nG = (nauseaColor >> 8 & 255) / 255.0F;
            float nB = (nauseaColor >> 0 & 255) / 255.0F;

            int enderColor = 0x0B4D42;
            float eR = (enderColor >> 16 & 255) / 255.0F;
            float eG = (enderColor >> 8 & 255) / 255.0F;
            float eB = (enderColor >> 0 & 255) / 255.0F;

            for (int i = 0; i < 100; ++i) {
                double motionX = (random.nextDouble() - 0.5D) * 4.0D;
                double motionY = (random.nextDouble() - 0.5D) * 4.0D;
                double motionZ = (random.nextDouble() - 0.5D) * 4.0D;
                float colorRand = 0.75F + random.nextFloat() * 0.25F;
                float finalR;
                float finalG;
                float finalB;

                if (i % 2 == 0) {
                    finalR = Math.max(0.01F, nR * colorRand);
                    finalG = Math.max(0.01F, nG * colorRand);
                    finalB = Math.max(0.01F, nB * colorRand);
                } else {
                    finalR = Math.max(0.01F, eR * colorRand);
                    finalG = Math.max(0.01F, eG * colorRand);
                    finalB = Math.max(0.01F, eB * colorRand);
                }

                worldServer.spawnParticle(EnumParticleTypes.SPELL_MOB, this.x + motionX, this.y + motionY, this.z + motionZ,
                        0, finalR, finalG, finalB, 1.0D
                );
            }
        }
    }
}