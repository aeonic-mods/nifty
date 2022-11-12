package design.aeonic.nifty.impl.networking.packet;

import design.aeonic.nifty.api.networking.packet.ClientPacketHandler;
import design.aeonic.nifty.api.networking.packet.ServerPacketHandler;
import design.aeonic.nifty.api.networking.packet.SimpleChannel;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
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
    protected final Map<String, ServerboundPacketType<?>> serverPackets = new HashMap<>();
    protected final Map<String, ClientboundPacketType<?>> clientPackets = new HashMap<>();

    public SimpleChannelImpl(ResourceLocation id, Supplier<String> protocolVersion) {
        this.id = id;
        this.protocolVersion = protocolVersion;
    }

    @Override
    public String getProtocolVersion() {
        return protocolVersion.get();
    }

    @Override
    public <T> void registerServerPacketListener(String id, Class<T> packetClass, BiConsumer<FriendlyByteBuf, T> encoder, Function<FriendlyByteBuf, T> decoder, ServerPacketHandler<T> handler) {
        serverPackets.put(id, new ServerboundPacketType<>(packetClass, encoder, decoder, handler));
    }

    @Override
    public <T> void registerClientPacketListener(String id, Class<T> packetClass, BiConsumer<FriendlyByteBuf, T> encoder, Function<FriendlyByteBuf, T> decoder, ClientPacketHandler<T> handler) {
        clientPackets.put(id, new ClientboundPacketType<>(packetClass, encoder, decoder, handler));
    }

    public void writeProtocolVersion(FriendlyByteBuf buf) {
        buf.writeUtf(getProtocolVersion());
    }

    public void checkProtocolVersion(FriendlyByteBuf buf) {
        String version = buf.readUtf();
        assert (version.equals(getProtocolVersion())) : "Protocol version mismatch! For channel " + id + "\nExpected: " + getProtocolVersion() + ", got: " + version;
    }

    public static class ServerboundPacketType<T> {
        private final Class<T> packetClass;
        private final BiConsumer<FriendlyByteBuf, T> encoder;
        private final Function<FriendlyByteBuf, T> decoder;
        private final ServerPacketHandler<T> handler;

        public ServerboundPacketType(Class<T> packetClass, BiConsumer<FriendlyByteBuf, T> encoder, Function<FriendlyByteBuf, T> decoder, ServerPacketHandler<T> handler) {
            this.packetClass = packetClass;
            this.encoder = encoder;
            this.decoder = decoder;
            this.handler = handler;
        }

        public void encode(FriendlyByteBuf buf, T packet) {
            encoder.accept(buf, packet);
        }

        public T decode(FriendlyByteBuf buf) {
            return decoder.apply(buf);
        }

        public void handle(Consumer<Runnable> taskQueue, MinecraftServer server, ServerPlayer player, FriendlyByteBuf buf) {
            T packet = decode(buf);
            handler.handle(taskQueue, server, player, packet);
        }
    }

    public static class ClientboundPacketType<T> {
        private final Class<T> packetClass;
        private final BiConsumer<FriendlyByteBuf, T> encoder;
        private final Function<FriendlyByteBuf, T> decoder;
        private final ClientPacketHandler<T> handler;

        public ClientboundPacketType(Class<T> packetClass, BiConsumer<FriendlyByteBuf, T> encoder, Function<FriendlyByteBuf, T> decoder, ClientPacketHandler<T> handler) {
            this.packetClass = packetClass;
            this.encoder = encoder;
            this.decoder = decoder;
            this.handler = handler;
        }

        public void encode(FriendlyByteBuf buf, T packet) {
            encoder.accept(buf, packet);
        }

        public T decode(FriendlyByteBuf buf) {
            return decoder.apply(buf);
        }

        public void handle(Consumer<Runnable> taskQueue, Minecraft minecraft, FriendlyByteBuf buf) {
            T packet = decode(buf);
            handler.handle(taskQueue, minecraft, packet);
        }
    }
}
