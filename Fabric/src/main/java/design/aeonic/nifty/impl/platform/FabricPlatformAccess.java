package design.aeonic.nifty.impl.platform;

import design.aeonic.nifty.api.client.FluidRenderInfo;
import design.aeonic.nifty.api.platform.PlatformAccess;
import design.aeonic.nifty.impl.client.FabricFluidRenderInfo;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class FabricPlatformAccess implements PlatformAccess {
    @Override
    public <T extends BlockEntity> BlockEntityType<T> blockEntityType(BlockEntitySupplier<T> supplier, Block... validBlocks) {
        return FabricBlockEntityTypeBuilder.create(supplier::create, validBlocks).build(null);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(MenuSupplier<T> menuSupplier) {
        return new MenuType<>(menuSupplier::create);
    }

    @Override
    public <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void registerScreen(MenuType<M> menuType, ScreenSupplier<M, S> screenSupplier) {
        MenuScreens.register(menuType, screenSupplier::create);
    }

    @Override
    public void setRenderLayer(Block block, RenderType renderType) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, renderType);
    }

    @Override
    public CreativeModeTab registerCreativeTab(ResourceLocation id, Supplier<ItemStack> icon) {
        return FabricItemGroupBuilder.create(id).icon(icon).build();
    }

    @Override
    public int getBurnTime(ItemStack stack) {
        return FuelRegistry.INSTANCE.get(stack.getItem());
    }

    @Override
    public FluidRenderInfo getFluidRenderInfo(Fluid fluid) {
        return FabricFluidRenderInfo.get(fluid);
    }
}
