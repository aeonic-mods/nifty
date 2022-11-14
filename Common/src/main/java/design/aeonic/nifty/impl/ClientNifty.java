package design.aeonic.nifty.impl;

import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class ClientNifty {
    public static void clientInit(Consumer<Runnable> taskQueue) {

        NetTest.CHANNEL.registerClientPacketListener("chest_message", NetTest.ChestMessage.class, (buf, msg) -> msg.encode(buf), NetTest.ChestMessage::new, ($, client, packet) -> {
            client.player.displayClientMessage(Component.literal("Received chest message on client: " + packet.message), false);
        });
    }
}