<div align="center">

# 🔫 GunGame

**A GunGame minigame plugin for Minecraft Paper servers.**

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-21%2B-orange.svg)](https://adoptium.net/)
[![Paper](https://img.shields.io/badge/Paper-1.21.11%2B-blue.svg)](https://papermc.io/)
[![GitHub release](https://img.shields.io/github/v/release/MarcoHeckmann/GunGame)](https://github.com/MarcoHeckmann/GunGame/releases)
[![GitHub issues](https://img.shields.io/github/issues/MarcoHeckmann/GunGame)](https://github.com/MarcoHeckmann/GunGame/issues)
[![GitHub stars](https://img.shields.io/github/stars/MarcoHeckmann/GunGame?style=social)](https://github.com/MarcoHeckmann/GunGame/stargazers)

</div>

---

## 📖 Table of Contents

- [Features](#-features)
- [Requirements](#-requirements)
- [Installation](#-installation)
- [Setup](#-setup)
- [Commands](#-commands)
- [Permissions](#-permissions)
- [Configuration](#-configuration)
- [Gameplay & Levels](#-gameplay--levels)
- [Building from Source](#-building-from-source)
- [Contributing](#-contributing)
- [License](#-license)

---

## ✨ Features

- 🔫 Classic GunGame system with 5 levels and progressive weapon/armor loadouts
- 🗺️ Multiple map support with per-map spawns
- 🛡️ Configurable spawn-protection regions that block damage
- 🗄️ Stats (kills, deaths, K/D ratio) stored in a MySQL database via HikariCP
- 📋 Live scoreboard showing kills, deaths, K/D and the current map
- 🔄 Automatic map changes with a visible actionbar countdown
- 👑 Force a map change via command or an interactive GUI
- 🛠️ Edit mode for building and tuning maps
- 📝 Fully customizable messages via `messages.yml`

## 📋 Requirements

| Requirement | Version |
|---|---|
| Java | 21+ |
| Paper | 1.21.11+ |
| MySQL | Any recent version |

## 🚀 Installation

1. Download the latest JAR from the [Releases](https://github.com/MarcoHeckmann/GunGame/releases) page
2. Place the JAR file into your server's `plugins/` folder
3. Start the server once so the plugin generates its default `messages.yml` and `spawns.yml` files
4. Stop the server and configure your MySQL connection in `DatabaseManager.java` (see [Configuration](#-configuration))
5. Restart the server

## 🗺️ Setup

To make a map fully playable, set up its spawn and spawn protection:

1. Stand at the desired spawn location of the map
2. `/gungame setspawn <Map>` – set the map's spawn
3. Stand at one corner of the protected zone and run `/gungame pos1 <Map>`
4. Stand at the opposite corner and run `/gungame pos2 <Map>`
5. Repeat for every additional map – the server changes between them automatically

## ⌨️ Commands

| Command | Description | Permission |
|---|---|---|
| `/gungame spawn <map>` | Teleport to a map's spawn | - |
| `/gungame maps` | List all available maps | `gungame.maps` |
| `/gungame forcemap` | Open the GUI to force a map change | `gungame.forcemap` |
| `/gungame stats <player>` | Open another player's stats GUI | - |
| `/gungame edit` | Toggle edit mode for yourself | `gungame.editor` |
| `/gungame edit <player>` | Toggle edit mode for another player | `gungame.editor.others` |
| `/gungame editors` | List all active editors | `gungame.editors` |

### Admin/Setup commands

| Command | Description | Permission |
|---|---|---|
| `/gungame setspawn <map>` | Set the spawn of a map | `gungame.setspawn` |
| `/gungame delspawn <map>` | Remove the spawn of a map | `gungame.delspawn` |
| `/gungame pos1 <map>` | Set corner 1 of the spawn-protection zone | `gungame.setup` |
| `/gungame pos2 <map>` | Set corner 2 of the spawn-protection zone | `gungame.setup` |

> The main command is `/gungame` with the alias `/gg`.

## 🔑 Permissions

| Permission | Description |
|---|---|
| `gungame.setspawn` | Allows setting a map's spawn |
| `gungame.delspawn` | Allows removing a map's spawn |
| `gungame.setup` | Allows setting spawn-protection zones (pos1/pos2) |
| `gungame.maps` | Allows listing all maps |
| `gungame.forcemap` | Allows forcing a map change |
| `gungame.editor` | Allows toggling edit mode for yourself |
| `gungame.editor.others` | Allows toggling edit mode for other players |
| `gungame.editors` | Allows listing active editors |

> Grant `gungame.*` to admins, or add the individual nodes to a permission group.

## ⚙️ Configuration

Configuration files are generated in `plugins/GunGame/` the first time the server starts.

### `messages.yml`

All plugin messages can be fully customized, including the prefix. The following placeholders are available:

| Placeholder | Description |
|---|---|
| `%prefix%` | The plugin prefix defined in `messages.yml` |
| `%map%` | The name of the map |
| `%victim%` | The killed player |
| `%killer%` | The killing player |
| `%time%` | Remaining time until the map change |
| `%player%` | The player who performed the action |
| `%permission%` | The required permission node |
| `%kills%` / `%deaths%` / `%kd%` | Stats values |
| `%reason%` | Error reason (database) |

### `spawns.yml`

Created automatically; stores the spawns and spawn-protection regions of all maps.

### `Database`

The MySQL credentials currently live in `DatabaseManager.java`. Adjust them there:

```java
config.setJdbcUrl("jdbc:mysql://<HOST>:<PORT>/<DATABASE>");
config.setUsername("<USER>");
config.setPassword("<PASSWORD>");
```

The `stats` table is created on startup and stores each player's UUID, kills, deaths and K/D ratio.

> ⚠️ **Warning:** The credentials in the code are for development only. For production, move them into a configuration file.

## 🎮 Gameplay & Levels

A kill ranks you up and grants the next loadout; a death resets you to level 0. Kills, deaths and K/D are saved per player in the database.

| Level | Loadout |
|---|---|
| 0 (Start) | Wooden axe |
| 1 | Wooden sword |
| 2 | Wooden sword + iron boots |
| 3 | Iron sword + iron boots + iron leggings |
| 4 | Iron sword + partially enchanted iron armor |
| 5 | Iron sword (Sharpness) + full enchanted iron armor |

## 🛠️ Building from Source

Make sure you have [Maven](https://maven.apache.org/) and Java 21+ installed, then run:

```bash
mvn clean package
```

The compiled JAR will be located in the `target/` folder.

## 🤝 Contributing

Contributions are welcome! If you'd like to contribute:

1. Fork the repository
2. Create a new branch (`git checkout -b feature/my-feature`)
3. Commit your changes (`git commit -m 'Add my feature'`)
4. Push to the branch (`git push origin feature/my-feature`)
5. Open a Pull Request

Please open an [issue](https://github.com/MarcoHeckmann/GunGame/issues) first for major changes or to report bugs.

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

<div align="center">

Made with ❤️ by [SkyDynamics](https://github.com/MarcoHeckmann)

</div>
