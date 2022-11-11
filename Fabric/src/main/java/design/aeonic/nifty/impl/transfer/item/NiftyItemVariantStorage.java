package design.aeonic.nifty.impl.transfer.item;

import design.aeonic.nifty.api.transfer.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import java.util.Iterator;

@SuppressWarnings("UnstableApiUsage")
public class NiftyItemVariantStorage implements Storage<ItemVariant> {
    private final ItemStorage itemStorage;

    public NiftyItemVariantStorage(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        int intAmount = (int) Math.min(maxAmount, Integer.MAX_VALUE);
        long inserted = intAmount - itemStorage.insert(resource.toStack(intAmount), true).getCount();
        if (inserted > 0) transaction.addCloseCallback((ctx, result) -> {
            if (result.wasCommitted()) itemStorage.insert(resource.toStack((int) inserted), false);
        });
        return inserted;
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        int intAmount = (int) Math.min(maxAmount, Integer.MAX_VALUE);
        long extracted = itemStorage.extract(resource::matches, intAmount, true).getCount();
        if (extracted > 0) transaction.addCloseCallback((ctx, result) -> {
            if (result.wasCommitted()) itemStorage.extract(resource::matches, (int) extracted, false);
        });
        return extracted;
    }

    @Override
    public Iterator<StorageView<ItemVariant>> iterator() {
        return new ViewIterator();
    }

    private class View implements StorageView<ItemVariant> {
        private int slot;

        View(int slot) {
            this.slot = slot;
        }

        View next() {
            this.slot++;
            return this;
        }

        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            return NiftyItemVariantStorage.this.extract(resource, maxAmount, transaction);
        }

        @Override
        public boolean isResourceBlank() {
            return itemStorage.getStackInSlot(slot).isEmpty();
        }

        @Override
        public ItemVariant getResource() {
            return ItemVariant.of(itemStorage.getStackInSlot(slot));
        }

        @Override
        public long getAmount() {
            return itemStorage.getStackInSlot(slot).getCount();
        }

        @Override
        public long getCapacity() {
            return itemStorage.getSlotCapacity(slot);
        }
    }

    private class ViewIterator implements Iterator<StorageView<ItemVariant>> {
        private View view = null;

        @Override
        public boolean hasNext() {
            return view == null || view.slot < itemStorage.getSlots() - 1;
        }

        @Override
        public StorageView<ItemVariant> next() {
            return view == null ? view = new View(0) : view.next();
        }
    }
}
