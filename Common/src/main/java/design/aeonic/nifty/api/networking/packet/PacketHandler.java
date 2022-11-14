package design.aeonic.nifty.api.networking.packet;

import net.minecraft.server.level.ServerPlayer;

/**
 * A packet handler that can be used to handle a specific type o packet, obtained and registered through a {@link SimpleChannel}
 * instance.
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
     */
    PacketHandler<T> onServerReceived(ServerPacketCallback<T> callback);

    /**
     * Registers a callback to execute when this packet is received on the client. Can be called on either physical
     * side; just won't do anything on the server. If a callback is already registered, it will be replaced.<br><br>
     * The callback will be executed on the networking thread; use the passed runnable task queue to execute code on
     * the main client thread.
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
