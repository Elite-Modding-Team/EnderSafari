package mod.emt.endersafari.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface IESProjectileEffect {
    default void onHit(World world, RayTraceResult raytrace, IESProjectilePreset projectile) {
        if (raytrace.entityHit != null) {
            onEntityImpact(raytrace.entityHit, projectile);
        } else {
            onBlockImpact(world, raytrace.getBlockPos(), raytrace.sideHit, projectile);
        }
    }

    default void onEntityImpact(Entity entity, IESProjectilePreset projectile) {
    }

    default void onBlockImpact(World world, BlockPos pos, EnumFacing side, IESProjectilePreset projectile) {
    }

    default void onFizzle(World world, Vec3d pos, IESProjectilePreset projectile) {
    }
}
