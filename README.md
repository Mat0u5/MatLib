# MatLib

A shared multi-platform multi-version minecraft library for my mods using Stonecutter.

### Local Maven Builds

Created via the publishMavenLocal task here in MatLib.

To add to other projects as an implementation:

```kotlin
repositories {
    mavenLocal()
}

configurations.all {
    resolutionStrategy.cacheDynamicVersionsFor(0, TimeUnit.SECONDS)
}

dependencies {
    // Fabric
    implementation("net.mat0u5:matlib-fabric-${prop("deps.minecraft")}:local-+")

    // NeoForge
    implementation("net.mat0u5:matlib-neoforge-${prop("deps.minecraft")}:local-+")

    //Forge
    implementation("net.mat0u5:matlib-forge-${prop("deps.minecraft")}:local-+")
}
```
