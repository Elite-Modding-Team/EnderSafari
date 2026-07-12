package mod.emt.enderzoo.config;

import mod.emt.enderzoo.EnderSafari;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = EnderSafari.MOD_ID, name = "EnderSafari")
public class EZConfig {
    @Config.LangKey("cfg.enderzoo.general")
    @Config.Name("General")
    public static final GeneralSettings GENERAL = new GeneralSettings();

    @Config.LangKey("config.enderzoo.entities")
    @Config.Comment("Settings for entities")
    public static final Entities ENTITIES = new Entities();

    public static class GeneralSettings {
    }

    public static class Entities {
        @Config.LangKey("config.enderzoo.concussion_creeper")
        @Config.Comment("Concussion Creeper settings")
        public final ConcussionCreeper CONCUSSION_CREEPER = new ConcussionCreeper();

        @Config.LangKey("config.enderzoo.dire_cube")
        @Config.Comment("Khndrel Keght settings")
        public final DireCube DIRE_CUBE = new DireCube();

        @Config.LangKey("config.enderzoo.dire_wolf")
        @Config.Comment("Dire Wolf settings")
        public final DireWolf DIRE_WOLF = new DireWolf();

        @Config.LangKey("config.enderzoo.enderized_zombie")
        @Config.Comment("Enderized Zombie settings")
        public final EnderizedZombie ENDERIZED_ZOMBIE = new EnderizedZombie();

        @Config.LangKey("config.enderzoo.enderminy")
        @Config.Comment("Enderminy settings")
        public final Enderminy ENDERMINY = new Enderminy();

        @Config.LangKey("config.enderzoo.epic_squid")
        @Config.Comment("Epic Squid settings")
        public final EpicSquid EPIC_SQUID = new EpicSquid();

        @Config.LangKey("config.enderzoo.fallen_knight")
        @Config.Comment("Fallen Knight settings")
        public final FallenKnight FALLEN_KNIGHT = new FallenKnight();

        @Config.LangKey("config.enderzoo.fallen_steed")
        @Config.Comment("Fallen Steed settings")
        public final FallenSteed FALLEN_STEED = new FallenSteed();

        @Config.LangKey("config.enderzoo.owl")
        @Config.Comment("Owl settings")
        public final Owl OWL = new Owl();

        @Config.LangKey("config.enderzoo.void_cube")
        @Config.Comment("Void Cube settings")
        public final VoidCube VOID_CUBE = new VoidCube();

        @Config.LangKey("config.enderzoo.witch_cat")
        @Config.Comment("Witch's Cat settings")
        public final WitchCat WITCH_CAT = new WitchCat();

        @Config.LangKey("config.enderzoo.wither_witch")
        @Config.Comment("Wither Witch settings")
        public final WitherWitch WITHER_WITCH = new WitherWitch();

        public static class ConcussionCreeper {
            @Config.Name("Alternate Texture")
            @Config.Comment("Enables an alternate Concussion Creeper texture used in the original Ender Zoo mod")
            public boolean enableAlternateTexture = false;

            @Config.Name("Armor")
            @Config.Comment("The amount of armor the Concussion Creeper has")
            public double armor = 0;

            @Config.Name("Enable Entity")
            @Config.Comment("Enables the Concussion Creeper")
            public boolean enableEntity = true;

            @Config.Name("Max Health")
            @Config.Comment("The maximum health of the Concussion Creeper")
            public double maxHealth = 20.0;

            @Config.Name("Movement Speed")
            @Config.Comment("The movement speed of the Concussion Creeper")
            public double movementSpeed = 0.25;

            @Config.Name("Spawn Max")
            @Config.Comment("The maximum spawn group size of the Concussion Creeper")
            @Config.RangeInt(min = 0)
            public int spawnMax = 4;

            @Config.Name("Spawn Min")
            @Config.Comment("The minimum spawn group size of the Concussion Creeper")
            @Config.RangeInt(min = 0)
            public int spawnMin = 1;

            @Config.Name("Spawn Weight")
            @Config.Comment("The spawn weight of the Concussion Creeper")
            @Config.RangeInt(min = 0)
            public int spawnWeight = 20;
        }

        public static class DireCube {
            @Config.Name("Armor: Base")
            @Config.Comment("The base amount of armor the Khndrel Keght has (armor will scale by size)")
            public double armorBase = 0;

            @Config.Name("Attack Damage")
            @Config.Comment("The base attack damage of the Khndrel Keght (attack damage will scale by size)")
            public double attackDamage = 3.0;

