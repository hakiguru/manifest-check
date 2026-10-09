<p align="center">
  <img src=".github/logo.png" alt="manifest-check" width="600">
</p>

<p align="center">
  Velocity plugin + NeoForge mod that tells the proxy which modpack, modloader, MCversion a player is using.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.21.1-62B47A" alt="Minecraft 1.21.1">
  <img src="https://img.shields.io/badge/Java-21-E76F00" alt="Java 21">
  <a href="https://github.com/hakiguru/manifest-check/releases/latest"><img src="https://img.shields.io/github/v/release/hakiguru/manifest-check" alt="Latest release"></a>
</p>

## ✨ What it does

When a player connects, the Velocity proxy asks the client mod a question during login.
The mod answers with the modpack id and version, the loader and Minecraft versions,
and the list of installed mods. The proxy logs the answer.

Right now the plugin works in **diagnostic mode**: it only writes to the console.
🧭 Sending players to the right lobby by modpack is planned for a later version.

## 🧩 Platforms

| | Part | Runs on | Folder |
|---|---|---|---|
| <picture><source media="(prefers-color-scheme: dark)" srcset=".github/icons/velocity-dark-theme.png"><img src=".github/icons/velocity.png" alt="Velocity" height="24"></picture> | Plugin | Velocity proxy, Java 21 | [`velocity/`](velocity) |
| <img src=".github/icons/neoforge.png" alt="NeoForge" height="24"> | Client mod | NeoForge 21.1 · Minecraft 1.21.1 | [`neoforge-1.21.1/`](neoforge-1.21.1) |

## 📦 Download

Get both jars from [Releases](https://github.com/hakiguru/manifest-check/releases/latest).

## 📖 Documentation

Installation, configuration and how it works: see the [Wiki](https://github.com/hakiguru/manifest-check/wiki).

## 🔨 Building

Each folder is a separate Gradle project:

```bash
cd velocity && gradle build          # → velocity/build/libs/
cd neoforge-1.21.1 && gradle build   # → neoforge-1.21.1/build/libs/
```

Or open the folder in IntelliJ IDEA and run the `build` task.
