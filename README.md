# Stonecutter Multi-Platform Mod Template

This is a heavily modified older version of this template - https://github.com/rotgruengelb/stonecutter-mod-template

A multi-platform Minecraft mod template for **Fabric**, **Forge** and **NeoForge**,
using [Stonecutter](https://stonecutter.kikugie.dev/) for
multiversion and multiloader code.

## **Configure your mod**

1. Run setup_template.bat to automatically rename your packages and Mod ID and similar.

2. Change gradle.properties as necessary

3. Replace `src/main/resources/icon.png` (and optionally add `.idea/icon.png`) with the mods icon.

4. Set the desired publishing environment in ModPlatformPlugin.kt for CurseForge and Modrinth (client and/or server).

### Java and Gradle settings
Project Java version: 25
Gradle JVM: JetBrains Runtime 25

### Info
Forge < 1.15 does not support mixins by default.

### Issues
Forge client launch currently does not work in <= 1.15

### Fabric AW/CT Loader versions
`accessWidener v2` - Fabric Loader 0.12.0+
`classTweaker v1` - Fabric Loader 0.18.0+
