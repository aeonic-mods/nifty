package design.aeonic.nifty.api.networking.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.ContainerData;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A wrapper for {@link FriendlyByteBuf} that allows for the reading and writing of extra data types and that provides
 * some extra utility methods.
 */
public class ExtraFriendlyByteBuf extends FriendlyByteBuf {
    private ExtraFriendlyByteBuf(ByteBuf buf) {
        super(buf);
    }

    public static ExtraFriendlyByteBuf create() {
        return of(Unpooled.buffer());
    }

    public static ExtraFriendlyByteBuf of(ByteBuf buf) {
        return new ExtraFriendlyByteBuf(buf);
    }

    public static <T> BiConsumer<T, FriendlyByteBuf> encoder(BiConsumer<T, ExtraFriendlyByteBuf> encoder) {
        return (t, buf) -> encoder.accept(t, of(buf));
    }

    public static <T> Function<FriendlyByteBuf, T> decoder(Function<ExtraFriendlyByteBuf, T> decoder) {
        return buf -> decoder.apply(of(buf));
    }

    public void writeContainerData(ContainerData data) {
        writeInt(data.getCount());
        for (int i = 0; i < data.getCount(); i++) {
            writeInt(data.get(i));
        }
    }

    public void readContainerData(ContainerData data) {
        int count = readInt();
        for (int i = 0; i < count; i++) {
            data.set(i, readInt());
        }
    }
}
