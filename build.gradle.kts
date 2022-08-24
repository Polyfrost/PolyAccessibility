@Suppress(
    "DSL_SCOPE_VIOLATION",
    "MISSING_DEPENDENCY_CLASS",
    "UNRESOLVED_REFERENCE_WRONG_RECEIVER",
    "FUNCTION_CALL_EXPECTED"
)

plugins {
    java
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.loom.quiltflower)
}

group = "cc.polyfrost"
version = "1.0.0"

repositories {
    maven("https://repo.polyfrost.cc/releases")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/")
    maven("https://server.bbkr.space/artifactory/libs-release")
}

dependencies {
    minecraft(libs.minecraft)
    mappings(libs.yarn)

    modImplementation(libs.fabric)
    modImplementation(libs.fabricApi)

    modApi(libs.modmenu)
    modApi(libs.cloth.config) {
        exclude("net.fabricmc.fabric-api")
    }
}

tasks {
    processResources {
        val modId: String by project
        val modName: String by project
        val modDescription: String by project
        val githubProject: String by project

        inputs.property("id", modId)
        inputs.property("group", project.group)
        inputs.property("name", modName)
        inputs.property("description", modDescription)
        inputs.property("version", project.version)
        inputs.property("github", githubProject)

        filesMatching(listOf("fabric.mod.json", "quilt.mod.json")) {
            expand(
                "id" to modId,
                "group" to project.group,
                "name" to modName,
                "description" to modDescription,
                "version" to project.version,
                "github" to githubProject,
            )
        }
    }

    remapJar {
        archiveClassifier.set("fabric-1.19.2")
    }

    remapSourcesJar {
        archiveClassifier.set("fabric-1.19.2-sources")
    }
}

java {
    withSourcesJar()
}