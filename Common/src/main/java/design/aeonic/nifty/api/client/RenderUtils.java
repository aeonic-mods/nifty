package design.aeonic.nifty.api.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import design.aeonic.nifty.api.client.screen.drawable.Texture;
import design.aeonic.nifty.api.platform.PlatformAccess;
import design.aeonic.nifty.api.transfer.fluid.FluidStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

/**
 * Messy class with some utility methods for rendering.
 */
public final class RenderUtils {

    /**
     * Renders a fluid block with the given state (though it ignores the level - use the height parameter instead).
     */
    public static void drawFluidBlock(PoseStack stack, FluidState fluidState, FluidRenderInfo fluidInfo, float height) {
        TextureAtlasSprite sprite = fluidInfo.getStillTexture(fluidState);
        float[] color = new float[4];
        RenderUtils.unpackRGBA(fluidInfo.getTintColor(fluidState), color);

        RenderSystem.setShaderColor(color[1], color[2], color[3], 1f);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, sprite.atlas().location());

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV(height * .98f * sprite.getHeight());

        stack.pushPose();
        Matrix4f pose = stack.last().pose();
        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        builder.vertex(pose, 0, height * .98f, .02f).uv(u0, v1).endVertex();
        builder.vertex(pose, 1, height * .98f, .02f).uv(u1, v1).endVertex();
        builder.vertex(pose, 1, 0, .02f).uv(u1, v0).endVertex();
        builder.vertex(pose, 0, 0, .02f).uv(u0, v0).endVertex();

        builder.vertex(pose, 1, height * .98f, .98f).uv(u0, v1).endVertex();
        builder.vertex(pose, 0, height * .98f, .98f).uv(u1, v1).endVertex();
        builder.vertex(pose, 0, 0, .98f).uv(u1, v0).endVertex();
        builder.vertex(pose, 1, 0, .98f).uv(u0, v0).endVertex();

        builder.vertex(pose, .98f, height * .98f, 0).uv(u0, v1).endVertex();
        builder.vertex(pose, .98f, height * .98f, 1).uv(u1, v1).endVertex();
        builder.vertex(pose, .98f, 0, 1).uv(u1, v0).endVertex();
        builder.vertex(pose, .98f, 0, 0).uv(u0, v0).endVertex();

        builder.vertex(pose, .02f, height * .98f, 1).uv(u0, v1).endVertex();
        builder.vertex(pose, .02f, height * .98f, 0).uv(u1, v1).endVertex();
        builder.vertex(pose, .02f, 0, 0).uv(u1, v0).endVertex();
        builder.vertex(pose, .02f, 0, 1).uv(u0, v0).endVertex();

        builder.vertex(pose, 0, height * .98f, 1).uv(u0, sprite.getV1()).endVertex();
        builder.vertex(pose, 1, height * .98f, 1).uv(u1, sprite.getV1()).endVertex();
        builder.vertex(pose, 1, height * .98f, 0).uv(u1, v0).endVertex();
        builder.vertex(pose, 0, height * .98f, 0).uv(u0, v0).endVertex();

