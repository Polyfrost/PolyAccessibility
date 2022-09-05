plugins {
    kotlin("jvm") version "1.6.21" apply false
    id("gg.essential.multi-version.root")
    alias(libs.plugins.loom.quiltflower) apply false
}

preprocess {
    "1.19.1-fabric"(11901, "yarn") {}
}