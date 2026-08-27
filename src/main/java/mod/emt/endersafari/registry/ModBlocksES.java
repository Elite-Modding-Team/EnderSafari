package mod.emt.endersafari.registry;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.block.ESBlockConcussionCharge;
import mod.emt.endersafari.block.ESBlockConfusingCharge;
import mod.emt.endersafari.block.ESBlockEnderCharge;
import mod.emt.endersafari.block.ESBlockLight;
import mod.emt.endersafari.tile.TileLight;
import mod.emt.endersafari.utils.ColorUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
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
public class ModBlocksES {
    public static final ESBlockConcussionCharge CONCUSSION_CHARGE = null;
    public static final ESBlockConfusingCharge CONFUSING_CHARGE = null;
    public static final ESBlockLight CONFUSING_LIGHT = null;
    public static final ESBlockEnderCharge ENDER_CHARGE = null;
    public static final ESBlockLight FAE_LIGHT = null;
    public static final ESBlockLight SPRITE_LIGHT = null;
    public static final ESBlockLight WITHERING_LIGHT = null;

    @SubscribeEvent
    public static void registerBlocks(@Nonnull final RegistryEvent.Register<Block> event) {
        final IForgeRegistry<Block> registry = event.getRegistry();
        registry.registerAll(
                new ESBlockConcussionCharge("concussion_charge"),
                new ESBlockConfusingCharge("confusing_charge"),
                new ESBlockEnderCharge("ender_charge"),
                new ESBlockLight("fae_light", ColorUtil.FAE, false),
                new ESBlockLight("sprite_light", ColorUtil.SPRITE, true),
                new ESBlockLight("confusing_light", null, false),
                new ESBlockLight("withering_light", ColorUtil.WITHER, true)
        );
    }

    @SuppressWarnings("ConstantConditions")
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();
        registry.registerAll(
                new ItemBlock(CONCUSSION_CHARGE).setRegistryName(CONCUSSION_CHARGE.getRegistryName()).setTranslationKey(CONCUSSION_CHARGE.getTranslationKey()).setCreativeTab(EnderSafari.tabEZ),
                new ItemBlock(CONFUSING_CHARGE).setRegistryName(CONFUSING_CHARGE.getRegistryName()).setTranslationKey(CONFUSING_CHARGE.getTranslationKey()).setCreativeTab(EnderSafari.tabEZ),
                new ItemBlock(CONFUSING_LIGHT).setRegistryName(CONFUSING_LIGHT.getRegistryName()).setTranslationKey(CONFUSING_LIGHT.getTranslationKey()).setCreativeTab(EnderSafari.tabEZ),
                new ItemBlock(ENDER_CHARGE).setRegistryName(ENDER_CHARGE.getRegistryName()).setTranslationKey(ENDER_CHARGE.getTranslationKey()).setCreativeTab(EnderSafari.tabEZ),
                new ItemBlock(FAE_LIGHT).setRegistryName(FAE_LIGHT.getRegistryName()).setTranslationKey(FAE_LIGHT.getTranslationKey()).setCreativeTab(EnderSafari.tabEZ),
                new ItemBlock(SPRITE_LIGHT).setRegistryName(SPRITE_LIGHT.getRegistryName()).setTranslationKey(SPRITE_LIGHT.getTranslationKey()).setCreativeTab(EnderSafari.tabEZ),
                new ItemBlock(WITHERING_LIGHT).setRegistryName(WITHERING_LIGHT.getRegistryName()).setTranslationKey(WITHERING_LIGHT.getTranslationKey()).setCreativeTab(EnderSafari.tabEZ)
        );
    }

    @SuppressWarnings("ConstantConditions")
    public static void registerBlockModels(ModelRegistryEvent event) {
        registerItemModel(CONCUSSION_CHARGE);
        registerItemModel(CONFUSING_CHARGE);
        registerItemModel(CONFUSING_LIGHT);
        registerItemModel(ENDER_CHARGE);
        registerItemModel(FAE_LIGHT);
        registerItemModel(SPRITE_LIGHT);
        registerItemModel(WITHERING_LIGHT);
    }

    @SuppressWarnings("ConstantConditions")
    @SideOnly(Side.CLIENT)
    private static void registerItemModel(Block block) {
        ModelResourceLocation loc = new ModelResourceLocation(block.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, loc);
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileLight.class, new ResourceLocation(EnderSafari.MOD_ID, "light"));
    }

    /*@SideOnly(Side.CLIENT)
    public static void registerTileEntityRenderers() {
    }*/
}
