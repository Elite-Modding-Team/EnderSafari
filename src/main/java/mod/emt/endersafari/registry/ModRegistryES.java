package mod.emt.endersafari.registry;

import javax.annotation.Nonnull;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.compat.EnderIOIntegration;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import utils.helpers.CompatHelper;

@Mod.EventBusSubscriber(modid = EnderSafari.MOD_ID)
public class ModRegistryES {
    @SubscribeEvent
    public static void registerBlocks(@Nonnull final RegistryEvent.Register<Block> event) {
        ModBlocksES.registerBlocks(event);
    }

    @SubscribeEvent
    public static void registerItems(@Nonnull final RegistryEvent.Register<Item> event) {
        ModBlocksES.registerBlockItems(event);
        ModItemsES.registerItems(event);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModItemsES.registerItemModels(event);
        ModBlocksES.registerBlockModels(event);
    }

    @SubscribeEvent
    public static void registerRecipes(@Nonnull final RegistryEvent.Register<IRecipe> event) {
        ModRecipesES.registerBrewingRecipes();

        if (CompatHelper.isEnderIOLoaded) {
            EnderIOIntegration.registerBrewingRecipes();
        }
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        for (ModSoundEventsES mcSound : ModSoundEventsES.values()) {
            event.getRegistry().register(mcSound.getSoundEvent());
        }
    }
}
