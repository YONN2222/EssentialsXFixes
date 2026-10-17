<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="../assets/icons/install-dark.svg">
    <img src="../assets/icons/install-light.svg" alt="" width="96">
  </picture>
</p>

<h1 align="center">Installation</h1>

<p align="center">Get XFixes running in a minute.</p>

<p align="center"><a href="README.md">All docs</a></p>

## Requirements

- Paper or a fork of it (Purpur, Pufferfish and similar) on Minecraft 1.21 or newer
- [EssentialsX](https://essentialsx.net/) installed and enabled
- Java 21 or newer

Spigot and Folia are not supported. The plugin uses the Paper command API, which Spigot does not have.

## Steps

1. Download the jar from [Modrinth](https://modrinth.com/plugin/essentialsx-fixes), [Hangar](https://hangar.papermc.io/YONN2222/XFixes) or the [GitHub releases](https://github.com/YONN2222/EssentialsXFixes/releases).
2. Put it into the `plugins` folder next to EssentialsX.
3. Start the server. The plugin creates `plugins/XFixes/config.yml` and a `lang` folder.
4. Nothing else is needed. Players and staff keep the EssentialsX permissions they already have, see [Commands](commands.md#permissions).

The plugin is named `XFixes` on the server, so EssentialsX does not print a version mismatch warning for it.

## First test

Run this as an operator:

```
/execute in minecraft:the_end run tp @s 100 100 100
/gamemode creative @a
/xp add @s 5 levels
```

The first command moves you into the End. Without the plugin EssentialsX would teleport you inside your current world.
