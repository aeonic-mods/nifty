package design.aeonic.nifty.api.networking.packet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

/**
 * A serverside listener for packets sent from a client.
 */
public interface ServerPacketHandler<T> {
    void handle(Consumer<Runnable> taskQueue, MinecraftServer server, ServerPlayer player, T packet);
}
