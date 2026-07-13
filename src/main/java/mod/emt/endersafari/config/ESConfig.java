package mod.emt.endersafari.config;

import mod.emt.endersafari.EnderSafari;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = EnderSafari.MOD_ID, name = "EnderSafari")
public class ESConfig {
    @Config.LangKey("config.endersafari.general")
    public static final General GENERAL = new General();

    @Config.LangKey("config.endersafari.enchantments")
    @Config.Comment("Settings for enchantments")
    public static final Enchantments ENCHANTMENTS = new Enchantments();

    @Config.LangKey("config.endersafari.entities")
    @Config.Comment("Settings for entities")
    public static final Entities ENTITIES = new Entities();

    @Config.LangKey("config.endersafari.mod_integration")
    @Config.Comment("Settings for mod integration")
    public static final ModIntegration MOD_INTEGRATION = new ModIntegration();

    public static class General {
        @Config.Name("Disable Potion of Confusion")
        @Config.Comment("Disables the Potion of Confusion, highly recommended if another mod is adding a similar potion")
        public boolean disablePotionConfusion = false;

        @Config.Name("Disable Potion of Decay")
        @Config.Comment("Disables the Potion of Decay, highly recommended if another mod is adding a similar potion")
        public boolean disablePotionDecay = false;

        @Config.Name("Disable Potion of Displacement")
        @Config.Comment("Disables the Potion of Displacement, highly recommended if another mod is adding a similar potion")
        public boolean disablePotionDisplacement = false;

        @Config.Name("Disable Potion of Lifting")
        @Config.Comment("Disables the Potion of Lifting, highly recommended if another mod is adding a similar potion")
        public boolean disablePotionLifting = false;
    }

    public static class Enchantments {
        @Config.LangKey("config.endersafari.enchantments.decay")
        @Config.Comment("Decay settings")
        public final Enchantments.Decay DECAY = new Enchantments.Decay();

        @Config.LangKey("config.endersafari.enchantments.repellent")
        @Config.Comment("Repellent settings")
        public final Enchantments.Repellent REPELLENT = new Enchantments.Repellent();

        @Config.LangKey("config.endersafari.enchantments.wither_aspect")
        @Config.Comment("Wither Aspect settings")
        public final Enchantments.WitherAspect WITHER_ASPECT = new Enchantments.WitherAspect();

        public static class Decay {
            @Config.Name("Duration")
            @Config.Comment("The duration (in seconds) of Wither that the Decay enchantment will inflict")
            @Config.RangeInt(min = 1)
            public int duration = 6;

            @Config.Name("Enable Enchantment")
            @Config.Comment("Enables the Decay enchantment")
            public boolean enableEnchantment = true;

            @Config.Name("Max Enchantability")
            @Config.Comment("Max enchantability that the Decay enchantment can have")
            @Config.RangeInt(min = 0)
            public int maxEnchantability = 50;

            @Config.Name("Min Enchantability")
            @Config.Comment("Min enchantability that the Decay enchantment can have")
            @Config.RangeInt(min = 0)
            public int minEnchantability = 20;

            @Config.Name("Max Level")
            @Config.Comment("Max level that the Decay enchantment can be")
            @Config.RangeInt(min = 1)
            public int maxLevel = 1;

            @Config.Name("Rarity")
            @Config.Comment("The rarity of the Decay enchantment")
            public Enchantment.Rarity rarity = Enchantment.Rarity.RARE;
        }

        public static class Repellent {
            @Config.Name("Enable Enchantment")
            @Config.Comment("Enables the Repellent enchantment")
            public boolean enableEnchantment = true;

            @Config.Name("Max Enchantability")
            @Config.Comment("Max enchantability that the Repellent enchantment can have")
            @Config.RangeInt(min = 0)
            public int maxEnchantability = 50;

            @Config.Name("Min Enchantability")
            @Config.Comment("Min enchantability that the Repellent enchantment can have")
            @Config.RangeInt(min = 0)
            public int minEnchantability = 10;

            @Config.Name("Max Level")
            @Config.Comment("Max level that the Repellent enchantment can be")
            @Config.RangeInt(min = 1)
            public int maxLevel = 4;

            @Config.Name("Rarity")
            @Config.Comment("The rarity of the Repellent enchantment")
            public Enchantment.Rarity rarity = Enchantment.Rarity.VERY_RARE;

            @Config.Name("Base Teleport Range")
            @Config.Comment("The base range when attackers are teleported by the Repellent enchantment")
            public int repellentTeleportRangeBase = 16;

