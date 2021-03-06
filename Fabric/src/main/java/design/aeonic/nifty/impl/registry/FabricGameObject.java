package design.aeonic.nifty.impl.registry;

import design.aeonic.nifty.api.registry.GameObject;
import net.minecraft.resources.ResourceLocation;

public record FabricGameObject<T>(ResourceLocation key, T object) implements GameObject<T> {
    // On Fabric, there are no registry events - objects are registered immediately

    @Override
    public boolean isRegistered() {
        return true;
    }

    @Override
    public T get() {
        return object;
    }

    @Override
    public T getOrNull() {
        return object;
    }

}
