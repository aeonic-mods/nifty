package design.aeonic.nifty.api.core;

import design.aeonic.nifty.api.aspect.Aspects;
import design.aeonic.nifty.api.networking.Networking;
import design.aeonic.nifty.api.platform.PlatformAccess;
import design.aeonic.nifty.api.platform.PlatformInfo;

import java.util.ServiceLoader;

public class Services {
    /**
     * Contains information about the current platform and (eventually) loaded mods.
     */
    public static final PlatformInfo PLATFORM = load(PlatformInfo.class);
    /**
     * Provides access to platform-specific functionality or Vanilla code that the platform exposes.
     */
    public static final PlatformAccess ACCESS = load(PlatformAccess.class);
    /**
     * Aspects! Like capabilities, but not.
     */
    public static final Aspects ASPECTS = load(Aspects.class);
    /**
     * Provides networking functionality, packets etc.
     */
    public static final Networking NETWORKING = load(Networking.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
