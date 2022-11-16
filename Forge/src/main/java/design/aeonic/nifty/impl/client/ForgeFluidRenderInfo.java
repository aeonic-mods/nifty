package design.aeonic.nifty.impl.client;

import design.aeonic.nifty.api.client.FluidRenderInfo;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import design.aeonic.nifty.impl.transfer.fluid.ForgeFluidStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;

import java.util.HashMap;
import java.util.Map;

public class ForgeFluidRenderInfo implements FluidRenderInfo {
    private static final Map<Fluid, ForgeFluidRenderInfo> CACHE = new HashMap<>();

    private final IClientFluidTypeExtensions clientFluidExtensions;

    private ForgeFluidRenderInfo(Fluid fluid) {
        this.clientFluidExtensions = IClientFluidTypeExtensions.of(fluid);
    }

    public static ForgeFluidRenderInfo get(Fluid fluid) {
        return CACHE.computeIfAbsent(fluid, ForgeFluidRenderInfo::new);
    }

    @Override
    public TextureAtlasSprite getStillTexture(FluidStack stack) {
        return getBlockSprite(clientFluidExtensions.getStillTexture(ForgeFluidStorage.toForgeStack(stack)));
    }

    @Override
    public TextureAtlasSprite getStillTexture(FluidState state) {
        return getBlockSprite(clientFluidExtensions.getStillTexture());
    }

    @Override
    public TextureAtlasSprite getFlowingTexture(FluidStack stack) {
        return getBlockSprite(clientFluidExtensions.getFlowingTexture(ForgeFluidStorage.toForgeStack(stack)));
    }

    @Override
    public TextureAtlasSprite getFlowingTexture(FluidState state) {
        return getBlockSprite(clientFluidExtensions.getFlowingTexture());
    }

    TextureAtlasSprite getBlockSprite(ResourceLocation location) {
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(location);
    }

    @Override
    public int getTintColor(FluidStack stack) {
        return clientFluidExtensions.getTintColor(ForgeFluidStorage.toForgeStack(stack));
    }

    @Override
    public int getTintColor(FluidState state) {
        return clientFluidExtensions.getTintColor();
    }
}
