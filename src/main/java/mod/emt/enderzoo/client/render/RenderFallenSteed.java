package mod.emt.enderzoo.client.render;

import java.util.Map;

import com.google.common.collect.Maps;

import javax.annotation.Nonnull;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.entity.EntityFallenSteed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFallenSteed extends RenderHorse {
    private static final @Nonnull String BASE_TEXTURE = EnderSafari.MOD_ID + ":textures/entity/fallen_steed.png";
    private static final @Nonnull ResourceLocation HORSE_TEXTURE = new ResourceLocation(BASE_TEXTURE);
    private static final @Nonnull String[] HORSE_ARMOR_TEXTURES = new String[]{
            null,
            "textures/entity/horse/armor/horse_armor_iron.png",
            "textures/entity/horse/armor/horse_armor_gold.png",
            "textures/entity/horse/armor/horse_armor_diamond.png"
    };

    private static final @Nonnull Map<String, ResourceLocation> TEXTURE_CACHE = Maps.newHashMap();

    public RenderFallenSteed(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    protected @Nonnull ResourceLocation getEntityTexture(@Nonnull EntityHorse horse) {
        if (horse.getTotalArmorValue() == 0) {
            return HORSE_TEXTURE;
        } else {
            return getArmoredTexture(horse);
        }
    }

    private @Nonnull ResourceLocation getArmoredTexture(EntityHorse horse) {
        String armorPath = HORSE_ARMOR_TEXTURES[horse.getHorseArmorType().ordinal()];
        if (armorPath == null) {
            return HORSE_TEXTURE;
        }

        ResourceLocation resourceLocation = TEXTURE_CACHE.get(armorPath);
        if (resourceLocation == null) {
            String uniqueLayerKey = "layered:" + BASE_TEXTURE + "," + armorPath;
            resourceLocation = new ResourceLocation(uniqueLayerKey);
            Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, new LayeredTexture(BASE_TEXTURE, armorPath));
            TEXTURE_CACHE.put(armorPath, resourceLocation);
        }
        return resourceLocation;
    }

    public static class Factory implements IRenderFactory<EntityFallenSteed> {
        @Override
        public Render<? super EntityFallenSteed> createRenderFor(RenderManager manager) {
            return new RenderFallenSteed(manager);
        }
    }
}