package mod.emt.endersafari.event;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.entity.IFaeMob;
import mod.emt.endersafari.registry.ModItemsES;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = EnderSafari.MOD_ID)
public class EventOnLeafToolHurt {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLeafToolHurt(LivingHurtEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        Entity trueSource = event.getSource().getTrueSource();

        if (trueSource instanceof EntityLivingBase) {
            EntityLivingBase attacker = (EntityLivingBase) trueSource;
            if (attacker.getHeldItemMainhand().getItem() == ModItemsES.LEAF_AXE || attacker.getHeldItemMainhand().getItem() == ModItemsES.LEAF_SWORD) {
                if (entity instanceof IFaeMob || (entity instanceof EntityAnimal && !(entity instanceof IMob))) {
                    event.setAmount(event.getAmount() * 1.5F);
                }
            }
        }
    }
}
