package design.aeonic.nifty.api.client.screen.drawable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.RenderUtils;
import design.aeonic.nifty.api.client.screen.drawable.drawables.StaticDrawable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.system.linux.Stat;

public record Texture(ResourceLocation location, int fileWidth, int fileHeight, int width, int height, int u, int v) implements StaticDrawable {
    public Texture(String location, int fileWidth, int fileHeight) {
        this(new ResourceLocation(location), fileWidth, fileHeight);
    }

    public Texture(String location, int fileWidth, int fileHeight, int width, int height) {
        this(new ResourceLocation(location), fileWidth, fileHeight, width, height, 0, 0);
    }

    public Texture(String location, int fileWidth, int fileHeight, int width, int height, int u, int v) {
        this(new ResourceLocation(location), fileWidth, fileHeight, width, height, u, v);
    }

    public Texture(ResourceLocation location, int fileWidth, int fileHeight) {
        this(location, fileWidth, fileHeight, fileWidth, fileHeight);
    }

    public Texture(ResourceLocation location, int fileWidth, int fileHeight, int width, int height) {
        this(location, fileWidth, fileHeight, width, height, 0, 0);
    }

    public StaticDrawable withColor(int color) {
        float[] rgba = new float[4];
        RenderUtils.unpackRGBA(color, rgba);
        return (stack, x, y, zOffset) -> draw(stack, x, y, zOffset, rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    public StaticDrawable withColor(float r, float g, float b, float a) {
        return (stack, x, y, zOffset) -> draw(stack, x, y, zOffset, r, g, b, a);
    }

    public void draw(PoseStack stack, int x, int y, int zOffset) {
        draw(stack, x, y, zOffset, false);
    }

    public void draw(PoseStack stack, int x, int y, int zOffset, boolean blend) {
        draw(stack, x, y, zOffset, 1, 1, 1, 1, blend);
    }

    public void draw(PoseStack stack, int x, int y, int zOffset, float r, float g, float b, float a) {
        draw(stack, x, y, zOffset, r, g, b, a, false);
    }

    public void draw(PoseStack stack, int x, int y, int zOffset, float r, float g, float b, float a, boolean blend) {
        draw(stack, x, y, zOffset, width, height, r, g, b, a, blend);
    }

    public void draw(PoseStack stack, int x, int y, int zOffset, int width, int height) {
        draw(stack, x, y, zOffset, width, height, 1, 1, 1, 1, false);
    }

    public void drawWithUv(PoseStack stack, int x, int y, int zOffset, int width, int height, int u, int v) {
        drawWithUv(stack, x, y, zOffset, width, height, u, v, 1, 1, 1, 1, false);
    }

    public void drawWithUv(PoseStack stack, int x, int y, int zOffset, int width, int height, int u, int v, float r, float g, float b, float a) {
        drawWithUv(stack, x, y, zOffset, width, height, u, v, r, g, b, a, false);
    }

    public void drawWithUv(PoseStack stack, int x, int y, int zOffset, int width, int height, int u, int v, float r, float g, float b, float a, boolean blend) {
        setup(r, g, b, a);
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
        }
        Screen.blit(stack, x, y, zOffset, this.u + u, this.v + v, width, height, fileWidth, fileHeight);
    }

    public void draw(PoseStack stack, int x, int y, int zOffset, int width, int height, float r, float g, float b, float a, boolean blend) {
        setup(r, g, b, a);
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
        }
        Screen.blit(stack, x, y, zOffset, u, v, width, height, fileWidth, fileHeight);
    }

    public void setup(float r, float g, float b, float a) {
        RenderSystem.setShaderColor(r, g, b, a);
        setup();
    }

    public void setup() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, location);
    }
}
