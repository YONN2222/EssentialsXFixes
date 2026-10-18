<p align="center">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="../assets/icons/languages-dark.svg">
    <img src="../assets/icons/languages-light.svg" alt="" width="96">
  </picture>
</p>

<h1 align="center">Languages</h1>

<p align="center">Ten languages and your own texts.</p>

<p align="center"><a href="README.md">All docs</a></p>

English, Chinese, Spanish, Hindi, French, Arabic, Portuguese, Russian, German and Japanese are included. Hindi and Arabic are not rendered well by the default Minecraft font.

1. Set `language` in `config.yml` to one of `en`, `zh`, `es`, `hi`, `fr`, `ar`, `pt`, `ru`, `de`, `ja`.
2. Run `/essentialsxfixes reload`.

## Your own language

Copy a file in `plugins/XFixes/lang`, rename it, translate it and set its name as `language`. Messages use [MiniMessage](https://docs.advntr.dev/minimessage/format.html).

| Placeholder | Used in |
|---|---|
| `<label>` | errors, the command that was used |
| `<who>` | the player or entity name |
| `<where>`, `<count>`, `<item>`, `<what>`, `<slot>`, `<value>`, `<world>`, `<weather>`, `<name>`, `<uuid>` | the message that shows that value |

Missing keys fall back to English.
