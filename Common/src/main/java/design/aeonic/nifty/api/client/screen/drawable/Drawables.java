package design.aeonic.nifty.api.client.screen.drawable;

import design.aeonic.nifty.api.client.screen.drawable.drawables.FillingDrawable;
import design.aeonic.nifty.api.client.screen.drawable.drawables.StaticDrawable;
import design.aeonic.nifty.api.util.Direction2D;
import design.aeonic.nifty.api.util.Progress;

import java.util.function.Supplier;

/**
 * Contains some drawables for ease of use - recipe arrows, an empty container gui, item slots, etc. Some have more
 * advanced functionality via {@link Drawable}'s context parameter, but these can be converted to a {@link StaticDrawable}
 * via {@link Drawable#with(Supplier)}.
 */
public final class Drawables {
    /**
     * An empty container background, like a chest - has only the player inventory slots drawn.
     */
    public static final Texture EMPTY_CONTAINER = new Texture("nifty:textures/gui/container/empty.png", 256, 256, 176, 166);

    /**
     * A vanilla item slot, 18x18.
     */
    public static final Texture ITEM_SLOT = new Texture("nifty:textures/gui/slots.png", 64, 64, 18, 18);

    /**
     * A larger vanilla item slot, such as the crafting table output. 26x26.
     */
    public static final Texture ITEM_SLOT_OUTPUT = new Texture("nifty:textures/gui/slots.png", 64, 64, 26, 26, 18, 0);

    /**
     * A static recipe arrow, like that used in the crafting table.
     */
    public static final Texture RECIPE_ARROW_NORMAL = new Texture("nifty:textures/gui/arrows.png", 64, 64, 22, 16);

    /**
     * The white-filled recipe arrow, like that drawn over a furnace gui. For a filling arrow, use {@link #RECIPE_ARROW_FILLING}.
     */
    public static final Texture RECIPE_ARROW_FILL = new Texture("nifty:textures/gui/arrows.png", 64, 64, 22, 16, 22, 0);

    /**
     * A static recipe arrow with a red X, like that used for unsupported recipes in an anvil.
     */
    public static final Texture RECIPE_ARROW_DISALLOWED = new Texture("nifty:textures/gui/arrows.png", 64, 64, 22, 16, 0, 16);

    /**
     * A filling recipe arrow, like that used in a furnace gui.
     */
    public static final FillingDrawable RECIPE_ARROW_FILLING = new FillingDrawable(RECIPE_ARROW_NORMAL, RECIPE_ARROW_FILL);

    /**
     * The empty part of the burn timer used in the furnace. For a fully functional burn timer that represents lit duration, see {@link #BURN_TIMER}.
     */
    public static final Texture FURNACE_FIRE_EMPTY = new Texture("nifty:textures/gui/burn_timer.png", 32, 32, 14, 14);

    /**
     * The full/burning part of the burn timer used in the furnace. For a fully functional burn timer that represents lit duration, see {@link #BURN_TIMER}.
     */
    public static final Texture FURNACE_FIRE_FULL = new Texture("nifty:textures/gui/burn_timer.png", 32, 32, 14, 14, 14, 0);

    /**
     * A burn time indicator like that used in the furnace. Ticks down based on the passed context, which is inverted by the drawable.
     * For basic use, see {@link FillingDrawable#with} - you'll generally just want {@link Progress#of(Supplier, Supplier)}
     * ie {@code BURN_TIMER.with(Progress.of(() -> litTime, () -> litDuration))}
     */
    public static final FillingDrawable BURN_TIMER = new FillingDrawable(FURNACE_FIRE_EMPTY, FURNACE_FIRE_FULL, Direction2D.UP);

    /**
     * The background part of a horizontal energy bar that spans most of a normal-sized container, 162x5.
     */
    public static final Texture ENERGY_BAR_HORIZONTAL_BG = new Texture("nifty:textures/gui/energy.png", 256, 256, 162, 5);

    /**
     * The filling part of a horizontal energy bar that spans most of a normal-sized container, 162x5.
     */
    public static final Texture ENERGY_BAR_HORIZONTAL_FILL = new Texture("nifty:textures/gui/energy.png", 256, 256, 162, 5, 0, 5);

    /**
     * The background part of a wider vertical energy bar that spans most of a normal-sized container (vertically), 9x72.
     */
    public static final Texture ENERGY_BAR_VERTICAL_TALL_WIDE_BG = new Texture("nifty:textures/gui/energy.png", 256, 256, 9, 72, 0, 10);

