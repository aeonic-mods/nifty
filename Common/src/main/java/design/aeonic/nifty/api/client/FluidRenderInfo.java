package design.aeonic.nifty.api.client;

import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

/**
 * Exposes rendering info for a given fluid. Obtained from {@link design.aeonic.nifty.api.services.PlatformAccess#getFluidRenderInfo(Fluid)}.
 */
public interface FluidRenderInfo {
    /**
     * On fabric, just calls {@link #getFlowingTexture(FluidState)} with the fluid's default state. On forge, might render
     * based on NBT data as Forge's API includes an additional method to render by fluid stack. No guarantees.
     */
    TextureAtlasSprite getStillTexture(FluidStack stack);

    /**
     * Gets the sprite for the fluid's still texture.
     */
    TextureAtlasSprite getStillTexture(FluidState state);

    /**
     * On fabric, just calls {@link #getFlowingTexture(FluidState)} with the fluid's default state. On forge, might render
     * based on NBT data as Forge's API includes an additional method to render by fluid stack. No guarantees.
     */
    TextureAtlasSprite getFlowingTexture(FluidStack stack);

    /**
     * Gets the sprite for the fluid's flowing texture.
     */
    TextureAtlasSprite getFlowingTexture(FluidState state);

    /**
     * On fabric, just calls {@link #getTintColor(FluidState)} with the fluid's default state. On forge, might render
     * based on NBT data as Forge's API includes an additional method to render by fluid stack. No guarantees.
     */
    int getTintColor(FluidStack stack);

    /**
     * Gets the tint color of the fluid for the given state.
     */
    int getTintColor(FluidState state);
}
