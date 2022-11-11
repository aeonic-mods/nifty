package design.aeonic.nifty.impl;

import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.api.services.Aspects;
import design.aeonic.nifty.api.transfer.Transfer;
import design.aeonic.nifty.api.util.Registrar;
import design.aeonic.nifty.impl.services.ForgeAspects;
import design.aeonic.nifty.impl.transfer.energy.ForgeEnergyBattery;
import design.aeonic.nifty.impl.transfer.energy.NiftyEnergyStorage;
import design.aeonic.nifty.impl.transfer.fluid.ForgeFluidStorage;
import design.aeonic.nifty.impl.transfer.fluid.NiftyFluidHandler;
import design.aeonic.nifty.impl.transfer.item.ForgeItemStorage;
import design.aeonic.nifty.impl.transfer.item.NiftyItemHandler;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class ForgeNifty {
    
    public ForgeNifty() {
        Nifty.init();
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener((FMLClientSetupEvent event) -> {
            ClientNifty.clientInit(event::enqueueWork);
        });

        modBus.addListener(((ForgeAspects) Aspects.INSTANCE)::registerCapabilities);

        ForgeAspects aspects = (ForgeAspects) Aspects.INSTANCE;
        aspects.registerMapped(Transfer.ITEM, Transfer.ITEM_ASPECT, ForgeCapabilities.ITEM_HANDLER, NiftyItemHandler::new, ForgeItemStorage::new);
        aspects.registerMapped(Transfer.FLUID, Transfer.FLUID_ASPECT, ForgeCapabilities.FLUID_HANDLER, NiftyFluidHandler::new, ForgeFluidStorage::new);
        aspects.registerMapped(Transfer.ENERGY, Transfer.ENERGY_ASPECT, ForgeCapabilities.ENERGY, NiftyEnergyStorage::new, ForgeEnergyBattery::new);
    }

    <T> Registrar<T> registrar(RegisterEvent event, ResourceKey<? extends Registry<T>> registry) {
        return Registrar.of(Constants.MOD_ID, (key, value) -> event.register(registry, key, () -> value));
    }
}