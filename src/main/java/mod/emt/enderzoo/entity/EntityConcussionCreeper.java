package mod.emt.enderzoo.entity;

import mod.emt.enderzoo.misc.ConcussionExplosion;
import mod.emt.enderzoo.registry.ModLootTablesEZ;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

public class EntityConcussionCreeper extends EntityCreeper {
    // We're ignoring timeSinceIgnited for this
    private int timeSinceIgnitedCustom;

    public EntityConcussionCreeper(World world) {
        super(world);
    }

    // Very hacky but it's a way to get around the default explosions
    @Override
    public void onUpdate() {
        super.onUpdate();

        // Set it to 1 so the original code never fires
        this.timeSinceIgnited = 1;
        if (this.isEntityAlive()) {
            if (this.hasIgnited()) {
                this.setCreeperState(1);
            }

            int state = this.getCreeperState();

            if (state > 0 && this.timeSinceIgnitedCustom == 0) {
                this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
            }

            this.lastActiveTime = this.timeSinceIgnitedCustom;
            this.timeSinceIgnitedCustom += state;

            if (this.timeSinceIgnitedCustom < 0) {
                this.timeSinceIgnitedCustom = 0;
            }

            if (this.timeSinceIgnitedCustom >= this.fuseTime) {
                this.timeSinceIgnitedCustom = this.fuseTime;

                if (!this.world.isRemote) {
                    ConcussionExplosion.create(this.world, this, this.posX, this.posY, this.posZ, 4.0F, false, false);
                    this.setDead();
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getCreeperFlashIntensity(float partialTicks) {
        // Override so the new timeSinceIgnitedCustom is utilized
        return ((float) this.lastActiveTime + (float) (this.timeSinceIgnitedCustom - this.lastActiveTime) * partialTicks) / (float) (this.fuseTime - 2);
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return ModLootTablesEZ.CONCUSSION_CREEPER;
    }
}
