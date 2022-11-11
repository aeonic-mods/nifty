package design.aeonic.nifty.impl.mixin;

import design.aeonic.nifty.api.aspect.Aspect;
import design.aeonic.nifty.api.services.Aspects;
import design.aeonic.nifty.api.transfer.Transfer;
import design.aeonic.nifty.api.transfer.item.ItemStorage;
import design.aeonic.nifty.api.transfer.item.SimpleItemStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConduitBlockEntity.class)
public class TestConduitMixin extends BlockEntity {
    public TestConduitMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(method = "serverTick", at = @At("HEAD"))
    private static void injectServerTick(Level level, BlockPos pos, BlockState state, ConduitBlockEntity be, CallbackInfo ci) {
        Aspect<ItemStorage> up = Aspects.INSTANCE.getAspect(Transfer.ITEM_ASPECT, level, pos.above(), Direction.DOWN);
        Aspect<ItemStorage> down = Aspects.INSTANCE.getAspect(Transfer.ITEM_ASPECT, level, pos.below(), Direction.UP);

        up.ifPresent(upStorage -> down.ifPresent(downStorage -> {
            ItemStack extracted = upStorage.extract(0, 1, true);
            if (!extracted.isEmpty() && downStorage.insert(extracted, true).getCount() < extracted.getCount()) {
                downStorage.insert(upStorage.extract(0, 1, false), false);
            }
        }));
    }
}
