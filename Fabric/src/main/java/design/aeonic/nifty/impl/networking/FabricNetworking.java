package design.aeonic.nifty.impl.networking;

import design.aeonic.nifty.api.networking.Networking;
import design.aeonic.nifty.api.networking.packet.SimpleChannel;
import design.aeonic.nifty.impl.networking.packet.FabricSimpleChannel;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

public class FabricNetworking implements Networking {
    private final ConcurrentMap<ResourceLocation, FabricSimpleChannel> channels = new ConcurrentHashMap<>();

    @Override
    public SimpleChannel getChannel(ResourceLocation id, Supplier<String> protocolVersion) {
        return channels.computeIfAbsent(id, $ -> new FabricSimpleChannel(id, protocolVersion));
    }
}
