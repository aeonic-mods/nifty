package design.aeonic.nifty.impl.mixin;

import design.aeonic.nifty.api.aspect.AspectProviderBlockEntity;
import design.aeonic.nifty.api.aspect.Aspects;
import design.aeonic.nifty.impl.aspect.ForgeAspects;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(BlockEntity.class)
public class ForgeBlockEntityMixin extends CapabilityProvider<BlockEntity> {
    protected ForgeBlockEntityMixin(Class<BlockEntity> baseClass) {
        super(baseClass);
    }

    @Override
    public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (this instanceof AspectProviderBlockEntity self) {
            LazyOptional<T> opt = ((ForgeAspects) Aspects.INSTANCE).getAsCap(self, cap, side);
            if (opt != null) return opt;
        }
        return super.getCapability(cap);
    }
}
