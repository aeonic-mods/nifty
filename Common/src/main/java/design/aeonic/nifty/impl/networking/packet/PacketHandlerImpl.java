package design.aeonic.nifty.impl.networking.packet;

import design.aeonic.nifty.api.networking.packet.*;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class PacketHandlerImpl<T> implements PacketHandler<T> {
    private final SimpleChannel channel;
    private final Class<T> packetClass;
    private final BiConsumer<T, ExtraFriendlyByteBuf> serializer;
    private final Function<ExtraFriendlyByteBuf, T> deserializer;

    private ServerPacketCallback<T> serverCallback;
    private ClientPacketCallback<T> clientCallback;

    public PacketHandlerImpl(SimpleChannel channel, Class<T> packetClass, BiConsumer<T, ExtraFriendlyByteBuf> serializer, Function<ExtraFriendlyByteBuf, T> deserializer) {
        this.channel = channel;
        this.packetClass = packetClass;
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    public ServerPacketCallback<T> getServerCallback() {
        if (serverCallback == null) throw new IllegalStateException("Server callback not set for packet " + packetClass + " " + channel.getProtocolVersion());
        return serverCallback;
    }

    public ClientPacketCallback<T> getClientCallback() {
        if (clientCallback == null) throw new IllegalStateException("Client callback not set for packet " + packetClass + " " + channel.getProtocolVersion());
        return clientCallback;
    }

    @Override
    public SimpleChannel getChannel() {
        return channel;
    }

    @Override
    public Class<T> getPacketClass() {
        return packetClass;
    }

    @Override
    public void serialize(T packet, ExtraFriendlyByteBuf buffer) {
        serializer.accept(packet, buffer);
    }

    @Override
    public T deserialize(ExtraFriendlyByteBuf buffer) {
        return deserializer.apply(buffer);
    }

    @Override
    public PacketHandler<T> onServerReceived(ServerPacketCallback<T> callback) {
        this.serverCallback = callback;
        return this;
    }

    @Override
    public PacketHandler<T> onClientReceived(ClientPacketCallback<T> callback) {
        this.clientCallback = callback;
        return this;
    }
}
