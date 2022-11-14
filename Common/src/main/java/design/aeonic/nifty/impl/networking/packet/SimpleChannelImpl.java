package design.aeonic.nifty.impl.networking.packet;

import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.networking.packet.PacketHandler;
import design.aeonic.nifty.api.networking.packet.SimpleChannel;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class SimpleChannelImpl implements SimpleChannel {
    protected final ResourceLocation id;
    protected final Supplier<String> protocolVersion;
    protected final Map<String, PacketHandlerImpl<?>> packetHandlers = new HashMap<>();

    public SimpleChannelImpl(ResourceLocation id, Supplier<String> protocolVersion) {
        this.id = id;
        this.protocolVersion = protocolVersion;
    }

    protected abstract <T> void sendToServer(String id, PacketHandler<T> handler, Consumer<ExtraFriendlyByteBuf> writer);

    protected abstract <T> void sendToClients(String id, PacketHandler<T> handler, Consumer<ExtraFriendlyByteBuf> writer, ServerPlayer... players);

    @Override
    public String getProtocolVersion() {
        return protocolVersion.get();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T> void sendToServer(String id, T packet) {
        PacketHandler<T> handler = (PacketHandler<T>) packetHandlers.get(id);
        if (handler == null) throw new IllegalArgumentException("No packet handler registered for id " + id);

        sendToServer(id, handler, buffer -> {
            writeProtocolVersion(buffer);
            buffer.writeUtf(id);
            handler.serialize(packet, buffer);
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <T> void sendToClients(String id, T packet, ServerPlayer... players) {
        PacketHandler<T> handler = (PacketHandler<T>) packetHandlers.get(id);
        if (handler == null) throw new IllegalArgumentException("No packet handler registered for id " + id);

        sendToClients(id, handler, buffer -> {
            writeProtocolVersion(buffer);
            buffer.writeUtf(id);
            handler.serialize(packet, buffer);
        }, players);
    }

    @Override
    public <T> PacketHandler<T> registerPacket(String id, Class<T> packetClass, BiConsumer<T, ExtraFriendlyByteBuf> serializer, Function<ExtraFriendlyByteBuf, T> deserializer) {
        var ret = new PacketHandlerImpl<>(this, packetClass, serializer, deserializer);
        packetHandlers.put(id, ret);
        return ret;
    }

    public void writeProtocolVersion(FriendlyByteBuf buf) {
        buf.writeUtf(getProtocolVersion());
    }

    public void checkProtocolVersion(FriendlyByteBuf buf) {
        String version = buf.readUtf();
        assert (version.equals(getProtocolVersion())) : "Protocol version mismatch! For channel " + id + "\nExpected: " + getProtocolVersion() + ", got: " + version;
    }
}
