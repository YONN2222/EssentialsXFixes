<p align="center">
  <img src="assets/banner.png" alt="EssentialsXFixes" width="100%">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21+">
  <img src="https://img.shields.io/badge/Paper%20%7C%20Purpur-2C2C2C?style=for-the-badge&logoColor=white" alt="Paper, Purpur">
  <img src="https://img.shields.io/badge/Minecraft-1.21+-DFAB40?style=for-the-badge&logoColor=white" alt="Minecraft 1.21+">
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-GPLv3-DF6440?style=for-the-badge" alt="GPLv3"></a>
</p>

<p align="center">
  <a href="https://modrinth.com/plugin/essentialsx-fixes">Modrinth</a>
  <a href="https://hangar.papermc.io/YONN2222/EssentialsX-Fixes">Hangar</a>
  <a href="https://github.com/YONN2222/EssentialsXFixes/releases">GitHub Releases</a>
</p>

> EssentialsX Fixes is a small open source addon for EssentialsX. EssentialsX replaces several vanilla commands with its own, simpler ones, and everything only the vanilla command could do stops working. This addon brings those functions back inside the same commands. Unofficial addon, not affiliated with or endorsed by the EssentialsX team.

Run `/execute in minecraft:the_end run tp @s 100 100 100` with EssentialsX alone and you end up in the wrong world. Run `/xp add Steve 10 levels` and you get an error. Use `@a` in the console or a command block and nothing happens. With EssentialsX Fixes all of that works, and the EssentialsX syntax stays exactly as it was. No vanilla command is ever called, the plugin does the work itself.

## Before and after

| Command | With EssentialsX alone | With EssentialsX Fixes |
|---|---|---|
| `/execute in minecraft:the_end run tp @s 100 100 100` | lands in the wrong world | lands in the End |
| `/tp @s ~ ~10 ~` | not understood | coordinates, `~`, `^`, rotation and `facing` |
| `/xp add Steve 10 levels` | error | `add`, `set` and `query` in points or levels |
| `/gamemode creative @a` | only player names | every selector with all filters |
| `/give Steve diamond_sword[enchantments={sharpness:5}]` | no components | components work |
| `/clear Steve #minecraft:logs` | no tags | item tags, components and count `0` |
| `/time set 6d`, `/weather rain 5m` | no units or duration | units and durations |
| `execute as Steve run gamemode creative` | hits the console | hits Steve |

## Features

- Target selectors `@a @p @r @s @e @n` with all filters in every EssentialsX command
- Works from the console, command blocks and inside `/execute`, EssentialsX and vanilla commands can be mixed
- `/tp` with coordinates, `~` and `^`, rotation, `facing` and entities, in the dimension set by `/execute in`
- `/xp add`, `set` and `query`, `/time` and `/weather` with units, `/list uuids`
- `/give`, `/clear`, `/item`, `/enchant` and `/recipe` with components, tags and slots
- EssentialsX keeps handling everything it can handle itself, same syntax and same permissions
- Tab completion for the added functions
- 10 languages and every message is editable
- Each addition can be switched off in `config.yml`

## Requirements

- Paper or a fork (Purpur and similar), Minecraft 1.21 or newer
- EssentialsX
- Java 21 or newer

Spigot and Folia are not supported.

## Quick start

1. Put the jar into `plugins` next to EssentialsX and start the server.
2. Run `/execute in minecraft:the_end run tp @s 100 100 100` to try it.

No setup needed. Permissions are the ones from EssentialsX plus `essentialsxfixes.selectors`, which everybody has by default. See [Installation](docs/installation.md) for details.

## What it does not change

EssentialsX stays in charge of everything it can handle itself. The plugin only steps in when the input needs a vanilla function EssentialsX does not have, for example a selector, a coordinate or a unit. `/return` is left alone because EssentialsX uses it as an alias of `/back`.

## Documentation

| | Topic | About |
|---|---|---|
| <picture><source media="(prefers-color-scheme: dark)" srcset="assets/icons/install-dark.svg"><img src="assets/icons/install-light.svg" alt="" width="40"></picture> | [Installation](docs/installation.md) | Requirements and first steps |
| <picture><source media="(prefers-color-scheme: dark)" srcset="assets/icons/functions-dark.svg"><img src="assets/icons/functions-light.svg" alt="" width="40"></picture> | [Vanilla functions](docs/commands.md) | What each EssentialsX command gained |
| <picture><source media="(prefers-color-scheme: dark)" srcset="assets/icons/selectors-dark.svg"><img src="assets/icons/selectors-light.svg" alt="" width="40"></picture> | [Target selectors](docs/selectors.md) | Selectors with filters, execute and command blocks |
| <picture><source media="(prefers-color-scheme: dark)" srcset="assets/icons/config-dark.svg"><img src="assets/icons/config-light.svg" alt="" width="40"></picture> | [Configuration reference](docs/configuration.md) | Every option and its default |
| <picture><source media="(prefers-color-scheme: dark)" srcset="assets/icons/languages-dark.svg"><img src="assets/icons/languages-light.svg" alt="" width="40"></picture> | [Languages](docs/languages.md) | Ten languages, custom texts, placeholders |
| <picture><source media="(prefers-color-scheme: dark)" srcset="assets/icons/help-dark.svg"><img src="assets/icons/help-light.svg" alt="" width="40"></picture> | [Troubleshooting](docs/troubleshooting.md) | Common problems |

## Building

```
./gradlew build
```

The jar ends up in `build/libs`. Java 25 is used to build, the plugin itself targets Java 21.

## Privacy

EssentialsXFixes reports anonymous usage statistics to [bStats](https://bstats.org/plugin/bukkit/XFixes/34778). Turn it off for all plugins in `plugins/bStats/config.yml`. Nothing else leaves your server.

## License

GPL-3.0, see [LICENSE](LICENSE). EssentialsX is a separate project by the EssentialsX team and is not part of this plugin.
