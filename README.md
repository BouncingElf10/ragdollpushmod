Ragdoll Push Mod
=======

Adds pushing to [Sable: Player Ragdoll](https://github.com/Leo-T22/sable-player-ragdoll).

Aim at a player, press the push key, and they get launched as a ragdoll via
`RagdollAPI.launch(player, velocity)`. While a valid target is under your
crosshair a ring of sparks orbits them and their name appears under the
crosshair, so you can tell at a glance whether a push will land. Both cues can
be turned off independently, and mobs can be made pushable too — see Config.

Default key: **V** (rebindable under Controls → Ragdoll Push).

Setup
=======

Nothing manual — dependencies come from the
[Modrinth Maven](https://api.modrinth.com/maven) and are pinned by Modrinth
project/version id in `gradle.properties`:

| Dependency | Coordinates | Why |
| --- | --- | --- |
| Sable: Player Ragdoll 0.7.5 | `maven.modrinth:I3mWDgfy:CyKh8XSr` | Provides `RagdollAPI` |
| Sable 2.0.3 (NeoForge) | `maven.modrinth:T9PomCSv:1L6XJqnY` | Physics engine the above runs on |

Sable is declared explicitly rather than picked up transitively, because the
Modrinth Maven API does not serve dependency metadata. Note that Modrinth's own
dependency listing for Sable: Player Ragdoll 0.7.5 points at the *Fabric* build
of Sable; the id above is the NeoForge build, which is the one this project
needs. Neither jar is bundled into our output — both are all-rights-reserved
and are consumed as ordinary classpath dependencies.

To bump either one, replace the version id with a new one from that project's
Modrinth version list.

```
gradlew build          # produces build/libs/ragdollpushmod-<version>.jar
gradlew runClient      # dev client, with both mods loaded
```

Config
=======

`run/config/ragdollpushmod-common.toml`, or Mods → Ragdoll Push Mod → Config.

| Option | Default | Meaning |
| --- | --- | --- |
| `pushRange` | 6.0 | How far a target can be, in blocks |
| `pushStrength` | 4.0 | Launch speed along your look direction, m/s |
| `pushLift` | 1.0 | Extra upward launch speed, m/s |
| `pushCooldownTicks` | 20 | Ticks between pushes |
| `pushMobs` | false | Whether mobs can be pushed, not just players |
| `targetParticles` | true | Whether to show the spark ring on your target |
| `targetPrompt` | true | Whether to show the name tag under the crosshair |

Requirements
=======

Minecraft 1.21.1 · NeoForge 21.1.248+ · Sable: Player Ragdoll 0.7.5+ · Java 21
