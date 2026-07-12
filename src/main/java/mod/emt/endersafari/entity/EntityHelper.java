package mod.emt.endersafari.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class EntityHelper {
    public static AxisAlignedBB getBoundsAround(Entity entity, double range) {
        return getBoundsAround(entity.posX, entity.posY, entity.posZ, range);
    }

    public static AxisAlignedBB getBoundsAround(Vec3d pos, double range) {
        return getBoundsAround(pos.x, pos.y, pos.z, range);
    }

    public static AxisAlignedBB getBoundsAround(BlockPos pos, int range) {
        return getBoundsAround(pos.getX(), pos.getY(), pos.getZ(), range);
    }

    public static AxisAlignedBB getBoundsAround(double x, double y, double z, double range) {
        return new AxisAlignedBB(x - range, y - range, z - range, x + range, y + range, z + range);
    }
}
