package mod.emt.enderzoo.effect;

import mod.emt.enderzoo.entity.TeleportHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class MobEffectDisplacement extends MobEffectBaseEZ {
    public MobEffectDisplacement(String name, boolean isBadEffect, int liquidColor) {
        super(name, isBadEffect, liquidColor);
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entity, int amplifier, double health) {
        if (!entity.world.isRemote) {
            int range = (amplifier + 1) * 16;
            int maxDistance = 32 + range;
            int finalDistance = (int) Math.ceil(maxDistance * health);
            if (health > 0.025D) {
                TeleportHelper.teleportEntity(entity.world, entity, false, true, finalDistance);
            }
        }
    }
}
