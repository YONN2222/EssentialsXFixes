<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="../assets/icons/config-dark.svg">
    <img src="../assets/icons/config-light.svg" alt="" width="96">
  </picture>
</p>

<h1 align="center">Configuration reference</h1>

<p align="center">Every option and its default.</p>

<p align="center"><a href="README.md">All docs</a></p>

Apply changes with `/essentialsxfixes reload`. The list of taken over command labels is built at startup, so a change of `exclude` needs a restart.

| Option | Default | About |
|---|---|---|
| `language` | `en` | Language file from `plugins/XFixes/lang` without `.yml` |
| `selectors.enabled` | `true` | Resolve target selectors in EssentialsX commands |
| `extensions.<command>` | `true` | Add the vanilla functions to that command. Commands: `tp`, `kill`, `clear`, `enchant`, `give`, `item`, `recipe`, `list`, `xp`, `time`, `weather` |
| `exclude` | `[]` | Command labels this plugin never takes over |

With an extension switched off, the command works like plain EssentialsX again, selectors still apply.
