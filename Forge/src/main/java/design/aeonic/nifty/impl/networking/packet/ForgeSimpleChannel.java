package design.aeonic.nifty.impl.networking.packet;

import design.aeonic.nifty.api.networking.packet.ExtraFriendlyByteBuf;
import design.aeonic.nifty.api.networking.packet.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeSimpleChannel extends SimpleChannelImpl {
    private final SimpleChannel forgeChannel;
    private int counter = -1;

    public ForgeSimpleChannel(ResourceLocation id, Supplier<String> protocolVersion) {
        super(id, protocolVersion);

        forgeChannel = NetworkRegistry.newSimpleChannel(id, protocolVersion, this::protocolVersionMatches, this::protocolVersionMatches);
    }

    boolean protocolVersionMatches(String version) {
        return getProtocolVersion().equals(version);
    }

    @Override
    public <T> PacketHandler<T> registerPacket(String id, Class<T> packetClass, BiConsumer<T, ExtraFriendlyByteBuf> serializer, Function<ExtraFriendlyByteBuf, T> deserializer) {
        PacketHandler<T> ret = new ForgePacketHandler<>(this, )
        forgeChannel.registerMessage(++counter, packetClass,
                ExtraFriendlyByteBuf.encoder(serializer),
                ExtraFriendlyByteBuf.decoder(deserializer), this::receive);
        return ret;
    }

    @SuppressWarnings({"unchecked"})
    <T> void receive(ForgePacketContext<T> packet, Supplier<NetworkEvent.Context> ctx) {
        // Forge checks protocol versions for us
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            PacketHandlerImpl<T> handler = (PacketHandlerImpl<T>) packetHandlers.get(packet.getId());
            if (handler == null) throw new IllegalArgumentException("No packet handler registered for id " + packet.getId());
            handler.getClientCallback().handle(ctx.get()::enqueueWork, Minecraft.getInstance(), packet.getPacket());
        } else {
            PacketHandlerImpl<T> handler = (PacketHandlerImpl<T>) packetHandlers.get(packet.getId());
            if (handler == null) throw new IllegalArgumentException("No packet handler registered for id " + packet.getId());
            handler.getServerCallback().handle(ctx.get()::enqueueWork, Objects.requireNonNull(ctx.get().getSender()).server, ctx.get().getSender(), packet.getPacket());
        }
    }

    @Override
    protected <T> void sendToServer(String id, PacketHandler<T> handler, Consumer<ExtraFriendlyByteBuf> writer) {
//        forgeChannel.sendToServer(new ForgePacketContext<>(id, handler.getPacketClass().cast(writer)));
    }

    @Override
    protected <T> void sendToClients(String id, PacketHandler<T> handler, Consumer<ExtraFriendlyByteBuf> writer, ServerPlayer... players) {

    }
}
