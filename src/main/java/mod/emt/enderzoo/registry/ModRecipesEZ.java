package mod.emt.enderzoo.registry;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionHelper;
import utils.helpers.CompatHelper;

public class ModRecipesEZ {
    public static void registerBrewingRecipes() {
        PotionHelper.addMix(PotionTypes.AWKWARD, ModItemsEZ.ENDER_FRAGMENT, ModPotionsEZ.DISPLACEMENT_POTION);
        PotionHelper.addMix(PotionTypes.AWKWARD, Items.ENDER_PEARL, ModPotionsEZ.DISPLACEMENT_POTION_STRONG);
        PotionHelper.addMix(ModPotionsEZ.DISPLACEMENT_POTION, Items.GLOWSTONE_DUST, ModPotionsEZ.DISPLACEMENT_POTION_STRONG);

        if (!CompatHelper.isEnderIOLoaded) {
            PotionHelper.addMix(PotionTypes.AWKWARD, ModItemsEZ.OWL_EGG, ModPotionsEZ.LEVITATION_POTION);
            PotionHelper.addMix(PotionTypes.AWKWARD, ModItemsEZ.CONFUSING_DUST, ModPotionsEZ.NAUSEA_POTION);
            PotionHelper.addMix(PotionTypes.AWKWARD, ModItemsEZ.WITHERING_DUST, ModPotionsEZ.WITHER_POTION);
            PotionHelper.addMix(ModPotionsEZ.LEVITATION_POTION, Items.REDSTONE, ModPotionsEZ.LEVITATION_POTION_LONG);
            PotionHelper.addMix(ModPotionsEZ.LEVITATION_POTION, Items.GLOWSTONE_DUST, ModPotionsEZ.LEVITATION_POTION_STRONG);
            PotionHelper.addMix(ModPotionsEZ.NAUSEA_POTION, Items.REDSTONE, ModPotionsEZ.NAUSEA_POTION_LONG);
            PotionHelper.addMix(ModPotionsEZ.WITHER_POTION, Items.REDSTONE, ModPotionsEZ.WITHER_POTION_LONG);
            PotionHelper.addMix(ModPotionsEZ.WITHER_POTION, Items.GLOWSTONE_DUST, ModPotionsEZ.WITHER_POTION_STRONG);
        }
    }
}
