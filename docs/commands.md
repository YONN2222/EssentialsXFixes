<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="../assets/icons/functions-dark.svg">
    <img src="../assets/icons/functions-light.svg" alt="" width="96">
  </picture>
</p>

<h1 align="center">Vanilla functions</h1>

<p align="center">What each EssentialsX command gained.</p>

<p align="center"><a href="README.md">All docs</a></p>

EssentialsX keeps its simple syntax. Everything it already handles is still handled by EssentialsX. Only input that EssentialsX cannot handle is carried out by XFixes, with its own code and in the same command. No vanilla command is ever called.

The sender needs the same permission EssentialsX uses for that command, for example `essentials.tp`.

| Command | Added by XFixes |
|---|---|
| `/tp` | Coordinates `100 64 100`, relative `~` and local `^` positions, yaw and pitch, `facing <x y z>` and `facing entity <target>`. Works in the dimension and position set by `/execute in`, `at` and `positioned`. Entities as targets and as destination |
| `/kill` | `/kill` for yourself and selectors that match any entity |
| `/clear` | Item tags like `#minecraft:logs`, items with components, and a count of `0` to only count |
| `/enchant` | `/enchant <targets> <enchantment> [level]` for any living entity |
| `/give` | Items with components like `diamond_sword[enchantments={sharpness:5}]` |
| `/item` | `/item replace entity <targets> <slot> with <item> [count]` |
| `/recipe` | `/recipe give <targets> <recipe or *>` and `/recipe take ...` |
| `/list` | `/list uuids` |
| `/xp` | `/xp add\|set\|query <targets> <amount> [points\|levels]` |
| `/time` | `set`, `add` and `query` with `d`, `s` and `t` units and the names `day`, `noon`, `night`, `midnight`, in the world of the command source |
| `/weather` | `clear`, `rain` and `thunder` with an optional duration, for example `600`, `30s` or `1d` |

Every EssentialsX command also understands [target selectors](selectors.md).

## Permissions

| Permission | Allows | Default |
|---|---|---|
| `essentialsxfixes.selectors` | Using target selectors in EssentialsX commands | everyone |
| `essentialsxfixes.reload` | `/essentialsxfixes reload` | operators |

All other permissions are the ones from EssentialsX. The administration command is `/essentialsxfixes`, alias `/exfixes`.

## What is not covered

- `item modify` and copying items between containers
- `/clear` with components matches the whole component set of the item, not single components
- `execute store` and `execute if` get the number of successful runs as the result
