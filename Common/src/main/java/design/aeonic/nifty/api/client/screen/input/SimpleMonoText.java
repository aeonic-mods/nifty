package design.aeonic.nifty.api.client.screen.input;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.RenderUtils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class SimpleMonoText {
    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/font/unicode_page_00.png");

    public static final int WIDTH = 7;
    public static final int HEIGHT = 12;

    private static final int left = 1;
    private static final int top = 36;
    private static final int marginX = 9;
    private static final int marginY = 3;

    private static final String[] lines = new String[]{
            "□!\"#$%&'()*+,-./",
            "0123456789:;<=>?",
            "@ABCDEFGHIJKLMNO",
            "PQRSTUVWXYZ[\\]^_",
            "`abcdefghijklmno",
            "pqrstuvwxyz{|}~"
    };

    private static final Map<Character, int[]> charCoords = new HashMap<>();
    static {
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                charCoords.put(lines[i].charAt(j), new int[]{left + j * (WIDTH + marginX), top + i * (HEIGHT + marginY)});
            }
        }
    }

    public static void drawRightAlign(PoseStack stack, String text, int x, int y, int blitOffset, int spacing, int length, int color, Integer shadowColor) {
        stack.pushPose();
        stack.scale(.5f, .5f, 1);
        stack.translate(x * 2, y * 2 + 1, 0);

        if (shadowColor != null) {
            stack.translate(1, 1, 0);
            drawRightAlign(stack, text, spacing, blitOffset, length, shadowColor);
            stack.translate(-1, -1, 0);
        }
        drawRightAlign(stack, text, spacing, blitOffset, length, color);
        stack.popPose();
    }

    public static void drawRightAlign(PoseStack stack, String text, int spacing, int blitOffset, int length, int color) {
        stack.pushPose();
        stack.translate((length - text.length()) * (WIDTH + spacing), 0, 0);
        draw(stack, text, spacing, blitOffset, color);
        stack.popPose();
    }

    public static void draw(PoseStack stack, String text, int x, int y, int blitOffset, int spacing, int color, Integer shadowColor) {
        stack.pushPose();
        stack.scale(.5f, .5f, 1);
        stack.translate(x * 2, y * 2 + 1, 0);

        if (shadowColor != null) {
            stack.translate(1, 1, 0);
            draw(stack, text, spacing, blitOffset, shadowColor);
            stack.translate(-1, -1, 0);
        }
        draw(stack, text, spacing, blitOffset, color);
        stack.popPose();
    }

    public static void draw(PoseStack stack, String text, int spacing, int blitOffset, int color) {
        float[] rgba = new float[4];
        RenderUtils.unpackRGBA(color, rgba);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(rgba[0], rgba[1], rgba[2], rgba[3]);

        for (int i = 0; i < text.length(); i++) {
            drawChar(stack, i * (WIDTH + spacing), 0, blitOffset, text.charAt(i));
        }
    }

    static void drawChar(PoseStack stack, int x, int y, int blitOffset, char character) {
        int[] coords = charCoords.get(character);
        if (coords != null) Screen.blit(stack, x, y, blitOffset, coords[0], coords[1], WIDTH, HEIGHT, 256, 256);
    }
}
