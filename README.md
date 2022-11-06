# Nifty
A cross-platform library mod for UI, networking, machinery, transfer and more.

## Planned features
- Aspects
    - System that looks something like a cross between Forge's capabilities and the Fabric API lookups
    - Very similar implementation to capabilities; blockentities etc can implement an interface to provide an aspect
    - The aspects are then wrapped in a lazy optional, much like forge, and recognize when they are invalidated
- Component UI, steal from Logic Networks