    /**
     * The filling part of a wider vertical energy bar that spans most of a normal-sized container (vertically), 9x72.
     */
    public static final Texture ENERGY_BAR_VERTICAL_TALL_WIDE_FILL = new Texture("nifty:textures/gui/energy.png", 256, 256, 9, 72, 9, 10);

    /**
     * The background part of a thinner vertical energy bar that spans most of a normal-sized container (vertically), 5x72.
     */
    public static final Texture ENERGY_BAR_VERTICAL_TALL_THIN_BG = new Texture("nifty:textures/gui/energy.png", 256, 256, 5, 72, 18, 10);

    /**
     * The filling part of a thinner vertical energy bar that spans most of a normal-sized container (vertically), 5x72.
     */
    public static final Texture ENERGY_BAR_VERTICAL_TALL_THIN_FILL = new Texture("nifty:textures/gui/energy.png", 256, 256, 5, 72, 23, 10);

    /**
     * The background part of a wider vertical energy bar that spans half of a normal-sized container (vertically), 5x36.
     */
    public static final Texture ENERGY_BAR_VERTICAL_SHORT_WIDE_BG = new Texture("nifty:textures/gui/energy.png", 256, 256, 9, 36, 0, 82);

    /**
     * The filling part of a wider vertical energy bar that spans half of a normal-sized container (vertically), 5x36.
     */
    public static final Texture ENERGY_BAR_VERTICAL_SHORT_WIDE_FILL = new Texture("nifty:textures/gui/energy.png", 256, 256, 9, 36, 9, 82);

    /**
     * The background part of a thinner vertical energy bar that spans half of a normal-sized container (vertically), 5x36.
     */
    public static final Texture ENERGY_BAR_VERTICAL_SHORT_THIN_BG = new Texture("nifty:textures/gui/energy.png", 256, 256, 5, 36, 18, 82);

    /**
     * The filling part of a thinner vertical energy bar that spans half of a normal-sized container (vertically), 5x36.
     */
    public static final Texture ENERGY_BAR_VERTICAL_SHORT_THIN_FILL = new Texture("nifty:textures/gui/energy.png", 256, 256, 5, 36, 23, 82);

    /**
     * A horizontal energy bar. Lacks a tooltip; in most cases you'll want {@link design.aeonic.nifty.api.client.screen.input.gizmos.EnergyGizmo}.
     */
    public static final FillingDrawable ENERGY_BAR_HORIZONTAL = new FillingDrawable(ENERGY_BAR_HORIZONTAL_BG, ENERGY_BAR_HORIZONTAL_FILL);

    /**
     * A tall, wide vertical energy bar. Lacks a tooltip; in most cases you'll want {@link design.aeonic.nifty.api.client.screen.input.gizmos.EnergyGizmo}.
     */
    public static final FillingDrawable ENERGY_BAR_VERTICAL_TALL_WIDE = new FillingDrawable(ENERGY_BAR_VERTICAL_TALL_WIDE_BG, ENERGY_BAR_VERTICAL_TALL_WIDE_FILL, Direction2D.UP);

    /**
     * A tall, thin vertical energy bar. Lacks a tooltip; in most cases you'll want {@link design.aeonic.nifty.api.client.screen.input.gizmos.EnergyGizmo}.
     */
    public static final FillingDrawable ENERGY_BAR_VERTICAL_TALL_THIN = new FillingDrawable(ENERGY_BAR_VERTICAL_TALL_THIN_BG, ENERGY_BAR_VERTICAL_TALL_THIN_FILL, Direction2D.UP);

    /**
     * A short, wide vertical energy bar. Lacks a tooltip; in most cases you'll want {@link design.aeonic.nifty.api.client.screen.input.gizmos.EnergyGizmo}.
     */
    public static final FillingDrawable ENERGY_BAR_VERTICAL_SHORT_WIDE = new FillingDrawable(ENERGY_BAR_VERTICAL_SHORT_WIDE_BG, ENERGY_BAR_VERTICAL_SHORT_WIDE_FILL, Direction2D.UP);

    /**
     * A short, thin vertical energy bar. Lacks a tooltip; in most cases you'll want {@link design.aeonic.nifty.api.client.screen.input.gizmos.EnergyGizmo}.
     */
    public static final FillingDrawable ENERGY_BAR_VERTICAL_SHORT_THIN = new FillingDrawable(ENERGY_BAR_VERTICAL_SHORT_THIN_BG, ENERGY_BAR_VERTICAL_SHORT_THIN_FILL, Direction2D.UP);

    private Drawables() {}
}
