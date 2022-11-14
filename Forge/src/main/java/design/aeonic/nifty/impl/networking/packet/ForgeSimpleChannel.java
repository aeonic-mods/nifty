package design.aeonic.nifty.impl.networking.packet;

import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.networking.packet.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeSimpleChannel extends SimpleChannelImpl {
    private final SimpleChannel forgeChannel;
    private int counter = -1;

    public ForgeSimpleChannel(ResourceLocation id, Supplier<String> protocolVersion) {
        super(id, protocolVersion);

        forgeChannel = NetworkRegistry.newSimpleChannel(id, protocolVersion, this::protocolVersionMatches, this::protocolVersionMatches);
    }

    boolean protocolVersionMatches(String version) {
        return getProtocolVersion().equals(version);
    }

    @Override
    public <T> PacketHandler<T> registerPacket(String id, Class<T> packetClass, BiConsumer<T, ExtraFriendlyByteBuf> serializer, Function<ExtraFriendlyByteBuf, T> deserializer) {
        PacketHandlerImpl<T> handler = (PacketHandlerImpl<T>) super.registerPacket(id, packetClass, serializer, deserializer);
        forgeChannel.registerMessage(++counter, packetClass, (packet, buf) -> {
            writeProtocolVersion(buf);
            buf.writeUtf(id);
            handler.serialize(packet, ExtraFriendlyByteBuf.of(buf));
        }, buf -> {
            checkProtocolVersion(buf);
            String packetId = buf.readUtf();
            if (!packetId.equals(id)) throw new IllegalArgumentException("Packet id mismatch: expected " + id + ", got " + packetId);
            return handler.deserialize(ExtraFriendlyByteBuf.of(buf));
        }, (packet, ctx) -> {
            NetworkEvent.Context context = ctx.get();
            if (context.getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                handler.getClientCallback().handle(context::enqueueWork, Minecraft.getInstance(), packet);
                context.setPacketHandled(true);
            } else {
                handler.getServerCallback().handle(context::enqueueWork, Objects.requireNonNull(context.getSender()).server, context.getSender(), packet);
                context.setPacketHandled(true);
            }
        });

        return handler;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void sendToServer(String id, T packet) {
        PacketHandler<T> handler = (PacketHandler<T>) packetHandlers.get(id);
        if (handler == null) throw new IllegalArgumentException("No packet handler registered for id " + id);
        forgeChannel.sendToServer(packet);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void sendToClients(String id, T packet, ServerPlayer... players) {
        PacketHandler<T> handler = (PacketHandler<T>) packetHandlers.get(id);
        if (handler == null) throw new IllegalArgumentException("No packet handler registered for id " + id);
        for (ServerPlayer player : players) {
            forgeChannel.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    @Override
    protected <T> void sendToServer(String id, PacketHandler<T> handler, Consumer<ExtraFriendlyByteBuf> writer) {}

    @Override
    protected <T> void sendToClients(String id, PacketHandler<T> handler, Consumer<ExtraFriendlyByteBuf> writer, ServerPlayer... players) {}
}
