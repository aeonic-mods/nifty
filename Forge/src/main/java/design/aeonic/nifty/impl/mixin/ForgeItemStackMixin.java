package design.aeonic.nifty.impl.mixin;

import design.aeonic.nifty.api.aspect.AspectProviderItem;
import design.aeonic.nifty.api.services.Aspects;
import design.aeonic.nifty.impl.services.ForgeAspects;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public abstract class ForgeItemStackMixin extends CapabilityProvider<ItemStack> {
    @Shadow public abstract Item getItem();

    protected ForgeItemStackMixin(Class<ItemStack> baseClass) {
        super(baseClass);
    }

    @Override
    public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (getItem() instanceof AspectProviderItem item) {
            LazyOptional<T> opt = ((ForgeAspects) Aspects.INSTANCE).getAsCap(item, cap, null);
            if (opt != null) return opt;
        }
        return super.getCapability(cap);
    }
}
