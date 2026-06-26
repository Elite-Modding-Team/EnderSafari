package mod.emt.enderzoo.registry;

import mod.emt.enderzoo.EnderSafari;
import mod.emt.enderzoo.client.render.*;
import mod.emt.enderzoo.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = EnderSafari.MOD_ID)
public class ModEntitiesEZ {
    private static int entityID = 0;

    public static void registerEntity(String name, Class<? extends Entity> clazz, int eggColor1, int eggColor2) {
        EntityRegistry.registerModEntity(new ResourceLocation(EnderSafari.MOD_ID, name), clazz, EnderSafari.MOD_ID + "." + name, entityID++, EnderSafari.instance, 64, 1, true, eggColor1, eggColor2);
    }

    public static void registerEntity(String name, Class<? extends Entity> clazz, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity(new ResourceLocation(EnderSafari.MOD_ID, name), clazz, EnderSafari.MOD_ID + "." + name, entityID++, EnderSafari.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
    }

    @SubscribeEvent
    public static void registerEntities(@Nonnull final RegistryEvent.Register<EntityEntry> event) {
        registerEntity("concussion_creeper", EntityConcussionCreeper.class, 4032112, 2897273);
        registerEntity("dire_cube", EntityDireCube.class, 12158300, 5848361);
        registerEntity("enderized_zombie", EntityEnderizedZombie.class, 1257301, 2829596);
        registerEntity("epic_squid", EntityEpicSquid.class, 10223617, 15484494);
        registerEntity("void_cube", EntityVoidCube.class, 0, 11184810);

        registerEntity("primed_concussion_charge", EntityConcussionChargePrimed.class, 64, 1, true);
        registerEntity("primed_confusing_charge", EntityConfusingChargePrimed.class, 64, 1, true);
        registerEntity("primed_ender_charge", EntityEnderChargePrimed.class, 64, 1, true);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderers(@Nonnull final ModelRegistryEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EntityConcussionCreeper.class, new RenderConcussionCreeper.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityDireCube.class, new RenderDireCube.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityEnderizedZombie.class, new RenderEnderizedZombie.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityEpicSquid.class, new RenderEpicSquid.Factory());
        RenderingRegistry.registerEntityRenderingHandler(EntityVoidCube.class, new RenderVoidCube.Factory());

        RenderingRegistry.registerEntityRenderingHandler(EntityConcussionChargePrimed.class, new RenderChargePrimed.Factory(() -> ModBlocksEZ.CONCUSSION_CHARGE.getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityConfusingChargePrimed.class, new RenderChargePrimed.Factory(() -> ModBlocksEZ.CONFUSING_CHARGE.getDefaultState()));
        RenderingRegistry.registerEntityRenderingHandler(EntityEnderChargePrimed.class, new RenderChargePrimed.Factory(() -> ModBlocksEZ.ENDER_CHARGE.getDefaultState()));
    }
}
