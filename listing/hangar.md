# EssentialsX Fixes (Addon)

![EssentialsXFixes](https://raw.githubusercontent.com/YONN2222/EssentialsXFixes/main/assets/banner.png)

![Java 21+](https://img.shields.io/badge/Java-21+-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Paper, Purpur](https://img.shields.io/badge/Paper%20%7C%20Purpur-2C2C2C?style=flat-square)
![Minecraft 1.21+](https://img.shields.io/badge/Minecraft-1.21+-DFAB40?style=flat-square)
![GPLv3](https://img.shields.io/badge/License-GPLv3-DF6440?style=flat-square)
[![Source code](https://img.shields.io/badge/Source%20code-GitHub-2C2C2C?style=flat-square&logo=github)](https://github.com/YONN2222/EssentialsXFixes)
[![Documentation](https://img.shields.io/badge/Documentation-Docs-DFAB40?style=flat-square)](https://github.com/YONN2222/EssentialsXFixes/tree/main/docs)
[![Issues](https://img.shields.io/badge/Issues-Report%20a%20bug-DF6440?style=flat-square)](https://github.com/YONN2222/EssentialsXFixes/issues)

EssentialsX replaces several vanilla commands with its own, simpler ones. The downside: whatever only the vanilla command could do no longer works with the EssentialsX command. `/execute in minecraft:the_end run tp @s 100 100 100` ends up in the wrong world, `/xp add` throws an error and `@a` does not work from the console or command blocks.

Unofficial addon, not affiliated with or endorsed by the EssentialsX team.

This addon keeps the simple EssentialsX commands and adds the missing functions to them. It never calls a vanilla command, it does the work itself. Requires EssentialsX and Paper (or a fork), Minecraft 1.21 and newer. Open source under GPLv3.

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

## Added functions

| Command | Added |
|---|---|
| `/tp` | coordinates, `~`, `^`, rotation, `facing`, entities |
| `/kill` | selectors for any entity |
| `/clear` | tags, components, count `0` |
| `/enchant` | targets and any living entity |
| `/give` | item components |
| `/item` | `replace entity` |
| `/recipe` | `give` and `take` |
| `/list` | `uuids` |
| `/xp` | `add`, `set`, `query` with points or levels |
| `/time` | `set`, `add`, `query` with units and names |
| `/weather` | `clear`, `rain`, `thunder` with duration |

## Permissions

| Permission | Allows | Default |
|---|---|---|
| `essentialsxfixes.selectors` | Target selectors in EssentialsX commands | everyone |
| `essentialsxfixes.reload` | `/essentialsxfixes reload` | operators |

All other permissions are the ones from EssentialsX.

## Languages

English, Chinese, Spanish, Hindi, French, Arabic, Portuguese, Russian, German and Japanese are included. Copy a file in `plugins/XFixes/lang` to make your own.

## Links

- [Documentation](https://github.com/YONN2222/EssentialsXFixes/tree/main/docs)
- [Source code](https://github.com/YONN2222/EssentialsXFixes)
- [Issues](https://github.com/YONN2222/EssentialsXFixes/issues)

EssentialsX Fixes sends anonymous usage statistics to bStats. Turn it off for all plugins in `plugins/bStats/config.yml`.
