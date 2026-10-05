# Nametag Depth Fix

An exclusively client-side Fabric mod for Minecraft Java 26.3 that fixes
[MC-162693](https://bugs.mojang.com/browse/MC-162693).

Minecraft 26.3 is distributed without obfuscation, so the source uses Mojang's
official names directly. Loom rejects an additional mappings layer in this
environment because none is needed.

## Technical cause

Vanilla submits normal depth-tested nametags to the `nameTags` phase. When a
player is crouching, the nametag's translucent background can write to the depth
buffer before entities, items, blocks, or other translucent geometry behind it.
As a result, that geometry disappears within the transparent area of the
nametag.

The mixin redirects only the `nameTags` field read in
`SubmitNodeCollection.submitNameTagPart` to `afterTerrain`. It does not modify
`seeThrough`, so crouching players' nametags remain hidden behind solid blocks
and vanilla nametags without depth testing retain their original behavior. With
Improved Transparency enabled, `nameTags` and `afterTerrain` already share the
same OIT phase, making the change neutral.

## Building

Java 25 is required.

```powershell
./gradlew build
```

The production JAR is generated at
`build/libs/nametag-depth-fix-1.0-SNAPSHOT.jar`.

## Manual two-client test

1. Start a multiplayer server and connect two clients. Install the mod only on
   the observing client.
2. Place the second player in front of an entity or translucent geometry and
   have that player crouch.
3. Look at the entity through the transparent area of the nametag. The entity
   must remain visible.
4. Place a solid block between the observing client and the crouching player.
   The crouching nametag must be hidden.
5. Repeat without crouching and confirm that the nametag retains its vanilla
   behavior.
6. Repeat the cases with Improved Transparency both enabled and disabled.
7. Repeat with a renamed mob and scoreboard text.
