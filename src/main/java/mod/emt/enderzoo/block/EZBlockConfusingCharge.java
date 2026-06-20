package mod.emt.enderzoo.block;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.entity.EntityChargePrimed;
import mod.emt.enderzoo.entity.EntityConfusingChargePrimed;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EZBlockConfusingCharge extends BlockTNT {
    public EZBlockConfusingCharge(String name) {
        super();
        this.setRegistryName(EnderSafari.MOD_ID, name);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(EnderSafari.tabEZ);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    public void onExplosionDestroy(World world, @NotNull BlockPos pos, @NotNull Explosion explosion) {
        if (!world.isRemote) {
            EntityChargePrimed entityPrimed = new EntityConfusingChargePrimed(world, ((float) pos.getX() + 0.5F), pos.getY(), ((float) pos.getZ() + 0.5F), explosion.getExplosivePlacedBy());
            entityPrimed.setFuse((short) (world.rand.nextInt(entityPrimed.getFuse() / 4) + entityPrimed.getFuse() / 8));
            world.spawnEntity(entityPrimed);
        }
    }

    @Override
    public void explode(World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityLivingBase igniter) {
        if (!world.isRemote) {
            if (state.getValue(EXPLODE)) {
                EntityChargePrimed entityPrimed = new EntityConfusingChargePrimed(world, ((float) pos.getX() + 0.5F), pos.getY(), ((float) pos.getZ() + 0.5F), igniter);
                world.spawnEntity(entityPrimed);
                world.playSound(null, entityPrimed.posX, entityPrimed.posY, entityPrimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }
}
