[![Mod Loader: Fabric](https://img.shields.io/static/v1?label=modloader&message=fabric&color=brightgreen)](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
![Mod Environment](https://img.shields.io/static/v1?label=environment&message=client%2Fserver&color=yellow)
![Version](https://cf.way2muchnoise.eu/versions/quartz-elevator.svg)
[![CurseForge](https://cf.way2muchnoise.eu/quartz-elevator.svg)](https://www.curseforge.com/minecraft/mc-mods/quartz-elevator)
[![Modrinth](https://img.shields.io/modrinth/dt/O9C0qdu8?color=%2300AF5C&logo=modrinth)](https://modrinth.com/mod/quartz-elevator)
[![MIT License](https://img.shields.io/badge/license-MIT-blue.svg?style=flat)](./LICENSE)

# Quartz Elevator

The Quartz Elevator mod is inspired by
the [OpenBlocks Elevators](https://www.curseforge.com/minecraft/mc-mods/openblocks-elevator)
and the [Simple Elevators](https://www.curseforge.com/minecraft/mc-mods/fabric-simple-elevators).  
Adds Quartz Elevator and Smooth Quartz Elevator.

You can teleport between elevators with the same (x, z) coordinates: jump to go up and crouch to go down.

## Requires

The Quartz Elevator mod requires:

- [**Fabric Loader**](https://fabricmc.net/use/)
- [**Fabric API**](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

## Recipe

### Quartz Elevator

1. [Crafting] Nether Quartz x4 + Ender Pearl x1 (shaped crafting like the below image)

   ![quartz elevator recipe1](https://user-images.githubusercontent.com/38515249/106385105-cce95f00-6411-11eb-94bc-a3062db8397d.png)

2. [Crafting] Brock of Quartz x1 + Ender Pearl x1 (shapeless crafting)

- can be mined using any pickaxe. If mined without a pickaxe, it drops nothing.

### Smooth Quartz Elevator

1. [Crafting] Smooth Quartz Block x1 + Ender Pearl x1 (shapeless crafting)

2. [Smelting] Quartz Elevator x1 + any fuel

- can be mined using any pickaxe. If mined without a pickaxe, it drops nothing.

## Config

### Maximum teleport distance

|                        | Max Distance |
| ---------------------- | ------------ |
| Quartz Elevator        | 16           |
| Smooth Quartz Elevator | 64           |

These are default values and can be configurable.

### Mix types

You cannot move between different elevator types by default, but can be configurable.

### Entity filter

`Player Only` value is `false` by default.  
If `Player Only` is `true`, only player entities can teleport.

### ModMenu Integration

The above values can be configured with the [ModMenu](https://www.curseforge.com/minecraft/mc-mods/modmenu) GUI screen.

![ModMenu](https://user-images.githubusercontent.com/38515249/108022998-02728700-7065-11eb-990c-c0311c3d0b03.png)

![Config Screen](https://user-images.githubusercontent.com/38515249/108022992-00a8c380-7065-11eb-829a-8f4165913b10.png)

## License

The Quartz Elevator mod is licensed under the MIT License, see [LICENSE](./LICENSE).
