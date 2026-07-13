package mod.emt.endersafari.event;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.entity.EntityDireWolf;
import mod.emt.endersafari.entity.EntityOwl;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = EnderSafari.MOD_ID)
public class EventTaskOnJoin {
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof AbstractSkeleton) {
            EntityCreature skeleton = (EntityCreature) event.getEntity();
            skeleton.tasks.addTask(3, new EntityAIAvoidEntity<>(skeleton, EntityDireWolf.class, 6.0F, 1.0D, 1.2D));
        }

        if (event.getEntity() instanceof EntitySpider) {
            EntityCreature spider = (EntityCreature) event.getEntity();
            spider.tasks.addTask(3, new EntityAIAvoidEntity<>(spider, EntityOwl.class, 6.0F, 1.0D, 1.2D));
        }
    }
}
