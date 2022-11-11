package design.aeonic.nifty.impl.transfer.item;

import com.google.common.collect.Iterators;
import design.aeonic.nifty.api.transfer.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.impl.transfer.item.ItemVariantCache;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nonnull;

import java.util.function.Predicate;

@SuppressWarnings("UnstableApiUsage")
public class FabricItemStorage implements ItemStorage {
    private final Storage<ItemVariant> itemVariantStorage;

    public FabricItemStorage(Storage<ItemVariant> itemVariantStorage) {
        this.itemVariantStorage = itemVariantStorage;
    }

    @Override
    public int getSlots() {
        return Iterators.size(itemVariantStorage.iterator());
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        try (Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe())) {
            for (StorageView<ItemVariant> view : itemVariantStorage) {
                if (slot == 0) {
                    return view.getResource().toStack((int) Math.min(view.getAmount(), Integer.MAX_VALUE));
                }
                slot--;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public long getSlotCapacity(int slot) {
        try (Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe())) {
            for (StorageView<ItemVariant> view : itemVariantStorage) {
                if (slot == 0) {
                    return view.getCapacity();
                }
                slot--;
            }
        }
        return 0;
    }

    @Override
    public boolean isEverValid(int slot, ItemStack stack) {
        // TODO: Probably a better way to do this, but whatever. We just skip this check; the performance hit should be less than what would result from iterating through storage views.
        return true;
    }

    @Override
    public ItemStack insert(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return insert(stack, simulate);
    }

    @Override
    public ItemStack insert(@Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty() || !itemVariantStorage.supportsInsertion()) return stack;
        try (Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe())) {
            long amountInserted = itemVariantStorage.insert(ItemVariant.of(stack), stack.getCount(), transaction);
            if (amountInserted == 0) return stack;

            ItemStack result = stack.copy();
            result.shrink((int) amountInserted);
            transaction.commit();
            return result;
        }
    }

    @Override
    public ItemStack extract(Predicate<ItemStack> filter, long amount, boolean simulate) {
        if (!itemVariantStorage.supportsExtraction()) return ItemStack.EMPTY;
        try (Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe())) {
            for (StorageView<ItemVariant> view : itemVariantStorage) {
                ItemVariant resource = view.getResource();
                if (filter.test(resource.toStack())) {
                    if (resource.isBlank()) return ItemStack.EMPTY;

                    long amountExtracted = itemVariantStorage.extract(resource, amount, transaction);
                    if (amountExtracted == 0) return ItemStack.EMPTY;

                    transaction.commit();
                    return resource.toStack((int) amountExtracted);
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extract(int slot, long amount, boolean simulate) {
        if (!itemVariantStorage.supportsExtraction()) return ItemStack.EMPTY;
        try (Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe())) {
            for (StorageView<ItemVariant> view : itemVariantStorage) {
                ItemVariant resource = view.getResource();
                if (slot == 0) {
                    if (resource.isBlank()) return ItemStack.EMPTY;

                    long amountExtracted = itemVariantStorage.extract(resource, amount, transaction);
                    if (amountExtracted == 0) return ItemStack.EMPTY;

                    transaction.commit();
                    return resource.toStack((int) amountExtracted);
                }
                slot--;
            }
        }
        return ItemStack.EMPTY;
    }
}
