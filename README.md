# Away Digest

> Log back in and find out what you missed — how long you were gone and what the others got up to.

## What it does

On login, if you've been here before, you get a short recap:

```
Welcome back — you were away about 18 hour(s).
While you were away, others earned 3 advancement(s):
  Alice earned "We Need to Go Deeper"
  Bob earned "Free the End"
  Alice earned "Sky's the Limit"
```

Tracks each player's logout time and a rolling list of advancements earned (who + what + when), all **persisted across restarts**. No command, no setup — it just greets you.

## Why

The "2-week server problem" — SMPs going quiet after the initial burst — is a chronic r/admincraft topic. A small "the world kept living without you" recap helps a long-running server feel alive.

## Install

Drop `awaydigest-<version>.jar` into `mods/`. Server-side. No dependencies.

- Minecraft 1.21.1 · NeoForge · JDK 21

## Scope

Login/logout + advancement listeners + persistent world data. No mixin, no config, no command, no blocks/items. v0.1 covers away-time and others' advancements (deliberately minimal). 9 languages (machine-baseline; native PRs welcome).

## License

MIT — modpack inclusion welcome, no credit required.

Author: KURONAMI
