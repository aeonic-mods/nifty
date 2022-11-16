package design.aeonic.nifty.api.client.screen.input;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.RenderUtils;
import design.aeonic.nifty.api.client.Texture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Comparator;
import java.util.List;

/**
 * Describes a style of tooltip that can be rendered; used by {@link Gizmo}s.
 */
public interface TooltipStyle {
    /**
     * The vanilla tooltip renderer, used by item stacks. Not default.
     */
    TooltipStyle VANILLA = new VanillaTooltipStyle();
    /**
     * The Nifty tooltip renderer, used for most UI gizmos by default.
     */
    TooltipStyle NIFTY = new NiftyTooltipStyle();
    
    void renderTooltip(Screen screen, PoseStack stack, int mouseX, int mouseY, List<Component> tooltip);

    class VanillaTooltipStyle implements TooltipStyle {
        @Override
        public void renderTooltip(Screen screen, PoseStack stack, int mouseX, int mouseY, List<Component> tooltip) {
            screen.renderComponentTooltip(stack, tooltip, mouseX, mouseY);
        }
    }

    class NiftyTooltipStyle implements TooltipStyle {
        public static final Texture TOOLTIP = new Texture("nifty:textures/gui/tooltip.png", 16, 16, 16, 16);

        @Override
        public void renderTooltip(Screen screen, PoseStack stack, int mouseX, int mouseY, List<Component> tooltip) {
            Font font = Minecraft.getInstance().font;
            int x = mouseX + 6;
            int y = mouseY + 12;
            int width = font.width(tooltip.stream().max(Comparator.comparingInt(font::width)).orElse(Component.empty())) + 4;
            int height = tooltip.size() * font.lineHeight + 3;

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderUtils.drawRect(stack, TOOLTIP, x - 2, y - 2, screen.getBlitOffset() + 400, width, height, 0xF0FFFFFF);

            stack.pushPose();
            stack.translate(0, 0, 500);
            for (int i = 0; i < tooltip.size(); i++) {
                font.draw(stack, tooltip.get(i), x, y + i * font.lineHeight, 0xFFFFFF);
            }
            stack.popPose();
            RenderSystem.disableBlend();
        }
    }
}
