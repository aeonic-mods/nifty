package design.aeonic.nifty.impl.mixin;

import design.aeonic.nifty.api.aspect.Aspect;
import design.aeonic.nifty.api.aspect.AspectProviderBlockEntity;
import design.aeonic.nifty.api.aspect.AspectType;
import design.aeonic.nifty.api.transfer.Transfer;
import design.aeonic.nifty.api.transfer.item.ItemStorage;
import design.aeonic.nifty.api.transfer.item.SimpleItemStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SignBlockEntity.class)
public class TestSignMixin extends BlockEntity implements AspectProviderBlockEntity {
    private final ItemStorage itemStorage = new SimpleItemStorage(1);

    public TestSignMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    @Override
    public <A> Aspect<A> getAspect(AspectType<A> type, Direction context) {
        if (type.is(Transfer.ITEM_ASPECT)) return Aspect.of(() -> itemStorage).cast();
        return Aspect.empty();
    }
}
