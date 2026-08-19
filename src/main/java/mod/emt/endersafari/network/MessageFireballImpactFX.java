package mod.emt.endersafari.network;

import io.netty.buffer.ByteBuf;
import mod.emt.endersafari.registry.ModSoundEventsES;
import mod.emt.endersafari.utils.ParticleUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.Random;

public class MessageFireballImpactFX implements IMessage {
    public static Random random = new Random();
    public double posX = 0;
    public double posY = 0;
    public double posZ = 0;
    public double value = 0;
    public int packedColor;

    public MessageFireballImpactFX() {
        super();
    }

    public MessageFireballImpactFX(double x, double y, double z, double value, int packedColor) {
        super();
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.value = value;
        this.packedColor = packedColor;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        posX = buf.readDouble();
        posY = buf.readDouble();
        posZ = buf.readDouble();
        value = buf.readDouble();
        packedColor = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(posX);
        buf.writeDouble(posY);
        buf.writeDouble(posZ);
        buf.writeDouble(value);
        buf.writeInt(packedColor);
    }

    public static class MessageHolder implements IMessageHandler<MessageFireballImpactFX, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(final MessageFireballImpactFX message, final MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    Color color = new Color(message.packedColor);
                    World world = Minecraft.getMinecraft().world;
                    world.playSound(message.posX, message.posY, message.posZ, message.value > 7.0D ? ModSoundEventsES.ENTITY_LARGE_FIREBALL_HIT.getSoundEvent() : ModSoundEventsES.ENTITY_FIREBALL_HIT.getSoundEvent(), SoundCategory.NEUTRAL, 1.0F, 1.0F, false);
                    for (int k = 0; k < 80; k++) {
                        ParticleUtil.spawnParticleGlow(world, (float) message.posX, (float) message.posY, (float) message.posZ, ((float) message.value / 3.5F) * 0.125F * (random.nextFloat() - 0.5F), ((float) message.value / 3.5F) * 0.125F * (random.nextFloat() - 0.5F), ((float) message.value / 3.5F) * 0.125F * (random.nextFloat() - 0.5F), color.getRed(), color.getGreen(), color.getBlue(), 1.0F, (float) message.value, 24);
                    }
                });
            }
            return null;
        }
    }
}