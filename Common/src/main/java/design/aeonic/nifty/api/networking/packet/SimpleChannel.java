package design.aeonic.nifty.api.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A channel, obtained from {@link design.aeonic.nifty.api.networking.Networking}, that is used to register, send and
 * handle packets for a given mod.
 */
public interface SimpleChannel {
    /**
     * Gets the channel's protocol version. This is already used internally when packets are sent back and forth to
     * check for mismatches.
     */
    String getProtocolVersion();

    /**
     * Sends a packet to the server. If the packet has not yet been registered within this channel,
     * or if it is registered for the wrong logical side, throws an exception.
     * @param id the packet's id
     * @param packet the packet to send
     */
    <T> void sendToServer(String id, T packet);

    /**
     * Sends a packet to the specified clients. If the packet has not yet been registered within this channel,
     * or if it is registered for the wrong logical side, throws an exception.
     * @param id the packet's id
     * @param packet the packet to send
     * @param players the clients to send the packet to
     */
    <T> void sendToClients(String id, T packet, ServerPlayer... players);

    /**
     * Creates and registers a packet handler, returning the handler object for you to register server- and client-side
     * packet receive callbacks.
     * @param id the packet's id, unique to this channel
     * @param packetClass the packet's class
     * @param serializer a function that serializes the packet to a {@link FriendlyByteBuf}
     * @param deserializer a function that deserializes the packet from a {@link FriendlyByteBuf}
     */
    <T> PacketHandler<T> registerPacket(String id, Class<T> packetClass, BiConsumer<T, ExtraFriendlyByteBuf> serializer, Function<ExtraFriendlyByteBuf, T> deserializer);

}
