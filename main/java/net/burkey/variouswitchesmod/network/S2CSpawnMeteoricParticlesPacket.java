package net.burkey.variouswitchesmod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CSpawnMeteoricParticlesPacket {
    private final double x, y, z;
    private final float strength;

    public S2CSpawnMeteoricParticlesPacket(double x, double y, double z, float strength) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.strength = strength;
    }

    public S2CSpawnMeteoricParticlesPacket(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.strength = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeFloat(strength);
    }

    // This handler runs on the main client thread thanks to consumerMainThread()
    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        System.out.println("teest");
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // Get Minecraft instance safely
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) return;

            Level level = mc.level;
            if (level == null) return;
            System.out.println(level);

            // Additional safety check - ensure we're in a world
            if (!level.isClientSide()) return;

            spawnParticlesClientSide(level);
        });
        return true;
    }

    // Marked as only callable on the client. Now private since handle() is the entry point.
    private void spawnParticlesClientSide(Level level) {
        int rings = 5;
        int particlesPerRing = 20;
        double radius = 0.2 * strength;

        for (int r = 0; r <= rings; r++) {
            double currentRadius = radius * (r / (double) rings);

            for (int i = 0; i < particlesPerRing; i++) {
                double angle = 2 * Math.PI * i / particlesPerRing;
                double offsetX = currentRadius * Math.cos(angle);
                double offsetZ = currentRadius * Math.sin(angle);
                double posY = y + 0.1;

                // Use addAlwaysVisibleParticle instead of addParticle for better reliability
                level.addAlwaysVisibleParticle(
                        ParticleTypes.FLAME,
                        true, // force particle to show regardless of distance
                        x + offsetX, posY, z + offsetZ,
                        0, 0, 0
                );

                // Alternatively, use the regular addParticle but ensure it's client-side only
            /*
            level.addParticle(
                ParticleTypes.FLAME,
                x + offsetX, posY, z + offsetZ,
                0, 0, 0
            );
            */
            }
        }
    }
}