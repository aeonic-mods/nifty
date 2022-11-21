package design.aeonic.nifty.api.util;

import net.minecraft.util.Mth;

import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface Progress extends Supplier<Float> {
    static Progress of(Supplier<Float> progress) {
        return () -> Mth.clamp(progress.get(), 0, 1);
    }

    static Progress of(Supplier<Integer> value, int max) {
        return of(value, 0, max);
    }

    static Progress of(Supplier<Integer> value, int min, int max) {
        return () -> (value.get() - min) / ((float) (max - min));
    }

    static Progress of(Supplier<Integer> value, Supplier<Integer> max) {
        return () -> max.get() == 0 ? 0 : value.get() / (float) max.get();
    }

    default Progress oneMinus() {
        return () -> 1 - get();
    }

    default Progress map(Function<Float, Float> processor) {
        return () -> processor.apply(get());
    }
}
