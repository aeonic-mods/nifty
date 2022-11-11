package design.aeonic.nifty.impl.mixin;

import design.aeonic.nifty.api.aspect.AspectProviderEntity;
import design.aeonic.nifty.api.services.Aspects;
import design.aeonic.nifty.impl.services.ForgeAspects;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(Entity.class)
public class ForgeEntityMixin extends CapabilityProvider<Entity> {
    protected ForgeEntityMixin(Class<Entity> baseClass) {
        super(baseClass);
    }

    @Override
    public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (this instanceof AspectProviderEntity self) {
            LazyOptional<T> opt = ((ForgeAspects) Aspects.INSTANCE).getAsCap(self, cap, null);
            if (opt != null) return opt;
        }
        return super.getCapability(cap);
    }
}
