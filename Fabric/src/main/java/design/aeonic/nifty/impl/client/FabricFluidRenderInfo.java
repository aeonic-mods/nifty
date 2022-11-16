package design.aeonic.nifty.impl.client;

import design.aeonic.nifty.api.client.FluidRenderInfo;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import java.util.HashMap;
import java.util.Map;

public class FabricFluidRenderInfo implements FluidRenderInfo {
    static final Map<Fluid, FabricFluidRenderInfo> CACHE = new HashMap<>();

    private final FluidRenderHandler handler;

    private FabricFluidRenderInfo(Fluid fluid) {
        this.handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
    }

    public static FabricFluidRenderInfo get(Fluid fluid) {
        return CACHE.computeIfAbsent(fluid, FabricFluidRenderInfo::new);
    }

    @Override
    public TextureAtlasSprite getStillTexture(FluidStack stack) {
        return getStillTexture(stack.getFluid().defaultFluidState());
    }

    @Override
    public TextureAtlasSprite getStillTexture(FluidState state) {
        return handler.getFluidSprites(null, null, state)[0];
    }

    @Override
    public TextureAtlasSprite getFlowingTexture(FluidStack stack) {
        return getFlowingTexture(stack.getFluid().defaultFluidState());
    }

    @Override
    public TextureAtlasSprite getFlowingTexture(FluidState state) {
        return handler.getFluidSprites(null, null, state)[1];
    }

    @Override
    public int getTintColor(FluidStack stack) {
        return getTintColor(stack.getFluid().defaultFluidState());
    }

    @Override
    public int getTintColor(FluidState state) {
        return handler.getFluidColor(null, null, state);
    }
}
