package net.burkey.variouswitchesmod.network;

import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "";
    private static int id = 1;
    private static final SimpleChannel instance = NetworkRegistry.newSimpleChannel(
             ResourceLocation.fromNamespaceAndPath(VariousWitchesMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    public static void register(){
        instance.messageBuilder(S2CSpawnMeteoricParticlesPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CSpawnMeteoricParticlesPacket::toBytes)
                .decoder(S2CSpawnMeteoricParticlesPacket::new)
                .consumerNetworkThread(S2CSpawnMeteoricParticlesPacket::handle)
                .add();



    }

    public static void sendToServer(Object message){
        instance.send(PacketDistributor.SERVER.noArg(), message);
    }
    public static void sendToAllClients(Object message){
        instance.send(PacketDistributor.ALL.noArg(), message);
    }
    public static void sendToPlayer(Object packet, ServerPlayer player) {
        instance.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static void sendToTracking(Entity entity, Object message) {
        instance.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

}
