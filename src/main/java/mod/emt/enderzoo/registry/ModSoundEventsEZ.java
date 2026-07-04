package mod.emt.enderzoo.registry;

import mod.emt.enderzoo.EnderSafari;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public enum ModSoundEventsEZ {
    ENTITY_OWL_DEATH("entity.owl.death"),
    ENTITY_OWL_HOOT("entity.owl.hoot"),
    ENTITY_OWL_HURT("entity.owl.hurt"),
    ENTITY_SNOW_WOLF_DEATH("entity.snow_wolf.death"),
    ENTITY_SNOW_WOLF_GROWL("entity.snow_wolf.growl"),
    ENTITY_SNOW_WOLF_HOWL("entity.snow_wolf.howl"),
    ENTITY_SNOW_WOLF_HURT("entity.snow_wolf.hurt"),
    ENTITY_WITHER_CAT_GROW("entity.wither_cat.grow");

    private final SoundEvent soundEvent;

    ModSoundEventsEZ(String path) {
        ResourceLocation resourceLocation = new ResourceLocation(EnderSafari.MOD_ID, path);
        this.soundEvent = new SoundEvent(resourceLocation);
        this.soundEvent.setRegistryName(resourceLocation);
    }

    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }
}
