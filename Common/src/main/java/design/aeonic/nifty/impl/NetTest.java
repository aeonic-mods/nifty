package design.aeonic.nifty.impl;

import design.aeonic.nifty.api.core.Services;
import design.aeonic.nifty.api.networking.packet.SimpleChannel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class NetTest {
    public static final SimpleChannel CHANNEL = Services.NETWORKING.getChannel(new ResourceLocation("nifty", "test"), () -> "v1.0");

    public static void register() {
        CHANNEL.registerServerPacketListener("chest_message", ChestMessage.class, (buf, msg) -> msg.encode(buf), ChestMessage::new, (taskQueue, server, player, packet) -> {
            player.sendSystemMessage(Component.literal("Received chest message on server: " + packet.message));
        });
    }

    public static class ChestMessage {
        public final String message;

        public ChestMessage(FriendlyByteBuf buf) {
            this(buf.readUtf());
        }

        public ChestMessage(String message) {
            this.message = message;
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeUtf(message);
        }
    }
}
