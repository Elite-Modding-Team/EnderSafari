package mod.emt.endersafari.registry;

import mod.emt.endersafari.EnderSafari;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public enum ModSoundEventsES {
    ENTITY_DIRE_WOLF_DEATH("entity.dire_wolf.death"),
    ENTITY_DIRE_WOLF_GROWL("entity.dire_wolf.growl"),
    ENTITY_DIRE_WOLF_HOWL("entity.dire_wolf.howl"),
    ENTITY_DIRE_WOLF_HURT("entity.dire_wolf.hurt"),
    ENTITY_FIREBALL_HIT("entity.fireball.hit"),
    ENTITY_FIREBALL_LAUNCH("entity.fireball.launch"),
    ENTITY_GOLEM_DEATH("entity.golem.death"),
    ENTITY_GOLEM_HURT("entity.golem.hurt"),
    ENTITY_GOLEM_PUNCH("entity.golem.punch"),
    ENTITY_GOLEM_STEP("entity.golem.step"),
    ENTITY_LARGE_FIREBALL_HIT("entity.large_fireball.hit"),
    ENTITY_LARGE_FIREBALL_LAUNCH("entity.large_fireball.launch"),
    ENTITY_OWL_DEATH("entity.owl.death"),
    ENTITY_OWL_HOOT("entity.owl.hoot"),
    ENTITY_OWL_HURT("entity.owl.hurt"),
    ENTITY_SHEEPER_HURT("entity.sheeper.hurt"),
    ENTITY_WITCH_CAT_GROW("entity.witch_cat.grow");

    private final SoundEvent soundEvent;

    ModSoundEventsES(String path) {
        ResourceLocation resourceLocation = new ResourceLocation(EnderSafari.MOD_ID, path);
        this.soundEvent = new SoundEvent(resourceLocation);
        this.soundEvent.setRegistryName(resourceLocation);
    }

    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }
}
