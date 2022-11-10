package design.aeonic.nifty.api.transfer.base;

import design.aeonic.nifty.api.transfer.Battery;
import net.minecraft.nbt.CompoundTag;

import java.math.BigInteger;

public abstract class SimpleBattery<T extends Number & Comparable<T>> implements Battery<T> {
    private T capacity;
    private T stored;

    public SimpleBattery(T capacity) {
        this.capacity = capacity;
        this.stored = zero();
    }

    public SimpleBattery(T capacity, T stored) {
        this.capacity = capacity;
        this.stored = stored;
    }

    public void resize(T capacity) {
        this.capacity = capacity;
    }

    public void setStored(T stored) {
        this.stored = stored;
    }

    public abstract void writeNBT(CompoundTag tag);

    public abstract void readNBT(CompoundTag tag);

    protected abstract T zero();

    protected abstract T add(T a, T b);

    protected abstract T subtract(T a, T b);

    protected T min(T a, T b) {
        return a.compareTo(b) <= 0 ? a : b;
    }

    @Override
    public T getCapacity() {
        return capacity;
    }

    @Override
    public T getStored() {
        return stored;
    }

    @Override
    public T insert(T amount, boolean simulate) {
        T maxInsert = min(amount, subtract(capacity, stored));
        if (!simulate) stored = add(stored, maxInsert);
        return maxInsert;
    }

    @Override
    public T extract(T amount, boolean simulate) {
        T maxExtract = min(amount, stored);
        if (!simulate) stored = subtract(stored, maxExtract);
        return maxExtract;
    }

    public static class IntBattery extends SimpleBattery<Integer> {
        public IntBattery(int capacity) {
            super(capacity);
        }

        public IntBattery(int capacity, int stored) {
            super(capacity, stored);
        }

        @Override
        public void writeNBT(CompoundTag tag) {
            tag.putInt("capacity", getCapacity());
            tag.putInt("stored", getStored());
        }

        @Override
        public void readNBT(CompoundTag tag) {
            resize(tag.getInt("capacity"));
            setStored(tag.getInt("stored"));
        }

        @Override
        protected Integer zero() {
            return 0;
        }

        @Override
        protected Integer add(Integer a, Integer b) {
            return a + b;
        }

        @Override
        protected Integer subtract(Integer a, Integer b) {
            return a - b;
        }
    }

    public static class LongBattery extends SimpleBattery<Long> {
        public LongBattery(long capacity) {
            super(capacity);
        }

        public LongBattery(long capacity, long stored) {
            super(capacity, stored);
        }

        @Override
        public void writeNBT(CompoundTag tag) {
            tag.putLong("capacity", getCapacity());
            tag.putLong("stored", getStored());
        }

        @Override
        public void readNBT(CompoundTag tag) {
            resize(tag.getLong("capacity"));
            setStored(tag.getLong("stored"));
        }

        @Override
        protected Long zero() {
            return 0L;
        }

        @Override
        protected Long add(Long a, Long b) {
            return a + b;
        }

        @Override
        protected Long subtract(Long a, Long b) {
            return a - b;
        }
    }

    public static class BigBattery extends SimpleBattery<BigInteger> {
        public BigBattery(BigInteger capacity) {
            super(capacity);
        }

        public BigBattery(BigInteger capacity, BigInteger stored) {
            super(capacity, stored);
        }

        @Override
        public void writeNBT(CompoundTag tag) {
            tag.putByteArray("capacity", getCapacity().toByteArray());
            tag.putByteArray("stored", getStored().toByteArray());
        }

        @Override
        public void readNBT(CompoundTag tag) {
            resize(new BigInteger(tag.getByteArray("capacity")));
            setStored(new BigInteger(tag.getByteArray("stored")));
        }

        @Override
        protected BigInteger zero() {
            return BigInteger.ZERO;
        }

        @Override
        protected BigInteger add(BigInteger a, BigInteger b) {
            return a.add(b);
        }

        @Override
        protected BigInteger subtract(BigInteger a, BigInteger b) {
            return a.subtract(b);
        }
    }
}
