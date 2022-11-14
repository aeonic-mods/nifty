package design.aeonic.nifty.impl.services;

import design.aeonic.nifty.api.services.PlatformInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformInfo implements PlatformInfo {

    @Override
    public Platform getPlatform() {
        return Platform.NOT_FORGE;
    }

    @Override
    public Side getPhysicalSide() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT ? Side.CLIENT : Side.SERVER;
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
