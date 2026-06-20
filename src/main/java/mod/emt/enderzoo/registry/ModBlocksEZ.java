package mod.emt.enderzoo.registry;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.block.EZBlockConcussionCharge;
import mod.emt.enderzoo.block.EZBlockConfusingCharge;
import mod.emt.enderzoo.block.EZBlockEnderCharge;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
public class ModBlocksEZ {
    public static final EZBlockConcussionCharge CONCUSSION_CHARGE = null;
    public static final EZBlockConfusingCharge CONFUSING_CHARGE = null;
    public static final EZBlockEnderCharge ENDER_CHARGE = null;

    @SubscribeEvent
    public static void registerBlocks(@Nonnull final RegistryEvent.Register<Block> event) {
        final IForgeRegistry<Block> registry = event.getRegistry();
        registry.registerAll(
                new EZBlockConcussionCharge("concussion_charge"),
                new EZBlockConfusingCharge("confusing_charge"),
                new EZBlockEnderCharge("ender_charge")
        );
    }

    @SuppressWarnings("ConstantConditions")
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();
        registry.registerAll(
                new ItemBlock(CONCUSSION_CHARGE).setRegistryName(CONCUSSION_CHARGE.getRegistryName()).setTranslationKey(CONCUSSION_CHARGE.getTranslationKey()).setCreativeTab(EnderSafari.tabEZ),
                new ItemBlock(CONFUSING_CHARGE).setRegistryName(CONFUSING_CHARGE.getRegistryName()).setTranslationKey(CONFUSING_CHARGE.getTranslationKey()).setCreativeTab(EnderSafari.tabEZ),
                new ItemBlock(ENDER_CHARGE).setRegistryName(ENDER_CHARGE.getRegistryName()).setTranslationKey(ENDER_CHARGE.getTranslationKey()).setCreativeTab(EnderSafari.tabEZ)
        );
    }

    @SuppressWarnings("ConstantConditions")
    public static void registerBlockModels(ModelRegistryEvent event) {
        registerItemModel(CONCUSSION_CHARGE);
        registerItemModel(CONFUSING_CHARGE);
        registerItemModel(ENDER_CHARGE);
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    private static void registerItemModel(Block block) {
        ModelResourceLocation loc = new ModelResourceLocation(block.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, loc);
    }
}
