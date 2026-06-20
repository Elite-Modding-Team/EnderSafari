package mod.emt.enderzoo.registry;

import javax.annotation.Nonnull;

import mod.emt.enderzoo.EnderSafari;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = EnderSafari.MOD_ID)
public class ModRegistryEZ {
    @SubscribeEvent
    public static void registerBlocks(@Nonnull final RegistryEvent.Register<Block> event) {
        ModBlocksEZ.registerBlocks(event);
    }

    @SubscribeEvent
    public static void registerItems(@Nonnull final RegistryEvent.Register<Item> event) {
        ModBlocksEZ.registerBlockItems(event);
        ModItemsEZ.registerItems(event);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModItemsEZ.registerItemModels(event);
        ModBlocksEZ.registerBlockModels(event);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        for (ModSoundEventsEZ mcSound : ModSoundEventsEZ.values()) {
            event.getRegistry().register(mcSound.getSoundEvent());
        }
    }
}
