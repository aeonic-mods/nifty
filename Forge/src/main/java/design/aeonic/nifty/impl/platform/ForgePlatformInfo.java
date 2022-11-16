package design.aeonic.nifty.impl.platform;

import design.aeonic.nifty.api.platform.ModInfo;
import design.aeonic.nifty.api.platform.PlatformInfo;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.Optional;

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
    public Optional<ModInfo> getModInfo(String modId) {
        return ModList.get().getModContainerById(modId).map(ForgeModInfo::new);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
}
