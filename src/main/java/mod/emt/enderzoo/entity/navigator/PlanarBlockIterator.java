package mod.emt.enderzoo.entity.navigator;

import javax.annotation.Nonnull;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class PlanarBlockIterator {
    public enum Orientation {
        EAST_WEST,
        NORTH_SOUTH,
        HORIZONTAL;

        public static @Nonnull Orientation perpendicular(@Nonnull EnumFacing dir) {
            switch (dir) {
                case NORTH:
                case SOUTH:
                    return EAST_WEST;
                case EAST:
                case WEST:
                    return NORTH_SOUTH;
                case DOWN:
                case UP:
                default:
                    return HORIZONTAL;
            }
        }
    }

    private final @Nonnull Orientation orientation;
    private final int minX;
    private final int maxX;
    private final int minY;
    private final int maxY;
    private final int minZ;
    private final int maxZ;
    private int curX;
    private int curY;
    private int curZ;

    public PlanarBlockIterator(@Nonnull BlockPos base, @Nonnull Orientation orientation, int radius) {
        this.orientation = orientation;
        this.minX = base.getX() - radius;
        this.maxX = base.getX() + radius;
        this.minY = base.getY() - radius;
        this.maxY = base.getY() + radius;
        this.minZ = base.getZ() - radius;
        this.maxZ = base.getZ() + radius;
        this.curX = minX;
        this.curY = minY;
        this.curZ = minZ;

        if (orientation == Orientation.EAST_WEST) {
            this.curZ = base.getZ();
        } else if (orientation == Orientation.NORTH_SOUTH) {
            this.curX = base.getX();
        } else if (orientation == Orientation.HORIZONTAL) {
            this.curY = base.getY();
        }
    }

    public boolean hasNext() {
        switch (orientation) {
            case EAST_WEST:
                return curX <= maxX && curY <= maxY;
            case NORTH_SOUTH:
                return curZ <= maxZ && curY <= maxY;
            case HORIZONTAL:
                return curX <= maxX && curZ <= maxZ;
            default:
                return false;
        }
    }

    public @Nonnull BlockPos next() {
        BlockPos result = new BlockPos(curX, curY, curZ);
        switch (orientation) {
            case EAST_WEST:
                if (curY == maxY) {
                    curY = minY;
                    curX++;
                } else {
                    curY++;
                }
                break;

            case NORTH_SOUTH:
                if (curY == maxY) {
                    curY = minY;
                    curZ++;
                } else {
                    curY++;
                }
                break;

            case HORIZONTAL:
                if (curX == maxX) {
                    curX = minX;
                    curZ++;
                } else {
                    curX++;
                }
                break;
        }
        return result;
    }
}
