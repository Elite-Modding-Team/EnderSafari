package mod.emt.endersafari.block;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.registry.ModRegistryES;
import mod.emt.endersafari.tile.TileLight;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class ESBlockLight extends Block implements ITileEntityProvider {
    public static final AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0.33D, 0.33D, 0.33D, 0.66D, 0.66D, 0.66D);
    public int[][] color;
    public boolean colorTransition;

    public ESBlockLight(String name, int[][] color, boolean colorTransition) {
        super(Material.CIRCUITS, MapColor.AIR);
        this.setRegistryName(EnderSafari.MOD_ID, name);
        this.setTranslationKey(Objects.requireNonNull(this.getRegistryName()).toString());
        this.setCreativeTab(EnderSafari.tabEZ);
        this.setLightLevel(1.0F);
        this.setSoundType(ModRegistryES.LIGHT);
        this.color = color;
        this.colorTransition = colorTransition;
    }

    public int[][] getLightColor() {
        return color;
    }

    public boolean isFullCube(@NotNull IBlockState state) {
        return false;
    }

    public boolean isOpaqueCube(@NotNull IBlockState state) {
        return false;
    }

    public @NotNull AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    public AxisAlignedBB getCollisionBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess world, @NotNull BlockPos pos) {
        return null;
    }

    public @NotNull BlockFaceShape getBlockFaceShape(@NotNull IBlockAccess world, @NotNull IBlockState state, @NotNull BlockPos pos, @NotNull EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    public @NotNull EnumBlockRenderType getRenderType(@NotNull IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    public TileEntity createNewTileEntity(@NotNull World world, int meta) {
        return new TileLight();
    }

    public boolean hasTileEntity(@NotNull IBlockState state) {
        return true;
    }
}
