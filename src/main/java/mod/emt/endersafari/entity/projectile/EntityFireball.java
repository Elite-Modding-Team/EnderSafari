package mod.emt.endersafari.entity.projectile;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import mod.emt.endersafari.entity.EntityUtil;
import mod.emt.endersafari.network.ESPacketHandler;
import mod.emt.endersafari.network.MessageFireballImpactFX;
import mod.emt.endersafari.utils.ParticleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class EntityFireball extends Entity {
    private static final Predicate<Entity> VALID_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, entity -> entity.canBeCollidedWith());

    private static final DataParameter<Float> VALUE = EntityDataManager.createKey(EntityFireball.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> DEAD = EntityDataManager.createKey(EntityFireball.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> LIFETIME = EntityDataManager.createKey(EntityFireball.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityFireball.class, DataSerializers.VARINT);

    private Entity shootingEntity;
    private IESProjectileEffect effect;
    private IESProjectilePreset preset;
    private double gravity;
    private int homingTime;
    private double homingRange;
    private int homingIndex;
    private int homingModulo;
    private Entity homingTarget;
    private Predicate<Entity> homingPredicate;

    public EntityFireball(World worldIn) {
        super(worldIn);
        this.setInvisible(true);
        this.getDataManager().register(VALUE, 0.0F);
        this.getDataManager().register(DEAD, false);
        this.getDataManager().register(LIFETIME, 160);
        this.getDataManager().register(COLOR, new Color(255,64,16).getRGB());
    }

    public void initCustom(double x, double y, double z, double vx, double vy, double vz, double value, Entity shootingEntity) {
        this.setPosition(x, y, z);
        this.motionX = vx;
        this.motionY = vy;
        this.motionZ = vz;
        this.setSize((float) value / 10.0f, (float) value / 10.0f);
        this.getDataManager().set(VALUE, (float) value);
        this.shootingEntity = shootingEntity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public void setColor(int red, int green, int blue, int alpha) {
        int color = new Color((red * alpha) / 255, (green * alpha) / 255, (blue * alpha) / 255).getRGB();
        getDataManager().set(COLOR, color);
    }

    public void setColor(int hex) {
        Color color = new Color(hex);
        setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public void setHoming(int time, double range, int index, int modulo, Predicate<Entity> predicate) {
        this.homingTime = time;
        this.homingRange = range;
        this.homingIndex = index;
        this.homingModulo = modulo;
        this.homingPredicate = predicate;
    }

    public void setPreset(IESProjectilePreset preset) {
        this.preset = preset;
    }

    public void setEffect(IESProjectileEffect effect) {
        this.effect = effect;
    }

    public void setLifetime(int lifetime) {
        getDataManager().set(LIFETIME, lifetime);
    }

    @Nullable
    public Entity getShooter() {
        return shootingEntity;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        getDataManager().set(VALUE, compound.getFloat("value"));
        getDataManager().set(COLOR, compound.getInteger("color"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setFloat("value", getDataManager().get(VALUE));
        compound.setInteger("color", getDataManager().get(COLOR));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        int lifetime = getDataManager().get(LIFETIME);
        getDataManager().set(LIFETIME, lifetime - 1);
        World world = getEntityWorld();

        if (lifetime <= 0) {
            world.removeEntity(this);
            setDead();
        }

        if (!getDataManager().get(DEAD)) {
            float value = getDataManager().get(VALUE) - 0.025F;
            getDataManager().set(VALUE, value);

            if (value <= 0) {
                world.removeEntity(this);
            }

            Vec3d currentPos = new Vec3d(posX, posY, posZ);
            Vec3d nextPos = new Vec3d(posX + motionX, posY + motionY, posZ + motionZ);
            RayTraceResult rayTrace = world.rayTraceBlocks(currentPos, nextPos, false, true, false);

            if (rayTrace != null && rayTrace.typeOfHit != RayTraceResult.Type.MISS) {
                nextPos = rayTrace.hitVec;
            }

            RayTraceResult entityHit = EntityUtil.findEntityOnPath(world, this, shootingEntity, getEntityBoundingBox(), currentPos, nextPos, VALID_TARGETS);

            if (entityHit != null) {
                nextPos = entityHit.hitVec;
                rayTrace = entityHit;
            }

            posX = nextPos.x;
            posY = nextPos.y;
            posZ = nextPos.z;
            motionY += gravity;

            if (!world.isRemote && rayTrace != null && rayTrace.typeOfHit != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, rayTrace)) {
                onHit(rayTrace);
            }

            handleHoming(lifetime, world);

            if (world.isRemote) {
                Color particleColor = new Color(getDataManager().get(COLOR), true);

                double deltaX = posX - prevPosX;
                double deltaY = posY - prevPosY;
                double deltaZ = posZ - prevPosZ;
                double distance = Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) * 10.0);

                for (double i = 0; i < distance; i++) {
                    double coefficient = i / distance;
                    ParticleUtil.spawnParticleGlow(world, (float) (prevPosX + (posX - prevPosX) * coefficient), (float) (prevPosY + (posY - prevPosY) * coefficient), (float) (prevPosZ + (posZ - prevPosZ) * coefficient),
                            0.0125F * (rand.nextFloat() - 0.5F), 0.0125F * (rand.nextFloat() - 0.5F), 0.0125F * (rand.nextFloat() - 0.5F),
                            particleColor.getRed(), particleColor.getGreen(), particleColor.getBlue(),
                            1.0F, value / 1.75F, 24
                    );
                }
            }
            setPosition(posX, posY, posZ);
        } else {
            motionX = 0;
            motionY = 0;
            motionZ = 0;
        }
    }

    private void handleHoming(int lifetime, World world) {
        if (homingTime <= 0) {
            return;
        }

        if (!isTargetInvalid(homingTarget)) {
            double targetX = homingTarget.posX;
            double targetY = homingTarget.posY + homingTarget.height / 2.0;
            double targetZ = homingTarget.posZ;
            Vec3d targetVector = new Vec3d(targetX - posX, targetY - posY, targetZ - posZ);
            double distance = targetVector.length();
            targetVector = targetVector.scale(0.3 / distance);
            double weight = 0.0;

            if (distance <= homingRange) {
                weight = 0.9 * ((homingRange - distance) / homingRange);
            }

            motionX = (0.9 - weight) * motionX + (0.1 + weight) * targetVector.x;
            motionY = (0.9 - weight) * motionY + (0.1 + weight) * targetVector.y;
            motionZ = (0.9 - weight) * motionZ + (0.1 + weight) * targetVector.z;

            homingTime--;
            return;
        }

        if (lifetime % 5 != 0) {
            return;
        }

        AxisAlignedBB searchBox = new AxisAlignedBB(posX - homingRange, posY - homingRange, posZ - homingRange, posX + homingRange, posY + homingRange, posZ + homingRange);
        List<Entity> entities = world.getEntitiesInAABBexcluding(this, searchBox, homingPredicate);
        Entity fallbackTarget = null;

        for (Entity entity : entities) {
            long leastSignificantBits = entity.getUniqueID().getLeastSignificantBits() & 0xFFFF;
            if (leastSignificantBits % homingModulo == homingIndex % homingModulo) {
                homingTarget = entity;
            }
            fallbackTarget = entity;
        }

        if (homingTarget == null) {
            homingTarget = fallbackTarget;
        }
    }

    private boolean isTargetInvalid(@Nullable Entity entity) {
        return entity == null || entity.isDead;
    }

    private void onHit(RayTraceResult rayTrace) {
        ESPacketHandler.INSTANCE.sendToAll(new MessageFireballImpactFX(posX, posY, posZ, getDataManager().get(VALUE) / 1.75f, getDataManager().get(COLOR)));
        getDataManager().set(LIFETIME, 20);
        getDataManager().set(DEAD, true);
        if (effect != null) {
            effect.onHit(world, rayTrace, preset);
        }
    }
}
