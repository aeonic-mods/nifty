package design.aeonic.nifty.impl.platform;

import design.aeonic.nifty.api.platform.PlatformInfo;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class ForgePlatformInfo implements PlatformInfo {

    @Override
    public Platform getPlatform() {
        return Platform.FORGE;
    }

    @Override
    public Side getPhysicalSide() {
        return FMLLoader.getDist().isClient() ? Side.CLIENT : Side.SERVER;
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }
}
