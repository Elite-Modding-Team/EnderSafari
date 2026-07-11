package mod.emt.enderzoo.registry;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.item.EZItem;
import mod.emt.enderzoo.item.EZItemBow;
import mod.emt.enderzoo.item.EZItemFireproof;
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
public class ModItemsEZ {
    public static final EZItem CONFUSING_DUST = null;
    public static final EZItemFireproof ENDER_FRAGMENT = null;
    public static final EZItemFireproof EPIC_INK_SAC = null;
    public static final EZItemBow GUARDIAN_BOW = null;
    public static final EZItem OWL_EGG = null;
    public static final EZItem WITHERING_DUST = null;

    @SubscribeEvent
    public static void registerItems(@Nonnull final RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();

        registry.registerAll(
                new EZItem("withering_dust", EnumRarity.COMMON),
                new EZItem("confusing_dust", EnumRarity.COMMON),
                new EZItemFireproof("ender_fragment", EnumRarity.COMMON),
                new EZItemFireproof("epic_ink_sac", EnumRarity.EPIC),
                new EZItemBow("guardian_bow", 800, 1.1F, 1, 0.9F, 1.2F, Ingredient.fromStacks(new ItemStack(Items.IRON_INGOT)), EnumRarity.UNCOMMON),
                new EZItem("owl_egg", EnumRarity.COMMON)
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
