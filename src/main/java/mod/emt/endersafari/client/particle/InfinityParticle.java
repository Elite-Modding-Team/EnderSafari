package mod.emt.endersafari.client.particle;

import javax.annotation.Nonnull;
import javax.vecmath.Vector4f;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class InfinityParticle extends Particle {
    private static final int INIT_TIME = 25;
    private static final int FADE_TIME = 5;
    private static final int AGE_LIMIT = 40;

    public InfinityParticle(@Nonnull World world, @Nonnull BlockPos location, @Nonnull Vector4f offset) {
        this(world, location, new Vector4f(0, 0, 0, 0.4f), offset);
    }

    public InfinityParticle(@Nonnull World world, @Nonnull BlockPos location, @Nonnull Vector4f color, @Nonnull Vector4f offset) {
        super(world, location.getX(), location.getY(), location.getZ());
        setRBGColorF(color.x, color.y, color.z);
        setAlphaF(color.w);
        setSize(offset.w, offset.w);
        setPosition(location.getX() + .5f, location.getY() + .5f - height / 2f, location.getZ() + .5f);
        move(offset.x - .5f, offset.y - .5f, offset.z - .5f);
        setMaxAge(AGE_LIMIT);
        particleAge = -rand.nextInt(10);
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    @Override
    public void renderParticle(@Nonnull BufferBuilder worldRendererIn, @Nonnull Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        if (particleAge < 0) {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
        GlStateManager.depthMask(false);
        float scale = Math.min((particleAge + partialTicks) / INIT_TIME, 1);
        float fade = particleAge < FADE_TIME ? 1f : ((particleMaxAge - particleAge) / (float) (particleMaxAge - FADE_TIME));
        GlStateManager.translate(-interpPosX, -interpPosY, -interpPosZ);
        float r = getRedColorF();
        float g = getGreenColorF();
        float b = getBlueColorF();
        float a = particleAlpha * fade;
        AxisAlignedBB bb = getBoundingBox();
        double centerX = bb.minX + (bb.maxX - bb.minX) * 0.5D;
        double centerY = bb.minY + (bb.maxY - bb.minY) * 0.5D;
        double centerZ = bb.minZ + (bb.maxZ - bb.minZ) * 0.5D;
        double halfW = ((bb.maxX - bb.minX) * scale) * 0.5D;
        double halfH = ((bb.maxY - bb.minY) * scale) * 0.5D;
        double halfD = ((bb.maxZ - bb.minZ) * scale) * 0.5D;
        double minX = centerX - halfW;
        double minY = centerY - halfH;
        double minZ = centerZ - halfD;
        double maxX = centerX + halfW;
        double maxY = centerY + halfH;
        double maxZ = centerZ + halfD;
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        // Down
        buffer.pos(minX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, minY, maxZ).color(r, g, b, a).endVertex();

        // Up
        buffer.pos(minX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, maxY, minZ).color(r, g, b, a).endVertex();

        // North
        buffer.pos(minX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, minY, minZ).color(r, g, b, a).endVertex();

        // South
        buffer.pos(minX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, maxY, maxZ).color(r, g, b, a).endVertex();

        // West
        buffer.pos(minX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, maxY, minZ).color(r, g, b, a).endVertex();

        // East
        buffer.pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();

        Tessellator.getInstance().draw();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}
