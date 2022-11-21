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

    private Drawables() {}
}
