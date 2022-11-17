package design.aeonic.nifty.api.networking.packet;

import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.ContainerData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
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

    public <T> void writeTag(TagKey<T> tag) {
        writeResourceLocation(tag.location());
    }

    public <T> TagKey<T> readTag(ResourceKey<? extends Registry<T>> registry) {
        return TagKey.create(registry, readResourceLocation());
    }

    public <T> void writeList(Collection<T> list, Consumer<T> encoder) {
        writeList(list, ($, t) -> encoder.accept(t));
    }

    public <T> void writeList(Collection<T> list, BiConsumer<ExtraFriendlyByteBuf, T> encoder) {
        writeVarInt(list.size());
        list.forEach(t -> encoder.accept(this, t));
    }

    public <T> List<T> readList(Function<ExtraFriendlyByteBuf, T> decoder) {
        int size = readVarInt();
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) list.add(decoder.apply(this));
        return list;
    }

    public void writeFluidStack(FluidStack stack) {
        if (stack.isEmpty()) writeBoolean(true);
        else {
            writeBoolean(false);
            writeId(Registry.FLUID, stack.getFluid());
            writeLong(stack.getAmount());
            writeNbt(stack.getTag());
        }
    }

    public FluidStack readFluidStack() {
        if (readBoolean()) return FluidStack.EMPTY_STACK;
        return FluidStack.of(readById(Registry.FLUID), readLong(), readNbt());
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
