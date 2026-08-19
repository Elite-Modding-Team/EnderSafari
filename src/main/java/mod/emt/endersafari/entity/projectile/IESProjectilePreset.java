package mod.emt.endersafari.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.awt.*;

public interface IESProjectilePreset {
    Vec3d getPos();

    Vec3d getVelocity();

    Color getColor();

    IESProjectileEffect getEffect();

    @Nullable
    Entity getEntity();

    @Nullable
    Entity getShooter();

    void setPos(Vec3d pos);

    void setVelocity(Vec3d velocity);

    void setColor(Color color);

    void setEffect(IESProjectileEffect effect);

    void shoot(World world);
}