            @Config.Name("Teleport Range Per Level")
            @Config.Comment("The extra range per level when attackers are teleported by the Repellent enchantment")
            public int repellentTeleportRangeScale = 8;

            @Config.Name("Repellent Chance Per Level")
            @Config.Comment("The default chance (multiplied by level) for the Repellent enchantment to teleport an attacker")
            public double repellentChancePerLevel = 0.15;
        }

        public static class WitherAspect {
            @Config.Name("Duration")
            @Config.Comment("The duration (in seconds) of Wither that the Wither Aspect enchantment will inflict")
            @Config.RangeInt(min = 1)
            public int duration = 5;

            @Config.Name("Enable Enchantment")
            @Config.Comment("Enables the Wither Aspect enchantment")
            public boolean enableEnchantment = true;

            @Config.Name("Max Enchantability")
            @Config.Comment("Max enchantability that the Wither Aspect enchantment can have")
            @Config.RangeInt(min = 0)
            public int maxEnchantability = 50;

            @Config.Name("Min Enchantability")
            @Config.Comment("Min enchantability that the Wither Aspect enchantment can have")
            @Config.RangeInt(min = 0)
            public int minEnchantability = 10;

            @Config.Name("Max Level")
            @Config.Comment("Max level that the Wither Aspect enchantment can be")
            @Config.RangeInt(min = 1)
            public int maxLevel = 2;

            @Config.Name("Rarity")
            @Config.Comment("The rarity of the Wither Aspect enchantment")
            public Enchantment.Rarity rarity = Enchantment.Rarity.RARE;
        }
    }

    public static class Entities {
        @Config.LangKey("config.endersafari.entities.concussion_creeper")
        @Config.Comment("Concussion Creeper settings")
        public final ConcussionCreeper CONCUSSION_CREEPER = new ConcussionCreeper();

        @Config.LangKey("config.endersafari.entities.dire_cube")
        @Config.Comment("Khndrel Keght settings")
        public final DireCube DIRE_CUBE = new DireCube();

        @Config.LangKey("config.endersafari.entities.dire_wolf")
        @Config.Comment("Dire Wolf settings")
        public final DireWolf DIRE_WOLF = new DireWolf();

        @Config.LangKey("config.endersafari.entities.enderized_zombie")
        @Config.Comment("Enderized Zombie settings")
        public final EnderizedZombie ENDERIZED_ZOMBIE = new EnderizedZombie();

        @Config.LangKey("config.endersafari.entities.enderminy")
        @Config.Comment("Enderminy settings")
        public final Enderminy ENDERMINY = new Enderminy();

        @Config.LangKey("config.endersafari.entities.epic_squid")
        @Config.Comment("Epic Squid settings")
        public final EpicSquid EPIC_SQUID = new EpicSquid();

        @Config.LangKey("config.endersafari.entities.fallen_knight")
        @Config.Comment("Fallen Knight settings")
        public final FallenKnight FALLEN_KNIGHT = new FallenKnight();

        @Config.LangKey("config.endersafari.entities.fallen_steed")
        @Config.Comment("Fallen Steed settings")
        public final FallenSteed FALLEN_STEED = new FallenSteed();

        @Config.LangKey("config.endersafari.entities.owl")
        @Config.Comment("Owl settings")
        public final Owl OWL = new Owl();

        @Config.LangKey("config.endersafari.entities.void_cube")
        @Config.Comment("Void Cube settings")
        public final VoidCube VOID_CUBE = new VoidCube();

        @Config.LangKey("config.endersafari.entities.witch_cat")
        @Config.Comment("Witch's Cat settings")
        public final WitchCat WITCH_CAT = new WitchCat();

        @Config.LangKey("config.endersafari.entities.wither_witch")
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

            @Config.Name("Explosion Range")
            @Config.Comment("The explosion range of the Concussion Creeper")
            public double explosionRange = 5.0;

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

            @Config.Name("Howl Chance")
            @Config.Comment("The chance for Dire Wolves to howl")
            public double howlChance = 0.05;

            @Config.Name("Howl Volume")
            @Config.Comment("The volume of the Dire Wolf howl sound")
            public double howlVolume = 8.0;

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

            @Config.Name("Enable Reinforcements")
            @Config.Comment("Allows Enderized Zombies to spawn additional Enderized Zombies to help when damaged in Hard difficulty (same as vanilla zombies)")
            public boolean enableReinforcements = true;

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

            @Config.Name("Teleport Chance: Self")
            @Config.Comment("The chance for the Enderized Zombie to teleport around when it gets hurt")
            public double teleportChanceSelf = 0.25;

