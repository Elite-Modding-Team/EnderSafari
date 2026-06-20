package mod.emt.enderzoo.entity;

import mod.emt.enderzoo.misc.EnderExplosion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class EntityEnderChargePrimed extends EntityChargePrimed {
    public EntityEnderChargePrimed(World world) {
        super(world);
        preventEntitySpawning = true;
        setSize(0.98F, 0.98F);
    }

    public EntityEnderChargePrimed(World world, double x, double y, double z, EntityLivingBase igniter) {
        super(world, x, y, z, igniter);
    }

    @Override
    public void explode() {
        EnderExplosion.create(this.world, this, this.posX, this.posY, this.posZ, 4.0F, false, false);
    }
}
