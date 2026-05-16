# Away Digest

> Log back in and find out what you missed — how long you were gone and what the others got up to.

The "2-week server problem" — SMPs going quiet after the initial burst — is a chronic r/admincraft topic. A small "the world kept living without you" recap helps a long-running server feel alive.

- 👋 On login: how long you were away
- 🏆 The advancements other players earned while you were gone
- 💾 Persists across restarts
- 🧩 No command, no setup — it just greets you

## What it does / Usage

```
Welcome back — you were away about 18 hour(s).
While you were away, others earned 3 advancement(s):
  Alice earned "We Need to Go Deeper"
  Bob earned "Free the End"
  Alice earned "Sky's the Limit"
```

Tracks each player's logout time and a rolling list of earned advancements.

## Supported loaders / versions

| Minecraft | NeoForge | Forge | Fabric |
|---|:---:|:---:|:---:|
| 1.21.1 | ✅ | planned | planned |

Forge / Fabric / 1.20.1 ports planned; this release is NeoForge 1.21.1.

## Dependencies

None.

## Compatibility & scope

Server-side: login/logout + advancement listeners + persistent world data. No mixin, no config, no command, no blocks/items.

## Known limitations

v0.1 recap = away-time + other players' advancements only (deliberately minimal). It shines on a multiplayer SMP; single-player just shows the away-time greeting (no other players to report).

## Install

1. Install NeoForge for Minecraft 1.21.1.
2. Drop `awaydigest-0.1.0.jar` into `mods/`. Server-side.

- Minecraft 1.21.1 · NeoForge · JDK 21

## Languages

Output localized in 9 languages (machine-baseline; native-speaker PRs welcome).

## License

MIT — modpack inclusion welcome, no credit required.

Author: KURONAMI
