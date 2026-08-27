package mod.emt.endersafari.client.particle;

import mod.emt.endersafari.EnderSafari;
import mod.emt.endersafari.utils.ColorUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParticleSparkle extends ParticleGlow {
    public static final ResourceLocation texture = new ResourceLocation(EnderSafari.MOD_ID, "particle/sparkle");

    public ParticleSparkle(World world, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float a, float scale, int lifetime) {
        super(world, x, y, z, vx, vy, vz, r, g, b, a, scale, lifetime);
        this.particleMaxAge = lifetime;
    }

    public ParticleSparkle(World world, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float a, float scale, int lifetime, boolean growth) {
        super(world, x, y, z, vx, vy, vz, r, g, b, a, scale, lifetime, growth);
    }

    public ParticleSparkle(World world, double x, double y, double z, double vx, double vy, double vz, float a, float scale, int lifetime, int[][] transitionColors) {
        super(world, x, y, z, vx, vy, vz, a, scale, lifetime, transitionColors);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }

        this.motionY -= 0.04D * (double) this.particleGravity;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9D;
        this.motionY += 0.003D;
        this.motionZ *= 0.9D;

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        if (transitionColors != null) {
            float progress = Math.min(1.0F, (float) particleAge / (float) particleMaxAge);
            int[] color = ColorUtil.colorTransition(transitionColors, progress);
            this.setRBGColorF(color[0] / 255.0F, color[1] / 255.0F, color[2] / 255.0F);
        }

        float lifeCoeff = ((float) this.particleMaxAge - (float) this.particleAge) / (float) this.particleMaxAge;
        if (lifeCoeff > 0.5F) {
            this.particleScale = this.initScale + this.initScale * (1.0F - lifeCoeff);
        } else {
            this.particleScale = 2.0F * this.initScale * lifeCoeff;
        }

        if (world.rand.nextInt(3) == 0 && this.particleAge < this.particleMaxAge) {
            this.particleAge++;
        }

        this.particleAlpha = this.initAlpha * (1.0F - lifeCoeff);
        this.prevParticleAngle = this.particleAngle;
        this.particleAngle += 1.0F;
    }

    @Override
    protected ResourceLocation getParticleTexture() {
        return texture;
    }
}
