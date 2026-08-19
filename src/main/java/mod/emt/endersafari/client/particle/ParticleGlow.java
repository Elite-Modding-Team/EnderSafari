package mod.emt.endersafari.client.particle;

import mod.emt.endersafari.EnderSafari;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ParticleGlow extends Particle implements IESParticle {
    public float colorR;
    public float colorG;
    public float colorB;
    public float initScale;
    public float initAlpha;
    public ResourceLocation texture = new ResourceLocation(EnderSafari.MOD_ID, "particle/glow_32");

    public ParticleGlow(World world, double x, double y, double z, double vx, double vy, double vz, float r, float g, float b, float a, float scale, int lifetime) {
        super(world, x, y, z, 0, 0, 0);
        this.colorR = r;
        this.colorG = g;
        this.colorB = b;
        if (this.colorR > 1.0) {
            this.colorR = this.colorR / 255.0f;
        }
        if (this.colorG > 1.0) {
            this.colorG = this.colorG / 255.0f;
        }
        if (this.colorB > 1.0) {
            this.colorB = this.colorB / 255.0f;
        }
        this.setRBGColorF(colorR, colorG, colorB);
        this.particleMaxAge = (int) ((float) lifetime * 0.5f);
        this.particleScale = scale;
        this.initScale = scale;
        this.motionX = vx * 2.0f;
        this.motionY = vy * 2.0f;
        this.motionZ = vz * 2.0f;
        this.initAlpha = a;
        this.particleAngle = 2.0f * (float) Math.PI;
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());
        this.setParticleTexture(sprite);
        this.canCollide = true;
    }

    @Override
    public int getBrightnessForRender(float pTicks) {
        return 255;
    }

    @Override
    public boolean shouldDisableDepth() {
        return true;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (rand.nextInt(6) == 0) {
            this.particleAge++;
        }
        float lifeCoeff = (float) this.particleAge / (float) this.particleMaxAge;
        this.particleScale = initScale - initScale * lifeCoeff;
        this.particleAlpha = initAlpha * (1.0f - lifeCoeff);
        this.prevParticleAngle = particleAngle;
        particleAngle += 1.0f;
    }

    @Override
    public boolean alive() {
        return this.particleAge < this.particleMaxAge;
    }

    @Override
    public boolean isAdditive() {
        return true;
    }

    @Override
    public boolean renderThroughBlocks() {
        return false;
    }
}
