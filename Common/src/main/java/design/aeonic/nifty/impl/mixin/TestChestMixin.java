package design.aeonic.nifty.impl.mixin;

import design.aeonic.nifty.impl.NetTest;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public class TestChestMixin {
    @Inject(method = "use", at = @At("HEAD"))
    private void injectUse(BlockState $$0, Level $$1, BlockPos $$2, Player $$3, InteractionHand $$4, BlockHitResult $$5, CallbackInfoReturnable<InteractionResult> cir) {
        if ($$1.isClientSide()) {
            NetTest.CHANNEL.sendToServer("chest_message", new NetTest.ChestMessage("Hello, world!"));
        } else {
            NetTest.CHANNEL.sendToClients("chest_message", new NetTest.ChestMessage("Hello, world!"), (ServerPlayer) $$3);
        }
    }
}
