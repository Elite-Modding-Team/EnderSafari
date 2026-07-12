package mod.emt.endersafari.registry;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionHelper;
import mod.emt.endersafari.utils.helpers.CompatHelper;

public class ModRecipesES {
    public static void registerBrewingRecipes() {
        PotionHelper.addMix(PotionTypes.AWKWARD, ModItemsES.ENDER_FRAGMENT, ModPotionsES.DISPLACEMENT_POTION);
        PotionHelper.addMix(PotionTypes.AWKWARD, Items.ENDER_PEARL, ModPotionsES.DISPLACEMENT_POTION_STRONG);
        PotionHelper.addMix(ModPotionsES.DISPLACEMENT_POTION, Items.GLOWSTONE_DUST, ModPotionsES.DISPLACEMENT_POTION_STRONG);

        if (!CompatHelper.isEnderIOLoaded) {
            PotionHelper.addMix(PotionTypes.AWKWARD, ModItemsES.OWL_EGG, ModPotionsES.LEVITATION_POTION);
            PotionHelper.addMix(PotionTypes.AWKWARD, ModItemsES.CONFUSING_DUST, ModPotionsES.NAUSEA_POTION);
            PotionHelper.addMix(PotionTypes.AWKWARD, ModItemsES.WITHERING_DUST, ModPotionsES.WITHER_POTION);
            PotionHelper.addMix(ModPotionsES.LEVITATION_POTION, Items.REDSTONE, ModPotionsES.LEVITATION_POTION_LONG);
            PotionHelper.addMix(ModPotionsES.LEVITATION_POTION, Items.GLOWSTONE_DUST, ModPotionsES.LEVITATION_POTION_STRONG);
            PotionHelper.addMix(ModPotionsES.NAUSEA_POTION, Items.REDSTONE, ModPotionsES.NAUSEA_POTION_LONG);
            PotionHelper.addMix(ModPotionsES.WITHER_POTION, Items.REDSTONE, ModPotionsES.WITHER_POTION_LONG);
            PotionHelper.addMix(ModPotionsES.WITHER_POTION, Items.GLOWSTONE_DUST, ModPotionsES.WITHER_POTION_STRONG);
        }
    }
}
