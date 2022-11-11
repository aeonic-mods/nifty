package design.aeonic.nifty.impl.transfer.fluid;

import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import design.aeonic.nifty.api.transfer.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import java.util.Iterator;
import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public class NiftyFluidVariantStorage implements Storage<FluidVariant> {
    private final FluidStorage fluidStorage;

    public NiftyFluidVariantStorage(FluidStorage fluidStorage) {
        this.fluidStorage = fluidStorage;
    }

    @Override
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        long inserted = fluidStorage.insert(FabricFluidStorage.variantToStack(resource, maxAmount), true).getAmount();
        if (inserted > 0) transaction.addCloseCallback((ctx, result) -> {
            if (result.wasCommitted()) fluidStorage.insert(FabricFluidStorage.variantToStack(resource, maxAmount), false);
        });
        return inserted;
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        Predicate<FluidStack> predicate = stack -> stack.is(resource.getFluid()) && stack.getTag().equals(resource.getNbt());

        long extracted = fluidStorage.extract(predicate, maxAmount, true).getAmount();
        if (extracted > 0) transaction.addCloseCallback((ctx, result) -> {
            if (result.wasCommitted()) fluidStorage.extract(predicate, extracted, false);
        });
        return extracted;
    }

    @Override
    public Iterator<StorageView<FluidVariant>> iterator() {
        return new ViewIterator();
    }

    private class View implements StorageView<FluidVariant> {
        private int slot;

        View(int slot) {
            this.slot = slot;
        }

        View next() {
            this.slot++;
            return this;
        }

        @Override
        public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            return NiftyFluidVariantStorage.this.extract(resource, maxAmount, transaction);
        }

        @Override
        public boolean isResourceBlank() {
            return fluidStorage.getStackInSlot(slot).isEmpty();
        }

        @Override
        public FluidVariant getResource() {
            FluidStack stack = fluidStorage.getStackInSlot(slot);
            return FluidVariant.of(stack.getFluid(), stack.getTag());
        }

        @Override
        public long getAmount() {
            return fluidStorage.getStackInSlot(slot).getAmount();
        }

        @Override
        public long getCapacity() {
            return fluidStorage.getSlotCapacity(slot);
        }
    }

    private class ViewIterator implements Iterator<StorageView<FluidVariant>> {
        private final View view = new View(0);

        @Override
        public boolean hasNext() {
            return view.slot < fluidStorage.getSlots();
        }

        @Override
        public StorageView<FluidVariant> next() {
            return view.next();
        }
    }
}
