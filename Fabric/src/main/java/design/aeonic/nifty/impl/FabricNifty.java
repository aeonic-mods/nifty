package design.aeonic.nifty.impl;

import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.api.services.Aspects;
import design.aeonic.nifty.api.transfer.Transfer;
import design.aeonic.nifty.api.util.Registrar;
import design.aeonic.nifty.impl.services.FabricAspects;
import design.aeonic.nifty.impl.transfer.energy.FabricEnergyBattery;
import design.aeonic.nifty.impl.transfer.energy.NiftyEnergyStorage;
import design.aeonic.nifty.impl.transfer.fluid.FabricFluidStorage;
import design.aeonic.nifty.impl.transfer.fluid.NiftyFluidVariantStorage;
import design.aeonic.nifty.impl.transfer.item.FabricItemStorage;
import design.aeonic.nifty.impl.transfer.item.NiftyItemVariantStorage;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import team.reborn.energy.api.EnergyStorage;

@SuppressWarnings("UnstableApiUsage")
public class FabricNifty implements ModInitializer {

    public static void onInitializeClient() {
        ClientNifty.clientInit(Minecraft.getInstance()::submit);
//        LogicNetworks.clientInit(Minecraft.getInstance()::submit);
//        NetworkKeybinds.register(($, mapping) -> KeyBindingHelper.registerKeyBinding(mapping));
//        Minecraft.getInstance().submit(() -> {
//            NetworkBlockEntityRenderers.register(BlockEntityRendererRegistry::register);
//            NetworkItemProperties.register(ItemProperties::register);
//        });
    }

    @Override
    public void onInitialize() {
        Nifty.init();

        FabricAspects aspects = (FabricAspects) Aspects.INSTANCE;
        aspects.registerMapped(Transfer.ITEM, Transfer.ITEM_ASPECT, Storage.asClass(), NiftyItemVariantStorage::new, FabricItemStorage::new);
        aspects.registerMapped(Transfer.FLUID, Transfer.FLUID_ASPECT, Storage.asClass(), NiftyFluidVariantStorage::new, FabricFluidStorage::new);
        aspects.registerMapped(Transfer.ENERGY, Transfer.ENERGY_ASPECT, EnergyStorage.class, NiftyEnergyStorage::new, FabricEnergyBattery::new);
    }

    <T> Registrar<T> registrar(Registry<T> registry) {
        return Registrar.of(Constants.MOD_ID, (key, value) -> Registry.register(registry, key, value));
    }
}
