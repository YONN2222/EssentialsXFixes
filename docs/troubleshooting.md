<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="../assets/icons/help-dark.svg">
    <img src="../assets/icons/help-light.svg" alt="" width="96">
  </picture>
</p>

<h1 align="center">Troubleshooting</h1>

<p align="center">Common questions and fixes.</p>

<p align="center"><a href="README.md">All docs</a></p>

## The plugin disables itself

EssentialsX has to be installed and enabled. XFixes logs `EssentialsX is not enabled` and turns itself off otherwise.

## A selector is printed as text

The player needs `essentialsxfixes.selectors`, and `selectors.enabled` has to be `true`. Selector text inside free text arguments such as the message of `/msg` is never expanded.

## /tp with coordinates says I have no permission

XFixes uses the EssentialsX permission of the command, here `essentials.tp`. Give it to the player or group.

## /return does not work like vanilla

EssentialsX makes `/return` an alias of `/back`. XFixes does not change that. In functions and `/execute` the vanilla `return` still works.

## A command does not use the new functions

Check `extensions.<command>` in `config.yml` and that the label is not listed in `exclude`. Only commands that EssentialsX really handles are taken over. If another plugin or `overridden-commands` in the EssentialsX config changes who owns a label, XFixes follows that.

## "Version mismatch" in the log

That message comes from plugins whose name starts with Essentials. XFixes is named `XFixes` so it does not appear.

## Spigot or Folia

Not supported. The plugin needs the Paper command API.
