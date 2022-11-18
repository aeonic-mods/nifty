package design.aeonic.nifty.api.client.screen.input.gizmos;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.FluidRenderInfo;
import design.aeonic.nifty.api.client.RenderUtils;
import design.aeonic.nifty.api.client.screen.drawable.Texture;
import design.aeonic.nifty.api.client.screen.input.AbstractGizmo;
import design.aeonic.nifty.api.client.screen.input.GizmoScreen;
import design.aeonic.nifty.api.client.screen.input.TooltipStyle;
import design.aeonic.nifty.api.core.Services;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class FluidStackGizmo extends AbstractGizmo {
    private final Supplier<FluidStack> fluidStack;
    private final Supplier<Integer> capacity;
    private final Texture tankOverlay;
    private int width;
    private int height;
    private Fluid fluidCache;
    private FluidRenderInfo fluidRenderInfo;

    /**
     * @param x the x position
     * @param y the y position
     * @param width the width
     * @param height the height
     * @param fluidStack the fluid stack
     */
    public FluidStackGizmo(int x, int y, int width, int height, Supplier<FluidStack> fluidStack) {
        this(x, y, width, height, fluidStack, null);
    }

    /**
     * @param x the x position
     * @param y the y position
     * @param width the width
     * @param height the height
     * @param fluidStack the fluid stack
     * @param capacity the capacity, used to render partially full tanks - if null will just fill the given height
     */
    public FluidStackGizmo(int x, int y, int width, int height, Supplier<FluidStack> fluidStack, @Nullable Supplier<Integer> capacity) {
        this(x, y, width, height, fluidStack, capacity, null);
    }

    /**
     * @param x the x position
     * @param y the y position
     * @param fluidStack the fluid stack
     * @param capacity the capacity, used to render partially full tanks - if null will just fill the given height
     * @param tankOverlay the tank overlay texture - nonnull; this constructor infers dimensions from the texture size
     */
    public FluidStackGizmo(int x, int y, Supplier<FluidStack> fluidStack, @Nullable Supplier<Integer> capacity, @Nonnull Texture tankOverlay) {
        this(x, y, tankOverlay.width(), tankOverlay.height(), fluidStack, capacity, tankOverlay);
    }

    /**
     * @param x the x position
     * @param y the y position
     * @param width the width
     * @param height the height
     * @param fluidStack the fluid stack
     * @param capacity the capacity, used to render partially full tanks - if null will just fill the given height
     * @param tankOverlay the tank overlay texture, if null will not render an overlay
     */
    public FluidStackGizmo(int x, int y, int width, int height, Supplier<FluidStack> fluidStack, @Nullable Supplier<Integer> capacity, @Nullable Texture tankOverlay) {
        super(x, y);

        this.fluidStack = fluidStack;
        this.capacity = capacity;
        this.tankOverlay = tankOverlay;
        this.width = width;
        this.height = height;
    }

    void update() {
        if (fluidCache != fluidStack.get().getFluid()) {
            fluidCache = fluidStack.get().getFluid();
            fluidRenderInfo = Services.ACCESS.getFluidRenderInfo(fluidStack.get().getFluid());
        }
    }

    @Override
    public void draw(PoseStack stack, GizmoScreen screen, int mouseX, int mouseY, float partialTicks) {
        update();

        if (capacity == null) RenderUtils.drawScreenFluid(stack, getX(), getY(), 200, 16, 16, fluidStack.get(), fluidRenderInfo);
        else {
            int drawHeight = (int) (height * (fluidStack.get().getAmount() / (float) capacity.get()));
            RenderUtils.drawScreenFluid(stack, getX(), getY() + height - drawHeight, 200, 16, drawHeight, fluidStack.get(), fluidRenderInfo);
        }
        if (tankOverlay != null) tankOverlay.draw(stack, getX(), getY(), 300);
    }

    @Nullable
    @Override
    public List<Component> getTooltip(GizmoScreen screen, int mouseX, int mouseY) {
        return fluidStack.get().getTooltip();
    }

    @Override
    public TooltipStyle getTooltipStyle(GizmoScreen screen, int mouseX, int mouseY) {
        return TooltipStyle.VANILLA;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
