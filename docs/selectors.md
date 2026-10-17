<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="../assets/icons/selectors-dark.svg">
    <img src="../assets/icons/selectors-light.svg" alt="" width="96">
  </picture>
</p>

<h1 align="center">Target selectors</h1>

<p align="center">Selectors for every EssentialsX command.</p>

<p align="center"><a href="README.md">All docs</a></p>

Every EssentialsX command can take `@a`, `@p`, `@r`, `@s`, `@e` and `@n` with all vanilla filters, for example `@a[gamemode=survival,distance=..20]` or `@e[type=zombie,limit=3,sort=nearest]`.

```
/gamemode creative @a[team=builders]
/give @a[level=10..] diamond 2
/msg @p[distance=..5] hello
/tp @a @p
```

The selector is resolved by the server in the context of the command source. That means:

- it works from the console and from command blocks
- it works inside `/execute`, including `execute as`, `at`, `positioned` and `in`
- `@s` is the entity that `/execute as` selected
- EssentialsX and vanilla commands can be mixed in one `/execute` chain

If a selector matches several players, the command runs once for each of them. For arguments that need exactly one target, for example the destination of `/tp`, the selector has to match exactly one.

If a command needs a player and none is given, `/execute as <player> run gamemode creative` uses that player, the same way vanilla commands do. This works for `gamemode`, `speed`, `heal`, `feed`, `fly`, `god`, `clearinventory`, `vanish` and `extinguish`.

Selectors are not expanded in free text, so `/broadcast hello @a` and the text part of `/msg`, `/me` and `/kick` stay untouched.

Players need `essentialsxfixes.selectors`. Without it the text is passed to EssentialsX unchanged.
