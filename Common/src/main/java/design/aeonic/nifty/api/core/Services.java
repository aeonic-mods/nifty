package design.aeonic.nifty.api.core;

import design.aeonic.nifty.api.aspect.Aspects;
import design.aeonic.nifty.api.networking.Networking;
import design.aeonic.nifty.api.services.PlatformAccess;
import design.aeonic.nifty.api.services.PlatformInfo;

import java.util.ServiceLoader;

public class Services {
    public static final PlatformInfo PLATFORM = load(PlatformInfo.class);
    public static final PlatformAccess ACCESS = load(PlatformAccess.class);
    public static final Aspects ASPECTS = load(Aspects.class);
    public static final Networking NETWORKING = load(Networking.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
