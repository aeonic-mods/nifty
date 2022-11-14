package design.aeonic.nifty.impl.networking;

import design.aeonic.nifty.api.networking.Networking;
import design.aeonic.nifty.api.networking.packet.SimpleChannel;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ForgeNetworking implements Networking {
    @Override
    public SimpleChannel getChannel(ResourceLocation id, Supplier<String> protocolVersion) {
        return null;
    }
}
