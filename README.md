# Nifty

A cross-platform library mod for UI, networking, machinery, transfer and more.

## Features

- Aspects
    - System that looks something like a cross between Forge's capabilities and the Fabric API lookups
    - Very similar implementation to capabilities; blockentities etc can implement an interface to provide an aspect
    - The aspects are then wrapped in a lazy optional, much like forge, and recognize when they are invalidated
- Fully featured simulation-based transfer system for items, fluids, and energy
    - Simple handler implementations for use in your own systems
    - FluidStack system that gets wrapped on Forge and defers to FluidVariants on Fabric
    - Wrappers on both Forge and Fabric
    - Full mod support: on Forge, uses default item, fluid and FE systems; on Fabric, uses Fabric API transfer systems
      and Team Reborn's Energy API
    - Also includes abstracted implementations for simple transfer of your own custom types
- Component UI sytem (InputWidgets) stolen from my API in Logic Networks, which will eventually be swapped to depend on
  this one
- Some rendering helper methods
- Base blockentity, menu, and block implementations simplifying machine creation
- Platform information and utilities, accessors, etc
- Networking system for simple packet registration and handling, reminiscent of Forge's SimpleImpl
- More in the works!

## Usage

[wip]

## Extra

Feel free to use Nifty in any of your own projects, as a mod dependency or within a modpack. See the LICENSE file for
more information. Contribution is also heavily appreciated; if you find Nifty is lacking a feature you think would fit,
let us know in the issues.