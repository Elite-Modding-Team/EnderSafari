package mod.emt.enderzoo.registry;

import mod.emt.enderzoo.EnderSafari;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public enum ModSoundEventsEZ {
    ENTITY_DIRE_WOLF_DEATH("entity.dire_wolf.death"),
    ENTITY_DIRE_WOLF_GROWL("entity.dire_wolf.growl"),
    ENTITY_DIRE_WOLF_HOWL("entity.dire_wolf.howl"),
    ENTITY_DIRE_WOLF_HURT("entity.dire_wolf.hurt"),
    ENTITY_OWL_DEATH("entity.owl.death"),
    ENTITY_OWL_HOOT("entity.owl.hoot"),
    ENTITY_OWL_HURT("entity.owl.hurt"),
    ENTITY_WITCH_CAT_GROW("entity.witch_cat.grow");

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
