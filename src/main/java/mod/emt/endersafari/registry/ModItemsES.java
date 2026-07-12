package mod.emt.endersafari.registry;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.item.ESItem;
import mod.emt.endersafari.item.ESItemBow;
import mod.emt.endersafari.item.ESItemFireproof;
import mod.emt.endersafari.item.ItemOwlEgg;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@GameRegistry.ObjectHolder(EnderSafari.MOD_ID)
public class ModItemsES {
    public static final ESItem CONFUSING_DUST = null;
    public static final ESItemFireproof ENDER_FRAGMENT = null;
    public static final ESItemFireproof EPIC_INK_SAC = null;
    public static final ESItemBow GUARDIAN_BOW = null;
    public static final ItemOwlEgg OWL_EGG = null;
    public static final ESItem WITHERING_DUST = null;

    @SubscribeEvent
    public static void registerItems(@Nonnull final RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();

        registry.registerAll(
                new ESItem("withering_dust", EnumRarity.COMMON),
                new ESItem("confusing_dust", EnumRarity.COMMON),
                new ESItemFireproof("ender_fragment", EnumRarity.COMMON),
                new ESItemFireproof("epic_ink_sac", EnumRarity.EPIC),
                new ESItemBow("guardian_bow", 800, 1.1F, 1, 0.9F, 1.2F, Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT)), EnumRarity.UNCOMMON),
                new ItemOwlEgg("owl_egg")
        );
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    public static void registerItemModels(@Nonnull final ModelRegistryEvent event) {
        // Item Models
        registerItemModel(CONFUSING_DUST);
        registerItemModel(ENDER_FRAGMENT);
        registerItemModel(EPIC_INK_SAC);
        registerItemModel(GUARDIAN_BOW);
        registerItemModel(OWL_EGG);
        registerItemModel(WITHERING_DUST);
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    private static void registerItemModel(Item item) {
        ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, loc);
    }
}
