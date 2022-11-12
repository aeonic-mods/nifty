package design.aeonic.nifty.api.networking;

import design.aeonic.nifty.api.core.Services;
import design.aeonic.nifty.api.networking.packet.SimpleChannel;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface Networking {
    Networking INSTANCE = Services.NETWORKING;

    /**
     * Get or create a simple channel for the given ID with the given protocol version. The returned channel is used
     * to register, send and handle packets for both logical sides. If a channel already exists with the given ID, but
     * its protocol version *does not* match the given one, an exception will be thrown.
     * @param id the channel ID
     * @param protocolVersion the protocol version
     * @return the newly created (or, if one exists, the previously created) channel
     */
    SimpleChannel getChannel(ResourceLocation id, Supplier<String> protocolVersion);
}
