package mod.emt.endersafari.block;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.entity.EntityChargePrimed;
import mod.emt.endersafari.entity.EntityEnderChargePrimed;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class ESBlockEnderCharge extends BlockTNT {
    public ESBlockEnderCharge(String name) {
        super();
        this.setRegistryName(EnderSafari.MOD_ID, name);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(EnderSafari.tabEZ);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    public void onExplosionDestroy(World world, @NotNull BlockPos pos, @NotNull Explosion explosion) {
        if (!world.isRemote) {
            EntityChargePrimed entityPrimed = new EntityEnderChargePrimed(world, ((float) pos.getX() + 0.5F), pos.getY(), ((float) pos.getZ() + 0.5F), explosion.getExplosivePlacedBy());
            entityPrimed.setFuse((short) (world.rand.nextInt(entityPrimed.getFuse() / 4) + entityPrimed.getFuse() / 8));
            world.spawnEntity(entityPrimed);
        }
    }

    @Override
    public void explode(World world, @NotNull BlockPos pos, @NotNull IBlockState state, @NotNull EntityLivingBase igniter) {
        if (!world.isRemote) {
            if (state.getValue(EXPLODE)) {
                EntityChargePrimed entityPrimed = new EntityEnderChargePrimed(world, ((float) pos.getX() + 0.5F), pos.getY(), ((float) pos.getZ() + 0.5F), igniter);
                world.spawnEntity(entityPrimed);
                world.playSound(null, entityPrimed.posX, entityPrimed.posY, entityPrimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> list, @NotNull ITooltipFlag flag) {
        list.add(TextFormatting.GRAY + I18n.translateToLocal("tile." + Objects.requireNonNull(this.getRegistryName()) + ".tooltip"));
        list.add(TextFormatting.GRAY + I18n.translateToLocal("tile." + EnderSafari.MOD_ID + ":charge.tooltip"));
    }
}
