package design.aeonic.nifty.api.client.screen.input.gizmos;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.screen.drawable.Drawables;
import design.aeonic.nifty.api.client.screen.drawable.drawables.FillingDrawable;
import design.aeonic.nifty.api.client.screen.input.AbstractGizmo;
import design.aeonic.nifty.api.client.screen.input.GizmoScreen;
import design.aeonic.nifty.api.core.Translations;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class EnergyGizmo extends AbstractGizmo {
    private final FillingDrawable fillingDrawable;
    private final Supplier<Long> stored;
    private final Supplier<Long> capacity;

    public EnergyGizmo(int x, int y, Style style, Supplier<Long> stored, Supplier<Long> capacity) {
        this(x, y, style.get(), stored, capacity);
    }

    public EnergyGizmo(int x, int y, Style style, float r, float g, float b, float a, Supplier<Long> stored, Supplier<Long> capacity) {
        this(x, y, style.get(r, g, b, a), stored, capacity);
    }

    public EnergyGizmo(int x, int y, FillingDrawable fillingDrawable, Supplier<Long> stored, Supplier<Long> capacity) {
        super(x, y);
        this.fillingDrawable = fillingDrawable;
        this.stored = stored;
        this.capacity = capacity;
    }

    @Override
    public boolean mouseDown(GizmoScreen screen, int mouseX, int mouseY, int button) {
        return false;
    }

    @Nullable
    @Override
    public List<Component> getTooltip(GizmoScreen screen, int mouseX, int mouseY) {
        return List.of(Component.literal(String.format("%,d", stored.get()) + " / " + String.format("%,d", capacity.get()) + " ").append(Translations.Generic.POWER_UNITS));
    }

    @Override
    public void draw(PoseStack stack, GizmoScreen screen, int mouseX, int mouseY, float partialTicks) {
        float fill = (float) (stored.get() / (double) capacity.get());
        fillingDrawable.draw(stack, getX(), getY(), 200, fill);
    }

    @Override
    public int getWidth() {
        return fillingDrawable.getEmptyTexture().width();
    }

    @Override
    public int getHeight() {
        return fillingDrawable.getEmptyTexture().height();
    }

    public enum Style {
        HORIZONTAL,
        VERTICAL_TALL_WIDE,
        VERTICAL_TALL_THIN,
        VERTICAL_SHORT_WIDE,
        VERTICAL_SHORT_THIN;

        public FillingDrawable get(float r, float g, float b, float a) {
            return get().copyWithColor(r, g, b, a);
        }

        public FillingDrawable get() {
            return switch(this) {
                case HORIZONTAL -> Drawables.ENERGY_BAR_HORIZONTAL;
                case VERTICAL_TALL_WIDE -> Drawables.ENERGY_BAR_VERTICAL_TALL_WIDE;
                case VERTICAL_TALL_THIN -> Drawables.ENERGY_BAR_VERTICAL_TALL_THIN;
                case VERTICAL_SHORT_WIDE -> Drawables.ENERGY_BAR_VERTICAL_SHORT_WIDE;
                case VERTICAL_SHORT_THIN -> Drawables.ENERGY_BAR_VERTICAL_SHORT_THIN;
            };
        }
    }
}
