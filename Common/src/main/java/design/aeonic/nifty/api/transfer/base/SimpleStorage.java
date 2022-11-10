package design.aeonic.nifty.api.transfer.base;

import design.aeonic.nifty.api.transfer.Storage;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;

@MethodsReturnNonnullByDefault
public abstract class SimpleStorage<T> implements Storage<T> {
    private NonNullList<T> contents;

    public SimpleStorage(int size) {
        resize(size);
    }

    public void set(int slot, T stack) {
        contents.set(slot, stack);
    }

    protected abstract T getEmptyStack();

    @Override
    public boolean isEverValid(int slot, T stack) {
        return true;
    }

    @Override
    public T getStackInSlot(int slot) {
        return contents.get(slot);
    }

    @Override
    public int getSlots() {
        return contents.size();
    }

    @SuppressWarnings("unchecked")
    public void resize(int size) {
        T[] temp = (T[]) contents.toArray();
        contents = NonNullList.withSize(size, getEmptyStack());
        for (int i = 0; i < Math.min(temp.length, size); i++) {
            contents.set(i, temp[i]);
        }
    }
}