        builder.end();
        stack.popPose();

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Draws a fluid stack to the screen, intended for use in a GUI.
     * @param stack the pose stack
     * @param x the x position
     * @param y the y position
     * @param zOffset the z position (blit offset)
     * @param width the width
     * @param height the height
     * @param fluidStack the fluid stack
     * @param fluidRenderInfo the fluid render info (obtained from {@link PlatformAccess#getFluidRenderInfo(Fluid)}
     */
    public static void drawScreenFluid(PoseStack stack, int x, int y, int zOffset, int width, int height, FluidStack fluidStack, FluidRenderInfo fluidRenderInfo) {
        TextureAtlasSprite sprite = fluidRenderInfo.getStillTexture(fluidStack);
        float[] color = new float[4];
        RenderUtils.unpackRGBA(fluidRenderInfo.getTintColor(fluidStack), color);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(color[0], color[1], color[2], color[3]);
        RenderSystem.setShaderTexture(0, sprite.atlas().location());
        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        Matrix4f pose = stack.last().pose();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        do {
            int drawHeight = Math.min(sprite.getHeight(), height);
            height -= drawHeight;
            float v1 = sprite.getV((16f * drawHeight) / sprite.getHeight());
            int x2 = x;
            int widthLeft = width;

            do {
                int drawWidth = Math.min(sprite.getWidth(), widthLeft);
                widthLeft -= drawWidth;
                float u1 = sprite.getU((16f * drawWidth) / sprite.getWidth());

                builder.vertex(pose, x2, y + drawHeight, zOffset).uv(sprite.getU0(), v1).endVertex();
                builder.vertex(pose, x2 + drawWidth, y + drawHeight, zOffset).uv(u1, v1).endVertex();
                builder.vertex(pose, x2 + drawWidth, y, zOffset).uv(u1, sprite.getV0()).endVertex();
                builder.vertex(pose, x2, y, zOffset).uv(sprite.getU0(), sprite.getV0()).endVertex();

                x2 += drawWidth;
            } while (widthLeft > 0);
        } while (height > 0);

        builder.end();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    public static void angleGradient(PoseStack stack, int x1, int y1, int x2, int y2, int z, int fromRGBA, int toRGBA) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        angleGradient(stack.last().pose(), builder, x1, y1, x2, y2, z, fromRGBA, toRGBA);
        tesselator.end();
    }

    public static void angleGradient(Matrix4f pose, BufferBuilder builder, int x1, int y1, int x2, int y2, int z, int fromRGBA, int toRGBA) {
        builder.vertex(pose, (float)x2, (float)y1, (float)z).color(fromRGBA).endVertex();
        builder.vertex(pose, (float)x1, (float)y1, (float)z).color(fromRGBA).endVertex();
        builder.vertex(pose, (float)x1, (float)y2, (float)z).color(fromRGBA).endVertex();
        builder.vertex(pose, (float)x2, (float)y2, (float)z).color(toRGBA).endVertex();
    }

    public static void unpackRGB(int color, float[] rgb) {
        rgb[0] = ((color >> 16) & 0xFF) / 255.0F;
        rgb[1] = ((color >> 8) & 0xFF) / 255.0F;
        rgb[2] = (color & 0xFF) / 255.0F;
    }

    public static void unpackRGBA(int color, float[] rgba) {
        rgba[0] = ((color >> 16) & 0xFF) / 255f;
        rgba[1] = ((color >> 8) & 0xFF) / 255f;
        rgba[2] = (color & 0xFF) / 255f;
        rgba[3] = ((color >> 24) & 0xFF) / 255f;
    }

    public static void drawRect(PoseStack stack, Texture texture, int x, int y, int zOffset, int width, int height) {
        drawRect(stack, texture, x, y, zOffset, width, height, 0xFFFFFFFF);
    }

    /**
     * Draws a rectangle with the given texture tiled to fit the dimensions. Ex: nodes, which use a 16x16 texture.
     * See the `textures/gui/graph/node.png` file for an example.
     */
    public static void drawRect(PoseStack stack, Texture texture, int x, int y, int zOffset, int width, int height, int color) {
        int cornerWidth = texture.fileWidth() / 4;
        int cornerHeight = texture.fileHeight() / 4;

        float[] rgba = new float[4];
        unpackRGBA(color, rgba);
        texture.setup(rgba[0], rgba[1], rgba[2], rgba[3]);

        // Corners
        Screen.blit(stack, x, y, zOffset, 0, 0, cornerWidth, cornerHeight, texture.fileWidth(), texture.fileHeight());
        Screen.blit(stack, x + width - cornerWidth, y, zOffset, texture.fileWidth() - cornerWidth, 0, cornerWidth, cornerHeight, texture.fileWidth(), texture.fileHeight());
        Screen.blit(stack, x, y + height - cornerHeight, zOffset, 0, texture.fileHeight() - cornerHeight, cornerWidth, cornerHeight, texture.fileWidth(), texture.fileHeight());
        Screen.blit(stack, x + width - cornerWidth, y + height - cornerHeight, zOffset, texture.fileWidth() - cornerWidth, texture.fileHeight() - cornerHeight, cornerWidth, cornerHeight, texture.fileWidth(), texture.fileHeight());

        for (int i = cornerWidth; i < width - cornerWidth; i += cornerWidth * 2) {
            int w = Math.min(cornerWidth * 2, width - cornerWidth - i);
            // Top + bottom edges
            Screen.blit(stack, x + i, y, zOffset, cornerWidth, 0, w, cornerHeight, texture.fileWidth(), texture.fileHeight());
            Screen.blit(stack, x + i, y + height - cornerHeight, zOffset, cornerWidth, texture.fileHeight() - cornerHeight, w, cornerHeight, texture.fileWidth(), texture.fileHeight());

            // Infill
            for (int j = cornerHeight; j < height - cornerHeight; j += cornerHeight * 2) {
                int h = Math.min(cornerHeight * 2, height - cornerHeight - j);
                Screen.blit(stack, x + i, y + j, zOffset, cornerWidth, cornerHeight, w, h, texture.fileWidth(), texture.fileHeight());
            }
        }

        // Left + right edges
        for (int i = cornerHeight; i < height - cornerHeight; i += cornerHeight * 2) {
            int h = Math.min(cornerHeight * 2, height - cornerHeight - i);
            Screen.blit(stack, x, y + i, zOffset, 0, cornerHeight, cornerWidth, h, texture.fileWidth(), texture.fileHeight());
            Screen.blit(stack, x + width - cornerWidth, y + i, zOffset, texture.fileWidth() - cornerWidth, cornerHeight, cornerWidth, h, texture.fileWidth(), texture.fileHeight());
        }
    }
}
