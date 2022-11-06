package design.aeonic.nifty.api.services;

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

import java.util.function.Supplier;

public interface PlatformAccess {
    <T extends BlockEntity> BlockEntityType<T> blockEntityType(BlockEntitySupplier<T> supplier, Block... validBlocks);

    <T extends AbstractContainerMenu> MenuType<T> menuType(MenuSupplier<T> menuSupplier);

    <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void registerScreen(MenuType<M> menuType, ScreenSupplier<M, S> screenSupplier);

    void setRenderLayer(Block block, RenderType renderType);

    CreativeModeTab registerCreativeTab(ResourceLocation id, Supplier<ItemStack> icon);

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