            @Config.Name("Teleport Chance: Target")
            @Config.Comment("The chance for the Enderized Zombie to teleport a target when it damages them")
            public double teleportChanceTarget = 0.5;

            @Config.Name("Teleport Range: Self")
            @Config.Comment("The range distance when the Enderized Zombie teleports itself")
            public double teleportRangeSelf = 8.0;

            @Config.Name("Teleport Range: Target")
            @Config.Comment("The range distance when the Enderized Zombie teleports a target")
            public double teleportRangeTarget = 8.0;
        }

        public static class Enderminy {
            @Config.Name("Armor")
            @Config.Comment("The amount of armor the Enderminy has")
            public double armor = 0;

            @Config.Name("Attack Damage")
            @Config.Comment("The attack damage of the Enderminy")
            public double attackDamage = 4.0;

            @Config.Name("Alternate Texture")
            @Config.Comment("Enables an alternate Enderminy texture used in the original Ender Zoo mod")
            public boolean enableAlternateTexture = false;

            @Config.Name("Enable Entity")
            @Config.Comment("Enables the Enderminy")
            public boolean enableEntity = true;

            @Config.Name("Concussion Creeper Hostility")
            @Config.Comment("Enderminies will attack nearby Concussion Creepers")
            public boolean enableCreeperHostility = true;

            @Config.Name("Group Aggression")
            @Config.Comment("Nearby Enderminies will attack when one of them is hurt")
            public boolean enableGroupAggro = true;

            @Config.Name("Player Hostility")
            @Config.Comment("Enderminies will attack Players they directly look at (inverse of Endermen)")
            public boolean enablePlayerHostility = true;

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

            @Config.Name("Mount Chance")
            @Config.Comment("The chance for a Fallen Knight to be mounted on a Fallen Steed")
            public double mountChance = 0.2;

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

            @Config.Name("Horse Armor Chance")
            @Config.Comment("The chance for a Fallen Steed to spawn with horse armor (not to be confused with the armor attribute)")
            public double horseArmorChance = 0.2;

            @Config.Name("Horse Armor Chance: Hard Difficulty")
            @Config.Comment("The chance for a Fallen Steed to spawn with horse armor in Hard difficulty (not to be confused with the armor attribute)")
            public double horseArmorChanceHard = 0.5;

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

            @Config.Name("Hoot Interval")
            @Config.Comment("The interval for Owls to hoot at night")
            public int hootInterval = 1000;

            @Config.Name("Hoot Volume")
            @Config.Comment("The volume of the Owl hoot sound")
            public double hootVolume = 0.8;

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

            @Config.Name("Blindness Range")
            @Config.Comment("The range for the Void Cube to apply Blindness to nearby players")
            @Config.RangeInt(min = 0)
            public int blindnessRange = 8;

            @Config.Name("Enable Entity")
            @Config.Comment("Enables the Void Cube")
            public boolean enableEntity = true;

            @Config.Name("Enable Darkness Particles")
            @Config.Comment("Allows Void Cubes to emit darkness around it")
            public boolean enableDarknessParticles = true;

            @Config.Name("Max Health")
            @Config.Comment("The maximum health of the Void Cube")
            public double maxHealth = 30.0;

            @Config.Name("Spawning Requires Darkness")
            @Config.Comment("If enabled, Void Cubes will require darkness when spawning, otherwise they will always spawn even if the area is lit up")
            public boolean spawnInDarkness = false;

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

            @Config.Name("Cat Spawn: Max")
            @Config.Comment("The maximum amount of Witch's Cats that can spawn alongside a Wither Witch")
            @Config.RangeInt(min = 0)
            public int catSpawnMax = 2;

            @Config.Name("Cat Spawn: Min")
            @Config.Comment("The minimum amount of Witch's Cats that can spawn alongside a Wither Witch")
            @Config.RangeInt(min = 0)
            public int catSpawnMin = 1;

            @Config.Name("Enable Cat Spawning")
            @Config.Comment("The Wither Witch will spawn alongside Witch's Cats")
            public boolean enableCatSpawning = true;

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

    public static class ModIntegration {
        @Config.Name("Ender IO Integration")
        @Config.Comment("Enables Ender IO integration, which adds extra drops to certain mobs, and allows Ender IO potions to be brewed")
        public boolean enderIOIntegration = true;

        @Config.Name("Just Enough Resources Integration")
        @Config.Comment("Enables Just Enough Resources integration")
        public boolean JERIntegration = false;

        @Config.Name("Thaumcraft Integration")
        @Config.Comment("Enables Thaumcraft aspect integration")
        public boolean thaumcraftIntegration = true;
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
