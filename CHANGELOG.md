# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

## [v1.1.0]

### Added
* `RecipeIngredient` and implementations for more modular item, fluid ingredients etc.
* `RecipeOutput`, `RecipeOutputSet`, and item/fluid implementations
* `ModularRecipe` for recipes that can input/output fluids and items, and can require processing times/energy consumption
* `FluidStack#getTooltip`, along with a per-instance tooltip cache that is only created when it's queried for the first time.
* `FluidStack#setTag`, because I guess I forgot it before oops
* `Drawable` and its subclasses for configurable renderable things that aren't Gizmos - eventually Gizmos will be restructured to use this interface
* `Drawables` with a bunch of convenient drawable instances, such as slots, recipe arrows (with filling variants), burn indicators etc.

### Changed
* Moved some rendering things around, like `Texture` - now found among drawables.
* Some refactoring with gizmos to account for the above additions and changes.

## [v1.0.1] - 2022-11-16

### Added

* `MutableItemStorage`, `ItemStorageContainer` for item syncing with menus
* `FluidStackField` for syncing fluids with `ContainerFields`
* `FluidStackGizmo` for rendering fluid tanks, and fluid rendering methods in `RenderUtils`
* `TooltipStyle` for customizing a Gizmo's tooltip

### Changed

* `InputWidget` and related classes have been renamed around `Gizmo` to avoid confusion with the
  built-in GUI system.
* The `services` package has been renamed to `platform` since most service APIs are not contained there anyway and there
  are several non-service classes in the package.

## [v1.0.0] - 2022-11-15

### Added

* Initial mod structure, services, etc
* Aspects
* Transfer API
* `InputWidget` system
* `SimpleChannel` networking system
