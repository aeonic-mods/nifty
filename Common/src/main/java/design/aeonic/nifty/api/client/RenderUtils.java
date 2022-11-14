package design.aeonic.nifty.api.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import design.aeonic.nifty.api.client.screen.LineSet;
import design.aeonic.nifty.api.util.Texture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class RenderUtils {

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

    // Below here mostly copied implementations from private statics, just to avoid unnecessary transformations or reflectoin

    public static void renderTooltip(PoseStack poseStack, ItemStack itemStack, int x, int y) {
        renderTooltip(poseStack, getTooltipFromItem(itemStack), itemStack.getTooltipImage(), x, y);
    }

    public static void renderTooltip(PoseStack stack, List<Component> components, Optional<TooltipComponent> extra, int x, int y) {
        List<ClientTooltipComponent> $$5 = components.stream().map(Component::getVisualOrderText).map(ClientTooltipComponent::create).collect(Collectors.toList());
        extra.ifPresent(($$1x) -> $$5.add(1, ClientTooltipComponent.create($$1x)));
        renderTooltipInternal(stack, $$5, x, y);
    }

    private static void renderTooltipInternal(PoseStack stack, List<ClientTooltipComponent> tooltip, int x, int y) {
        if (!tooltip.isEmpty()) {
            int $$4 = 0;
            int $$5 = tooltip.size() == 1 ? -2 : 0;

            for(ClientTooltipComponent $$6 : tooltip) {
                int $$7 = $$6.getWidth(Minecraft.getInstance().font);
                if ($$7 > $$4) {
                    $$4 = $$7;
                }

                $$5 += $$6.getHeight();
            }

            int $$8 = x + 12;
            int $$9 = y - 12;
//            if ($$8 + $$4 > this.width) {
//                $$8 -= 28 + $$4;
//            }
//
//            if ($$9 + $$5 + 6 > this.height) {
//                $$9 = this.height - $$5 - 6;
//            }

            if (y - $$5 - 8 < 0) {
                $$9 = y + 8;
            }

            stack.pushPose();
            int $$12 = -267386864;
            int $$13 = 1347420415;
            int $$14 = 1344798847;
            int $$15 = 400;
            float $$16 = Minecraft.getInstance().getItemRenderer().blitOffset;
            Minecraft.getInstance().getItemRenderer().blitOffset = 400.0F;
            Tesselator $$17 = Tesselator.getInstance();
            BufferBuilder $$18 = $$17.getBuilder();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            $$18.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            Matrix4f $$19 = stack.last().pose();
            fillGradient($$19, $$18, $$8 - 3, $$9 - 4, $$8 + $$4 + 3, $$9 - 3, 400, -267386864, -267386864);
            fillGradient($$19, $$18, $$8 - 3, $$9 + $$5 + 3, $$8 + $$4 + 3, $$9 + $$5 + 4, 400, -267386864, -267386864);
            fillGradient($$19, $$18, $$8 - 3, $$9 - 3, $$8 + $$4 + 3, $$9 + $$5 + 3, 400, -267386864, -267386864);
            fillGradient($$19, $$18, $$8 - 4, $$9 - 3, $$8 - 3, $$9 + $$5 + 3, 400, -267386864, -267386864);
            fillGradient($$19, $$18, $$8 + $$4 + 3, $$9 - 3, $$8 + $$4 + 4, $$9 + $$5 + 3, 400, -267386864, -267386864);
            fillGradient($$19, $$18, $$8 - 3, $$9 - 3 + 1, $$8 - 3 + 1, $$9 + $$5 + 3 - 1, 400, 1347420415, 1344798847);
            fillGradient($$19, $$18, $$8 + $$4 + 2, $$9 - 3 + 1, $$8 + $$4 + 3, $$9 + $$5 + 3 - 1, 400, 1347420415, 1344798847);
            fillGradient($$19, $$18, $$8 - 3, $$9 - 3, $$8 + $$4 + 3, $$9 - 3 + 1, 400, 1347420415, 1347420415);
            fillGradient($$19, $$18, $$8 - 3, $$9 + $$5 + 2, $$8 + $$4 + 3, $$9 + $$5 + 3, 400, 1344798847, 1344798847);
            RenderSystem.enableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            BufferUploader.drawWithShader($$18.end());
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
            MultiBufferSource.BufferSource $$20 = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            stack.translate(0.0D, 0.0D, 400.0D);
            int $$21 = $$9;

            for(int $$22 = 0; $$22 < tooltip.size(); ++$$22) {
                ClientTooltipComponent $$23 = tooltip.get($$22);
                $$23.renderText(Minecraft.getInstance().font, $$8, $$21, $$19, $$20);
                $$21 += $$23.getHeight() + ($$22 == 0 ? 2 : 0);
            }

            $$20.endBatch();
            stack.popPose();
            $$21 = $$9;

            for(int $$24 = 0; $$24 < tooltip.size(); ++$$24) {
                ClientTooltipComponent $$25 = tooltip.get($$24);
                $$25.renderImage(Minecraft.getInstance().font, $$8, $$21, stack, Minecraft.getInstance().getItemRenderer(), 400);
                $$21 += $$25.getHeight() + ($$24 == 0 ? 2 : 0);
            }

            Minecraft.getInstance().getItemRenderer().blitOffset = $$16;
        }
    }

    public static List<Component> getTooltipFromItem(ItemStack stack) {
        return stack.getTooltipLines(Minecraft.getInstance().player, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
    }


    private static void fillGradient(Matrix4f $$0, BufferBuilder $$1, int $$2, int $$3, int $$4, int $$5, int $$6, int $$7, int $$8) {
        float $$9 = (float)($$7 >> 24 & 255) / 255.0F;
        float $$10 = (float)($$7 >> 16 & 255) / 255.0F;
        float $$11 = (float)($$7 >> 8 & 255) / 255.0F;
        float $$12 = (float)($$7 & 255) / 255.0F;
        float $$13 = (float)($$8 >> 24 & 255) / 255.0F;
        float $$14 = (float)($$8 >> 16 & 255) / 255.0F;
        float $$15 = (float)($$8 >> 8 & 255) / 255.0F;
        float $$16 = (float)($$8 & 255) / 255.0F;
        $$1.vertex($$0, (float)$$4, (float)$$3, (float)$$6).color($$10, $$11, $$12, $$9).endVertex();
        $$1.vertex($$0, (float)$$2, (float)$$3, (float)$$6).color($$10, $$11, $$12, $$9).endVertex();
        $$1.vertex($$0, (float)$$2, (float)$$5, (float)$$6).color($$14, $$15, $$16, $$13).endVertex();
        $$1.vertex($$0, (float)$$4, (float)$$5, (float)$$6).color($$14, $$15, $$16, $$13).endVertex();
    }
}