            @Config.Name("Enable Entity")
            @Config.Comment("Enables the Khndrel Keght")
            public boolean enableEntity = true;

            @Config.Name("Max Health: Base")
            @Config.Comment("The base maximum health of the Khndrel Keght (health will scale by size)")
            public double maxHealthBase = 10.0;

            @Config.Name("Spawn Max")
            @Config.Comment("The maximum spawn group size of the Khndrel Keght")
            @Config.RangeInt(min = 0)
            public int spawnMax = 1;

            @Config.Name("Spawn Min")
            @Config.Comment("The minimum spawn group size of the Khndrel Keght")
            @Config.RangeInt(min = 0)
            public int spawnMin = 1;

            @Config.Name("Spawn Weight")
            @Config.Comment("The spawn weight of the Khndrel Keght")
            @Config.RangeInt(min = 0)
            public int spawnWeight = 20;
        }

        public static class DireWolf {
            @Config.Name("Armor")
            @Config.Comment("The amount of armor the Dire Wolf has")
            public double armor = 0;

            @Config.Name("Attack Damage")
            @Config.Comment("The attack damage of the Dire Wolf")
            public double attackDamage = 4.0;

            @Config.Name("Enable Entity")
            @Config.Comment("Enables the Dire Wolf")
            public boolean enableEntity = true;

            @Config.Name("Max Health")
            @Config.Comment("The maximum health of the Dire Wolf")
            public double maxHealth = 20.0;

            @Config.Name("Movement Speed")
            @Config.Comment("The movement speed of the Dire Wolf")
            public double movementSpeed = 0.35;

            @Config.Name("Spawn Max")
            @Config.Comment("The maximum spawn group size of the Dire Wolf")
            @Config.RangeInt(min = 0)
            public int spawnMax = 6;

            @Config.Name("Spawn Min")
            @Config.Comment("The minimum spawn group size of the Dire Wolf")
            @Config.RangeInt(min = 0)
            public int spawnMin = 2;

            @Config.Name("Spawn Weight")
            @Config.Comment("The spawn weight of the Dire Wolf")
            @Config.RangeInt(min = 0)
            public int spawnWeight = 30;
        }

        public static class EnderizedZombie {
            @Config.Name("Armor")
            @Config.Comment("The amount of armor the Enderized Zombie has")
            public double armor = 4.0;

            @Config.Name("Attack Damage")
            @Config.Comment("The attack damage of the Enderized Zombie")
            public double attackDamage = 6.0;

            @Config.Name("Enable Entity")
            @Config.Comment("Enables the Enderized Zombie")
            public boolean enableEntity = true;

            @Config.Name("Max Health")
            @Config.Comment("The maximum health of the Enderized Zombie")
            public double maxHealth = 30.0;

            @Config.Name("Movement Speed")
            @Config.Comment("The movement speed of the Enderized Zombie")
            public double movementSpeed = 0.25;

            @Config.Name("Spawn Max")
            @Config.Comment("The maximum spawn group size of the Enderized Zombie")
            @Config.RangeInt(min = 0)
            public int spawnMax = 1;

            @Config.Name("Spawn Min")
            @Config.Comment("The minimum spawn group size of the Enderized Zombie")
            @Config.RangeInt(min = 0)
            public int spawnMin = 1;

            @Config.Name("Spawn Weight")
            @Config.Comment("The spawn weight of the Enderized Zombie")
            @Config.RangeInt(min = 0)
            public int spawnWeight = 10;
        }

        public static class Enderminy {
            @Config.Name("Alternate Texture")
            @Config.Comment("Enables an alternate Enderminy texture used in the original Ender Zoo mod")
            public boolean enableAlternateTexture = false;

            @Config.Name("Armor")
            @Config.Comment("The amount of armor the Enderminy has")
            public double armor = 0;

            @Config.Name("Attack Damage")
            @Config.Comment("The attack damage of the Enderminy")
            public double attackDamage = 4.0;

            @Config.Name("Enable Entity")
            @Config.Comment("Enables the Enderminy")
            public boolean enableEntity = true;

            @Config.Name("Max Health")
            @Config.Comment("The maximum health of the Enderminy")
            public double maxHealth = 15.0;

            @Config.Name("Movement Speed")
            @Config.Comment("The movement speed of the Enderminy")
            public double movementSpeed = 0.25;

            @Config.Name("Spawn Max")
            @Config.Comment("The maximum spawn group size of the Enderminy")
            @Config.RangeInt(min = 0)
            public int spawnMax = 4;

