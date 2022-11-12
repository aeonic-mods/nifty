package design.aeonic.nifty.api.networking.packet;

import net.minecraft.client.Minecraft;

import java.util.function.Consumer;

/**
 * A clientside listener for packets sent from the server.
 */
public interface ClientPacketHandler<T> {
    void handle(Consumer<Runnable> taskQueue, Minecraft minecraft, T packet);
}
