package design.aeonic.nifty.impl.networking.packet;

import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.networking.packet.PacketHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
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

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FabricSimpleChannel extends SimpleChannelImpl {
    public FabricSimpleChannel(ResourceLocation id, Supplier<String> protocolVersion) {
        super(id, protocolVersion);

        ServerPlayNetworking.registerGlobalReceiver(id, this::receiveServer);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientPlayNetworking.registerGlobalReceiver(id, this::receiveClient);
        }
    }

    @Override
    protected <T> void sendToServer(String id, PacketHandler<T> handler, Consumer<ExtraFriendlyByteBuf> writer) {
        var buffer = ExtraFriendlyByteBuf.create();
        writer.accept(buffer);
        ClientPlayNetworking.send(this.id, buffer);
    }

    @Override
    protected <T> void sendToClients(String id, PacketHandler<T> handler, Consumer<ExtraFriendlyByteBuf> writer, ServerPlayer... players) {
        var buffer = ExtraFriendlyByteBuf.create();
        writer.accept(buffer);
        for (var player : players) {
            ServerPlayNetworking.send(player, this.id, buffer);
        }
    }

    @SuppressWarnings("unchecked")
    <T> void receiveServer(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        checkProtocolVersion(buf);

        String id = buf.readUtf();
        PacketHandlerImpl<T> packetHandler = (PacketHandlerImpl<T>) packetHandlers.get(id);
        if (packetHandler == null) throw new IllegalArgumentException("No packet handler registered for id " + id);

        packetHandler.getServerCallback().handle(server::submit, server, player, packetHandler.deserialize(ExtraFriendlyByteBuf.of(buf)));
    }

    @SuppressWarnings("unchecked")
    <T> void receiveClient(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        checkProtocolVersion(buf);

        String id = buf.readUtf();
        PacketHandlerImpl<T> packetHandler = (PacketHandlerImpl<T>) packetHandlers.get(id);
        if (packetHandler == null) throw new IllegalArgumentException("No packet handler registered for id " + id);

        packetHandler.getClientCallback().handle(client::execute, client, packetHandler.deserialize(ExtraFriendlyByteBuf.of(buf)));
    }
}
