package design.aeonic.nifty;

import design.aeonic.nifty.api.aspect.Aspects;
import design.aeonic.nifty.api.client.RenderHelper;
import design.aeonic.nifty.api.util.Access;
import design.aeonic.nifty.api.core.PlatformInfo;
import design.aeonic.nifty.api.util.Wrappers;
import design.aeonic.nifty.api.energy.EnergyHandler;
import design.aeonic.nifty.api.item.FluidHandler;
import design.aeonic.nifty.api.item.ItemHandler;
import design.aeonic.nifty.api.registry.Registrar;
import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.api.util.Services;
import net.minecraft.resources.ResourceLocation;

public class Nifty {

    /**
     * Information about the current platform and loaded mods.
     */
    public static final PlatformInfo PLATFORM = Services.load(PlatformInfo.class);
    /**
     * Registry hooks.
     */
    public static final Registrar REGISTRY = Services.load(Registrar.class);
    /**
     * Factories and accessible Vanilla methods you might not be able to make without mixins/AWs, and for platform-specific
     * implementations of some Nifty things (ie item handlers, etc).
     */
    public static final Access ACCESS = Services.load(Access.class);
    /**
     * The Aspect system, which bridges the gap between Forge caps and the Fabric API lookup system.
     */
    public static final Aspects ASPECTS = Services.load(Aspects.class);
    /**
     * Includes methods for wrapping certain Nifty objects in a platform-specific implementation for compatibility with
     * existing systems (ie item and other transfer handlers, etc)
     */
    public static final Wrappers WRAPPERS = Services.load(Wrappers.class);
    /**
     * Includes miscellaneous helper methods for various rendering tasks.
     */
    public static final RenderHelper RENDER_HELPER = Services.load(RenderHelper.class);

    public static void init() {
        // Default Aspects
        ASPECTS.registerAspect(new ResourceLocation(Constants.NIFTY_ID, "item"), ItemHandler.class);
        ASPECTS.registerAspect(new ResourceLocation(Constants.NIFTY_ID, "fluid"), FluidHandler.class);
        ASPECTS.registerAspect(new ResourceLocation(Constants.NIFTY_ID, "energy"), EnergyHandler.class);
    }

    public static void clientInit() {

    }

}
