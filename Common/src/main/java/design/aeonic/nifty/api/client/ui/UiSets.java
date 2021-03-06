package design.aeonic.nifty.api.client.ui;

import design.aeonic.nifty.api.client.ui.template.BooleanUiElementTemplate;
import design.aeonic.nifty.api.client.ui.template.EnergyUiElementTemplate;
import design.aeonic.nifty.api.client.ui.template.FillingUiElementTemplate;
import design.aeonic.nifty.api.client.ui.template.StaticUiElementTemplate;
import design.aeonic.nifty.api.client.ui.template.TankUiElementTemplate;
import design.aeonic.nifty.api.core.Constants;
import net.minecraft.resources.ResourceLocation;

/**
 * Contains {@link UiElementTemplate}s for ease of use,6 with default assets shipped with Nifty
 */
public final class UiSets {

    /**
     * A UI set with some vanilla elements and vanilla style extras - tanks, etc
     */
    public static final class Vanilla {

        /**
         * A texture containing all elements of the Vanilla UI set.
         */
        public static final Texture TEXTURE_MAP = new Texture(new ResourceLocation(Constants.NIFTY_ID, "textures/gui/template/vanilla.png"));

        // Boring stuff

        /**
         * A Screen background with the same size as most vanilla menu screens, blank except for player inventory slots.
         */
        public static final StaticUiElementTemplate BACKGROUND = new StaticUiElementTemplate(TEXTURE_MAP, 176, 166, 80, 90);

        // Item slots

        /**
         * A normal item slot.
         */
        public static final StaticUiElementTemplate ITEM_SLOT_NORMAL = new StaticUiElementTemplate(TEXTURE_MAP, 18, 18, 0, 108);

        /**
         * An output slot used in vanilla furnaces etc.
         */
        public static final StaticUiElementTemplate ITEM_SLOT_OUTPUT = new StaticUiElementTemplate(TEXTURE_MAP, 26, 26, 18, 108);

        // Tanks

