package design.aeonic.nifty.impl.transfer.fluid;

import com.google.common.collect.Iterators;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import design.aeonic.nifty.api.transfer.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public class FabricFluidStorage implements FluidStorage {
    private final Storage<FluidVariant> fluidVariantStorage;

    public FabricFluidStorage(Storage<FluidVariant> fluidVariantStorage) {
        this.fluidVariantStorage = fluidVariantStorage;
    }

    @Override
    public int getSlots() {
        return Iterators.size(fluidVariantStorage.iterator());
    }

    @Override
    public FluidStack getStackInSlot(int slot) {
        try (Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe())) {
            for (StorageView<FluidVariant> view : fluidVariantStorage) {
                if (slot == 0) {
                    return variantToStack(view.getResource(), view.getAmount());
                }
                slot--;
            }
        }
        return FluidStack.EMPTY_STACK;
    }

    @Override
    public long getSlotCapacity(int slot) {
        try (Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe())) {
            for (StorageView<FluidVariant> view : fluidVariantStorage) {
                if (slot == 0) {
                    return view.getCapacity();
                }
                slot--;
            }
        }
        return 0;
    }

    @Override
    public boolean isEverValid(int slot, FluidStack stack) {
        // TODO: Probably a better way to do this, but whatever. We just skip this check; the performance
        // hit should be less than what would result from iterating through storage views.
        return true;
    }

    @Override
    public FluidStack insert(int slot, @Nonnull FluidStack stack, boolean simulate) {
        return insert(stack, simulate);
    }

    @Override
    public FluidStack insert(@Nonnull FluidStack stack, boolean simulate) {
        if (stack.isEmpty() || !fluidVariantStorage.supportsInsertion()) return stack;
        try (Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe())) {
            long amountInserted =  fluidVariantStorage.insert(stackToVariant(stack), stack.getAmount(), transaction);
            if (amountInserted == 0) return stack;

            FluidStack result = stack.copy();
            result.shrink((int) amountInserted);

            transaction.commit();
            return result;
        }
    }

    @Override
    public FluidStack extract(Predicate<FluidStack> filter, long amount, boolean simulate) {
        if (!fluidVariantStorage.supportsExtraction()) return FluidStack.EMPTY_STACK;
        try (Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe())) {
            for (StorageView<FluidVariant> view : fluidVariantStorage) {
                FluidVariant resource = view.getResource();
                if (filter.test(variantToStack(resource, 1))) {
                    if (resource.isBlank()) return FluidStack.EMPTY_STACK;

                    long amountExtracted = fluidVariantStorage.extract(resource, amount, transaction);
                    if (amountExtracted == 0) return FluidStack.EMPTY_STACK;

                    transaction.commit();
                    return variantToStack(resource, amountExtracted);
                }
            }
        }
        return FluidStack.EMPTY_STACK;
    }

    @Override
    public FluidStack extract(int slot, long amount, boolean simulate) {
        if (!fluidVariantStorage.supportsExtraction()) return FluidStack.EMPTY_STACK;
        try (Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe())) {
            for (StorageView<FluidVariant> view : fluidVariantStorage) {
                FluidVariant resource = view.getResource();
                if (slot == 0) {
                    if (resource.isBlank()) return FluidStack.EMPTY_STACK;

                    long amountExtracted = fluidVariantStorage.extract(resource, amount, transaction);
                    if (amountExtracted == 0) return FluidStack.EMPTY_STACK;

                    transaction.commit();
                    return variantToStack(resource, amountExtracted);
                }
                slot--;
            }
        }
        return FluidStack.EMPTY_STACK;
    }

    public static FluidStack variantToStack(FluidVariant variant, long amount) {
        return FluidStack.of(variant.getFluid(), amount, variant.copyNbt());
    }

    public static FluidVariant stackToVariant(FluidStack stack) {
        return FluidVariant.of(stack.getFluid(), stack.getTag());
    }
}
