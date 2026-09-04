# MatLib

A shared multi-platform multi-version minecraft library for my mods using Stonecutter.

### Local Maven

```kotlin
repositories {
    mavenLocal()
}

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)
}

dependencies {
    // Fabric
    implementation("net.mat0u5:matlib-fabric-${prop("deps.minecraft")}:local-SNAPSHOT") { isChanging = true }

    // NeoForge
    implementation("net.mat0u5:matlib-neoforge-${prop("deps.minecraft")}:local-SNAPSHOT") { isChanging = true }

    //Forge
    implementation("net.mat0u5:matlib-forge-${prop("deps.minecraft")}:local-SNAPSHOT") { isChanging = true }
}
```
