# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]

### Added
* `RecipeIngredient` and implementations for more modular item, fluid ingredients etc.

## [v1.0.1] - 2022-11-16

### Changed

* `InputWidget` and related classes have been renamed around `Gizmo` to avoid confusion with the
  built-in GUI system.
* The `services` package has been renamed to `platform` since most service APIs are not contained there anyway and there
  are several non-service classes in the package.

### Added

* `MutableItemStorage`, `ItemStorageContainer` for item syncing with menus
* `FluidStackField` for syncing fluids with `ContainerFields`
* `FluidStackGizmo` for rendering fluid tanks, and fluid rendering methods in `RenderUtils`
* `TooltipStyle` for customizing a Gizmo's tooltip

## [v1.0.0]

### Added

* Initial mod structure, services, etc
* Aspects
* Transfer API
* `InputWidget` system
* `SimpleChannel` networking system
