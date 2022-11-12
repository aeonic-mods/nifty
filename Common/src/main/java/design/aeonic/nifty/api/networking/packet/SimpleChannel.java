package design.aeonic.nifty.api.networking.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
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
     * Sends a packet to the server. If the packet has not yet been registered within this channel, throws an exception.
     * @param id the packet's id
     * @param packet the packet to send
     */
    <T> void sendToServer(String id, T packet);

    /**
     * Sends a packet to the specified clients. If the packet has not yet been registered within this channel, throws an exception.
     * @param id the packet's id
     * @param packet the packet to send
     * @param players the clients to send the packet to
     */
    <T> void sendToClients(String id, T packet, ServerPlayer... players);

    /**
     * Registers a server-side packet handler for the given packet type.
     * @param id the packet's ID, unique within the channel
     * @param packetClass the packet's class
     * @param encoder the packet encoder
     * @param decoder the packet decoder
     * @param handler the handler
     */
    <T> void registerServerPacketListener(String id, Class<T> packetClass, BiConsumer<FriendlyByteBuf, T> encoder, Function<FriendlyByteBuf, T> decoder, ServerPacketHandler<T> handler);

    /**
     * Registers a client-side packet handler for the given packet type.
     * @param id the packet's ID, unique within the channel
     * @param packetClass the packet's class
     * @param encoder the packet encoder
     * @param decoder the packet decoder
     * @param handler the handler
     */
    <T> void registerClientPacketListener(String id, Class<T> packetClass, BiConsumer<FriendlyByteBuf, T> encoder, Function<FriendlyByteBuf, T> decoder, ClientPacketHandler<T> handler);

}
