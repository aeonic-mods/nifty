package design.aeonic.nifty.api.util;

import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public interface Registrar<T> extends BiConsumer<String, T> {
    static <T> Registrar<T> of(String modId, BiConsumer<ResourceLocation, T> consumer) {
        return (name, value) -> consumer.accept(new ResourceLocation(modId, name), value);
    }
}
