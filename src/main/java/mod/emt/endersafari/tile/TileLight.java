package mod.emt.endersafari.tile;

import mod.emt.endersafari.block.ESBlockLight;
import mod.emt.endersafari.utils.ColorUtil;
import mod.emt.endersafari.utils.ParticleUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class TileLight extends TileEntity implements ITickable {
    int amount;

    public TileLight() {
        amount = 0;
    }

    public boolean shouldRefresh(@NotNull World world, @NotNull BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public void update() {
        if (world.isRemote) {
            ESBlockLight block = (ESBlockLight) world.getBlockState(pos).getBlock();
            int[][] colors = block.getLightColor();
            int[] color;
            float x = pos.getX() + 0.5F + (float) world.rand.nextGaussian() * 0.025F;
            float y = pos.getY() + 0.5F + (float) world.rand.nextGaussian() * 0.025F;
            float z = pos.getZ() + 0.5F + (float) world.rand.nextGaussian() * 0.025F;
            float vx = (world.rand.nextFloat() - 0.5F) * 0.006F;
            float vy = world.rand.nextFloat() * 0.03F;
            float vz = (world.rand.nextFloat() - 0.5F) * 0.006F;
            int lifetime = 20 + world.rand.nextInt(10);
            if (colors == null) {
                float hue = (world.getTotalWorldTime() % 200L) / 200.0F;
                color = ColorUtil.RAINBOW(hue)[0];
                ParticleUtil.spawnParticleGlow(world, x, y, z, vx, vy, vz, color[0], color[1], color[2], 0.8F, 2.0F, lifetime);
            } else if (block.colorTransition) {
                ParticleUtil.spawnParticleGlow(world, x, y, z, vx, vy, vz, 0.8F, 2.0F, lifetime, colors);
            } else {
                color = colors[world.rand.nextInt(colors.length)];
                ParticleUtil.spawnParticleGlow(world, x, y, z, vx, vy, vz, color[0], color[1], color[2], 0.8F, 2.0F, lifetime);
            }
        }
    }
}
