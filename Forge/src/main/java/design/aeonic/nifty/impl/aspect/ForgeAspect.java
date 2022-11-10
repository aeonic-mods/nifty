package design.aeonic.nifty.impl.aspect;

import design.aeonic.nifty.api.aspect.Aspect;
import net.minecraftforge.common.util.LazyOptional;

import java.util.function.Supplier;

public class ForgeAspect<T> extends Aspect<T> {
    public ForgeAspect(LazyOptional<T> optional) {
        super(() -> optional.resolve().orElse(null));
    }
}