            @Config.Name("Spawn Min")
            @Config.Comment("The minimum spawn group size of the Enderminy")
            @Config.RangeInt(min = 0)
            public int spawnMin = 4;

            @Config.Name("Spawn Weight")
            @Config.Comment("The spawn weight of the Enderminy")
            @Config.RangeInt(min = 0)
            public int spawnWeight = 15;
        }

        public static class EpicSquid {
            @Config.Name("Armor")
            @Config.Comment("The amount of armor the Epic Squid has")
            public double armor = 0;

            @Config.Name("Attack Damage")
            @Config.Comment("The attack damage of the Epic Squid")
            public double attackDamage = 10.0;

            @Config.Name("Enable Entity")
            @Config.Comment("Enables the Epic Squid")
            public boolean enableEntity = true;

            @Config.Name("Max Health")
            @Config.Comment("The maximum health of the Epic Squid")
            public double maxHealth = 50.0;

            @Config.Name("Spawn Max")
            @Config.Comment("The maximum spawn group size of the Epic Squid")
            @Config.RangeInt(min = 0)
            public int spawnMax = 1;

            @Config.Name("Spawn Min")
            @Config.Comment("The minimum spawn group size of the Epic Squid")
            @Config.RangeInt(min = 0)
            public int spawnMin = 4;

            @Config.Name("Spawn Weight")
            @Config.Comment("The spawn weight of the Epic Squid")
            @Config.RangeInt(min = 0)
            public int spawnWeight = 5;
        }

        public static class FallenKnight {
            @Config.Name("Armor")
            @Config.Comment("The amount of armor the Fallen Knight has")
            public double armor = 0;

            @Config.Name("Attack Damage")
            @Config.Comment("The attack damage of the Fallen Knight")
            public double attackDamage = 4.0;

            @Config.Name("Enable Entity")
            @Config.Comment("Enables the Fallen Knight")
            public boolean enableEntity = true;

            @Config.Name("Max Health")
            @Config.Comment("The maximum health of the Fallen Knight")
            public double maxHealth = 20.0;

            @Config.Name("Movement Speed")
            @Config.Comment("The movement speed of the Fallen Knight")
            public double movementSpeed = 0.25;

            @Config.Name("Spawn Max")
            @Config.Comment("The maximum spawn group size of the Fallen Knight")
            @Config.RangeInt(min = 0)
            public int spawnMax = 4;

            @Config.Name("Spawn Min")
            @Config.Comment("The minimum spawn group size of the Fallen Knight")
            @Config.RangeInt(min = 0)
            public int spawnMin = 1;

            @Config.Name("Spawn Weight")
            @Config.Comment("The spawn weight of the Fallen Knight")
            @Config.RangeInt(min = 0)
            public int spawnWeight = 20;
        }

        public static class FallenSteed {
            @Config.Name("Armor")
            @Config.Comment("The amount of armor the Fallen Steed has")
            public double armor = 0;

            @Config.Name("Attack Damage")
            @Config.Comment("The attack damage of the Fallen Steed")
            public double attackDamage = 8.0;

            @Config.Name("Enable Entity")
            @Config.Comment("Enables the Fallen Steed")
            public boolean enableEntity = true;

            @Config.Name("Max Health")
            @Config.Comment("The maximum health of the Fallen Steed")
            public double maxHealth = 30.0;

            @Config.Name("Spawn Max")
            @Config.Comment("The maximum spawn group size of the Fallen Steed")
            @Config.RangeInt(min = 0)
            public int spawnMax = 1;

            @Config.Name("Spawn Min")
            @Config.Comment("The minimum spawn group size of the Fallen Steed")
            @Config.RangeInt(min = 0)
            public int spawnMin = 1;

            @Config.Name("Spawn Weight")
            @Config.Comment("The spawn weight of the Fallen Steed")
            @Config.RangeInt(min = 0)
            public int spawnWeight = 0;
        }

        public static class Owl {
            @Config.Name("Armor")
            @Config.Comment("The amount of armor the Owl has")
            public double armor = 0;

            @Config.Name("Attack Damage")
            @Config.Comment("The attack damage of the Owl")
            public double attackDamage = 3.0;

            @Config.Name("Enable Entity")
            @Config.Comment("Enables the Owl")
            public boolean enableEntity = true;

            @Config.Name("Max Health")
            @Config.Comment("The maximum health of the Owl")
            public double maxHealth = 8.0;

            @Config.Name("Movement Speed")
            @Config.Comment("The movement speed of the Owl")
            public double movementSpeed = 0.25;

