package design.aeonic.nifty.api.platform;

import design.aeonic.nifty.api.client.FluidRenderInfo;
import design.aeonic.nifty.api.core.Services;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public interface PlatformAccess {
    PlatformAccess INSTANCE = Services.ACCESS;

    <T extends BlockEntity> BlockEntityType<T> blockEntityType(BlockEntitySupplier<T> supplier, Block... validBlocks);

    <T extends AbstractContainerMenu> MenuType<T> menuType(MenuSupplier<T> menuSupplier);

    <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void registerScreen(MenuType<M> menuType, ScreenSupplier<M, S> screenSupplier);

    void setRenderLayer(Block block, RenderType renderType);

    CreativeModeTab registerCreativeTab(ResourceLocation id, Supplier<ItemStack> icon);

    /**
     * Gets the burn time of a given item stack, or 0 if it is not a fuel.
     */
    int getBurnTime(ItemStack stack);

    /**
     * Gets the fluid render info for a given fluid. Client only!
     */
    FluidRenderInfo getFluidRenderInfo(Fluid fluid);

    @FunctionalInterface
    interface MenuSupplier<T extends AbstractContainerMenu> {
        T create(int syncId, Inventory playerInvenory);
    }

    @FunctionalInterface
    interface BlockEntitySupplier<T extends BlockEntity> {
        T create(BlockPos pos, BlockState state);
    }

    interface ScreenSupplier<M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> {
        S create(M menu, Inventory playerInventory, Component title);
    }
}
