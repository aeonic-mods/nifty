package design.aeonic.nifty.api.networking.packet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

/**
 * A serverside listener for packets sent from a client.
 */
public interface ServerPacketCallback<T> {
    /**
     * Handles a packet sent from a client. The passed task queue can be used to execute code on the main server thread.
     * @param taskQueue a queue of tasks to execute on the main server thread
     * @param server the server instance
     * @param player the player who sent the packet
     * @param packet the packet
     */
    void handle(Consumer<Runnable> taskQueue, MinecraftServer server, ServerPlayer player, T packet);
}
