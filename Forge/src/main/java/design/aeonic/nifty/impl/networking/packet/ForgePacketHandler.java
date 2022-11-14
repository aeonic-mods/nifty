package design.aeonic.nifty.impl.networking.packet;

import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.networking.packet.SimpleChannel;
import net.minecraftforge.network.NetworkRegistry;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ForgePacketHandler<T> extends PacketHandlerImpl<ForgePacketContext<T>> {
    public ForgePacketHandler(SimpleChannel channel, Class<ForgePacketContext<T>> packetClass, BiConsumer<ForgePacketContext<T>, ExtraFriendlyByteBuf> serializer, Function<ExtraFriendlyByteBuf, ForgePacketContext<T>> deserializer) {
        super(channel, packetClass, serializer, deserializer);
        NetworkRegistry.newEventChannel()
    }
}
