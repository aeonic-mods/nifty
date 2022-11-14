package design.aeonic.nifty.api.networking.packet;

import net.minecraft.client.Minecraft;

import java.util.function.Consumer;

/**
 * A clientside listener for packets sent from the server.
 */
public interface ClientPacketCallback<T> {
    /**
     * Handles a packet sent from the server. The passed task queue can be used to execute code on the main client thread.
     * @param taskQueue a queue of tasks to execute on the main client thread
     * @param minecraft the client instancce
     * @param packet the packet
     */
    void handle(Consumer<Runnable> taskQueue, Minecraft minecraft, T packet);
}