            @Config.Name("Spawn Max")
            @Config.Comment("The maximum spawn group size of the Owl")
            @Config.RangeInt(min = 0)
            public int spawnMax = 2;

            @Config.Name("Spawn Min")
            @Config.Comment("The minimum spawn group size of the Owl")
            @Config.RangeInt(min = 0)
            public int spawnMin = 1;

            @Config.Name("Spawn Weight")
            @Config.Comment("The spawn weight of the Owl")
            @Config.RangeInt(min = 0)
            public int spawnWeight = 5;
        }

        public static class VoidCube {
            @Config.Name("Attack Damage")
            @Config.Comment("The attack damage of the Void Cube")
            public double attackDamage = 11.0;

            @Config.Name("Enable Entity")
            @Config.Comment("Enables the Void Cube")
            public boolean enableEntity = true;

            @Config.Name("Max Health")
            @Config.Comment("The maximum health of the Void Cube")
            public double maxHealth = 30.0;

            @Config.Name("Spawn Max")
            @Config.Comment("The maximum spawn group size of the Void Cube")
            @Config.RangeInt(min = 0)
            public int spawnMax = 1;

            @Config.Name("Spawn Min")
            @Config.Comment("The minimum spawn group size of the Void Cube")
            @Config.RangeInt(min = 0)
            public int spawnMin = 1;

            @Config.Name("Spawn Weight")
            @Config.Comment("The spawn weight of the Void Cube")
            @Config.RangeInt(min = 0)
            public int spawnWeight = 10;
        }

        public static class WitchCat {
            @Config.Name("Armor")
            @Config.Comment("The amount of armor the Witch's Cat has")
            public double armor = 0;

            @Config.Name("Attack Damage")
            @Config.Comment("The attack damage of the Witch's Cat")
            public double attackDamage = 3.0;

            @Config.Name("Attack Damage: Extra")
            @Config.Comment("The extra amount of attack damage when the Witch's Cat grows in size")
            public double attackDamageExtra = 8.0;

            @Config.Name("Enable Entity")
            @Config.Comment("Enables the Witch's Cat")
            public boolean enableEntity = true;

            @Config.Name("Max Health")
            @Config.Comment("The maximum health of the Witch's Cat")
            public double maxHealth = 20.0;

            @Config.Name("Max Health: Extra")
            @Config.Comment("The extra amount of maximum health when the Witch's Cat grows in size")
            public double maxHealthExtra = 20.0;

            @Config.Name("Movement Speed")
            @Config.Comment("The movement speed of the Witch's Cat")
            public double movementSpeed = 0.4;

            @Config.Name("Spawn Max")
            @Config.Comment("The maximum spawn group size of the Witch's Cat")
            @Config.RangeInt(min = 0)
            public int spawnMax = 1;

            @Config.Name("Spawn Min")
            @Config.Comment("The minimum spawn group size of the Witch's Cat")
            @Config.RangeInt(min = 0)
            public int spawnMin = 1;

            @Config.Name("Spawn Weight")
            @Config.Comment("The spawn weight of the Witch's Cat")
            @Config.RangeInt(min = 0)
            public int spawnWeight = 0;
        }

        public static class WitherWitch {
            @Config.Name("Armor")
            @Config.Comment("The amount of armor the Wither Witch has")
            public double armor = 0;

            @Config.Name("Enable Entity")
            @Config.Comment("Enables the Wither Witch")
            public boolean enableEntity = true;

            @Config.Name("Max Health")
            @Config.Comment("The maximum health of the Wither Witch")
            public double maxHealth = 36.0;

            @Config.Name("Movement Speed")
            @Config.Comment("The movement speed of the Wither Witch")
            public double movementSpeed = 0.25;

            @Config.Name("Spawn Max")
            @Config.Comment("The maximum spawn group size of the Wither Witch")
            @Config.RangeInt(min = 0)
            public int spawnMax = 1;

            @Config.Name("Spawn Min")
            @Config.Comment("The minimum spawn group size of the Wither Witch")
            @Config.RangeInt(min = 0)
            public int spawnMin = 1;

            @Config.Name("Spawn Weight")
            @Config.Comment("The spawn weight of the Wither Witch")
            @Config.RangeInt(min = 0)
            public int spawnWeight = 5;
        }
    }

    @Mod.EventBusSubscriber(modid = EnderSafari.MOD_ID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(EnderSafari.MOD_ID)) {
                ConfigManager.sync(EnderSafari.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
