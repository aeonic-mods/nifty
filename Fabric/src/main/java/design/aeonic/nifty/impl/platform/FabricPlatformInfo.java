package design.aeonic.nifty.impl.platform;

import design.aeonic.nifty.api.platform.ModInfo;
import design.aeonic.nifty.api.platform.PlatformInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Optional;

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
