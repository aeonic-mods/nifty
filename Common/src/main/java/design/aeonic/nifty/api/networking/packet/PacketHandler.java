package design.aeonic.nifty.api.networking.packet;

import design.aeonic.nifty.api.core.Services;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Supplier;

/**
 * A packet handler for sending and receiving packets of a given type. Obtained and registered through a
 * {@link SimpleChannel} instance. Must be registered on both logical sides for packet serialization, but the
 * handler callbacks don't need to exist on the side the packet is being sent from (and probably shouldn't).<br><br>
 * Only one packet handler can exist per class, due to the way Forge registers packets under SimpleImpl.
 */
public interface PacketHandler<T> {
    /**
     * Gets the channel this packet handler is registered under.
     */
    SimpleChannel getChannel();

    /**
     * The packet class that this handler is registered for.
     */
    Class<T> getPacketClass();

    /**
     * Serializes the packet to a {@link ExtraFriendlyByteBuf}.
     */
    void serialize(T packet, ExtraFriendlyByteBuf buffer);

    /**
     * Deserializes the packet from a {@link ExtraFriendlyByteBuf}.
     */
    T deserialize(ExtraFriendlyByteBuf buffer);

    /**
     * Registers a callback to execute when this packet is received on the server. Can be called on either physical
     * side; just won't do anything on the client. If a callback is already registered, it will be replaced.<br><br>
     * The callback will be executed on the networking thread; use the passed runnable task queue to execute code on
     * the main server thread.
     * If a client callback is already registered, an error will be thrown.
     */
    PacketHandler<T> onServerReceived(ServerPacketCallback<T> callback);

    /**
     * If run on the physical client, adds a client-side callback as described in {@link #onClientReceived(ClientPacketCallback)}.
     * Otherwise, does nothing. This method should avoid unsafe classloading on the server so it can be called on both
     * sides.
     */
    default PacketHandler<T> onClientReceivedSafe(Supplier<ClientPacketCallback<T>> callback) {
        Services.PLATFORM.getPhysicalSide().ifClient(() -> onClientReceived(callback.get()));
        return this;
    }

    /**
     * Registers a callback to execute when this packet is received on the client. Will error if called on the physical
     * server - use {@link #onClientReceivedSafe(Supplier)}. If a callback is already registered, it will be replaced.<br><br>
     * The callback will be executed on the networking thread; use the passed runnable task queue to execute code on
     * the main client thread.<br><br>
     * If a server callback is already registered, an error will be thrown.
     */
    PacketHandler<T> onClientReceived(ClientPacketCallback<T> callback);

    /**
     * A convenience method that defers to {@link SimpleChannel#sendToServer(String, Object)}
     * @param id the packet's id
     * @param packet the packet to send
     */
    default void sendToServer(String id, T packet) {
        getChannel().sendToServer(id, packet);
    }

    /**
     * A convenience method that defers to {@link SimpleChannel#sendToClients(String, Object, ServerPlayer...)}
     * @param id the packet's id
     * @param packet the packet to send
     * @param players the clients to send the packet to
     */
    default void sendToClients(String id, T packet, ServerPlayer... players) {
        getChannel().sendToClients(id, packet, players);
    }
}
