# Changelog
Changes to the Nifty API and implementation that are of note will be documented here.
Please document your changes in this document if you are opening a pull request.

## v1.0.1
### Breaking
* `InputWidget` and related classes have been renamed around `Gizmo` to avoid confusion with the
  built-in GUI system.

### Added
* `MutableItemStorage`, `ItemStorageContainer` for item syncing with menus
* `FluidStackField` for syncing fluids with `ContainerFields`
* `FluidStackGizmo` for rendering fluid tanks
* `TooltipStyle` for customizing a Gizmo's tooltip

### Extra
* Added automatic Github release workflow
* Tweaked push/pr workflows
  * Renamed `Gradle-Build` jobs to `gradle`
  * Removed Javadoc deployment, moved to release workflow

## v1.0.0
### Added
* Initial mod structure, services, etc
* Aspects
* Transfer API
* `InputWidget` system
* `SimpleChannel` networking system