        /**
         * A tank the size of a normal inventory slot.
         */
        public static final TankUiElementTemplate TANK_ONE_ONE = new TankUiElementTemplate(TEXTURE_MAP,
                18, 18, 126, 0, 44, 108, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        /**
         * A tank two slots high and the width of half a slot.
         */
        public static final TankUiElementTemplate TANK_TWO_HALF = new TankUiElementTemplate(TEXTURE_MAP,
                9, 36, 126, 0, 0, 72, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        /**
         * A tank two slots high and the width of one slot.
         */
        public static final TankUiElementTemplate TANK_TWO_ONE = new TankUiElementTemplate(TEXTURE_MAP,
                18, 36, 126, 0, 9, 72, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        /**
         * A tank two slots high and wide.
         */
        public static final TankUiElementTemplate TANK_TWO_TWO = new TankUiElementTemplate(TEXTURE_MAP,
                36, 36, 126, 0, 27, 72, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        /**
         * A tank four slots high and the width of one slot.
         */
        public static final TankUiElementTemplate TANK_FOUR_ONE = new TankUiElementTemplate(TEXTURE_MAP,
                18, 72, 126, 0, 0, 0, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        /**
         * A tank four slots high and the width of two slots.
         */
        public static final TankUiElementTemplate TANK_FOUR_TWO = new TankUiElementTemplate(TEXTURE_MAP,
                36, 72, 126, 0, 27, 0, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        /**
         * A tank four slots high and wide.
         */
        public static final TankUiElementTemplate TANK_FOUR_FOUR = new TankUiElementTemplate(TEXTURE_MAP,
                72, 72, 126, 0, 54, 0, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        // Energy

        /**
         * An energy meter four slots high and the width of half a slot.
         */
        public static final EnergyUiElementTemplate ENERGY_FOUR_HALF = new EnergyUiElementTemplate(TEXTURE_MAP,
                9, 72, 126, 0, 198, 0, 238, 0,
                new float[]{.33f, 1f, .63f, 1f}, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        /**
         * An energy meter four slots high and a quarter of a slot wide (ish).
         */
        public static final EnergyUiElementTemplate ENERGY_FOUR_QUARTER = new EnergyUiElementTemplate(TEXTURE_MAP,
                5, 72, 126, 0, 207, 0, 238, 0,
                new float[]{.33f, 1f, .63f, 1f}, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        /**
         * An energy meter two slots high and the width of half a slot.
         */
        public static final EnergyUiElementTemplate ENERGY_TWO_HALF = new EnergyUiElementTemplate(TEXTURE_MAP,
                9, 36, 126, 0, 212, 0, 247, 0,
                new float[]{.33f, 1f, .63f, 1f}, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        /**
         * An energy meter two slots high and a quarter of a slot wide (ish).
         */
        public static final EnergyUiElementTemplate ENERGY_TWO_QUARTER = new EnergyUiElementTemplate(TEXTURE_MAP,
                5, 36, 126, 0, 221, 0, 247, 0,
                new float[]{.33f, 1f, .63f, 1f}, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        /**
         * An energy meter the height of a quarter slot (ish) and as wide as the player inventory slots (9 slots).
         */
        public static final EnergyUiElementTemplate ENERGY_QUARTER_NINE = new EnergyUiElementTemplate(TEXTURE_MAP,
                162, 5, 63, 82, 63, 72, 63, 77,
                new float[]{.33f, 1f, .63f, 1f}, FillingUiElementTemplate.FillDirection.LEFT_TO_RIGHT);

        // Miscellaneous

        /**
         * The static recipe arrow used in vanilla guis such as the anvil.
         */
        public static final StaticUiElementTemplate RECIPE_ARROW = new StaticUiElementTemplate(TEXTURE_MAP, 23, 16, 0, 176);

        /**
         * The static recipe arrow used in vanilla guis such as the anvil, flipped to face left.
         */
        public static final StaticUiElementTemplate RECIPE_ARROW_LEFT = new StaticUiElementTemplate(TEXTURE_MAP, 23, 16, 46, 176);

        /**
         * The static recipe arrow with a red X used in vanilla guis such as the anvil.
         */
        public static final StaticUiElementTemplate RECIPE_ARROW_DISALLOWED = new StaticUiElementTemplate(TEXTURE_MAP, 23, 16, 0, 192);

        /**
         * The recipe arrow used in vanilla furnaces, filling from left to right.
         */
        public static final FillingUiElementTemplate<FillingUiElementTemplate.FillLevel> RECIPE_ARROW_FILLING = new FillingUiElementTemplate<>(TEXTURE_MAP,
                23, 16, 0, 176, 23, 176, FillingUiElementTemplate.FillDirection.LEFT_TO_RIGHT);

        /**
         * A togglable recipe arrow that displays a red X when the boolean context is false (similar to the anvil's) and otherwise draws based on the passed fill level
         */
        public static final BooleanUiElementTemplate<FillingUiElementTemplate.FillLevel, Void> RECIPE_ARROW_FILLING_TOGGLABLE = new BooleanUiElementTemplate<>(
                RECIPE_ARROW_FILLING, RECIPE_ARROW_DISALLOWED);

        /**
         * The burn time indicator used in vanilla furnaces.
         */
        public static final FillingUiElementTemplate<FillingUiElementTemplate.FillLevel> BURN_TIME_INDICATOR = new FillingUiElementTemplate<>(TEXTURE_MAP,
                14, 14, 0, 162, 14, 162, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        /**
         * The bubble progress indicator used in vanilla brewing stands.
         */
        public static final FillingUiElementTemplate<FillingUiElementTemplate.FillLevel> BUBBLE_PROGRESS_BAR = new FillingUiElementTemplate<>(TEXTURE_MAP,
                10, 28, 0, 134, 10, 134, FillingUiElementTemplate.FillDirection.BOTTOM_TO_TOP);

        /**
         * The downward facing progress arrow used in vanilla brewing stands.
         */
        public static final FillingUiElementTemplate<FillingUiElementTemplate.FillLevel> DOWNWARD_PROGESS_ARROW = new FillingUiElementTemplate<>(TEXTURE_MAP,
                8, 27, 21, 134, 29, 134, FillingUiElementTemplate.FillDirection.TOP_TO_BOTTOM);

    }

}
