pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.polyfrost.cc/releases")
        maven("https://maven.architectury.dev/")
    }
    plugins {
        val egtVersion = "0.1.12"
        id("gg.essential.multi-version.root") version egtVersion
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "io.github.juuxel.loom-quiltflower-mini") {
                useModule("com.github.wyvest:loom-quiltflower-mini:${requested.version}")
            }
        }
    }
}

rootProject.name = "PolyAccessibility"
rootProject.buildFileName = "root.gradle.kts"

listOf(
    "1.19.1-fabric"
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}