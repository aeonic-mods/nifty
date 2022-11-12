package design.aeonic.nifty.impl.networking.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.function.Supplier;

public class FabricSimpleChannel extends SimpleChannelImpl {
    public FabricSimpleChannel(ResourceLocation id, Supplier<String> protocolVersion) {
        super(id, protocolVersion);

        ServerPlayNetworking.registerGlobalReceiver(id, this::receiveServer);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPlayNetworking.registerGlobalReceiver(id, this::receiveClient);
        }
    }

    void receiveServer(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        checkProtocolVersion(buf);
        ServerboundPacketType<?> type = serverPackets.get(buf.readUtf());
        if (type != null) type.handle(server::submit, server, player, buf);
    }

    void receiveClient(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        checkProtocolVersion(buf);
        ClientboundPacketType<?> type = clientPackets.get(buf.readUtf());
        if (type != null) type.handle(client::submit, client, buf);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void sendToServer(String id, T packet) {
        ServerboundPacketType<T> packetType = (ServerboundPacketType<T>) serverPackets.get(id);
        if (packetType == null) throw new IllegalArgumentException("Unknown packet type \"" + id + "\" for channel " + this.id);

        FriendlyByteBuf buf = PacketByteBufs.create();
        writeProtocolVersion(buf);
        buf.writeUtf(id);
        packetType.encode(buf, packet);

        ClientPlayNetworking.send(this.id, buf);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void sendToClients(String id, T packet, ServerPlayer... players) {
        ClientboundPacketType<T> packetType = (ClientboundPacketType<T>) clientPackets.get(id);
        if (packetType == null) throw new IllegalArgumentException("Unknown packet type \"" + id + "\" for channel " + this.id);

        FriendlyByteBuf buf = PacketByteBufs.create();
        writeProtocolVersion(buf);
        buf.writeUtf(id);
        packetType.encode(buf, packet);

        for (ServerPlayer player : players) {
            ServerPlayNetworking.send(player, this.id, buf);
        }
    }
}
