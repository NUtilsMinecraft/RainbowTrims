plugins {
    kotlin("jvm") version("2.0.0")
    id("fabric-loom") version("1.7-SNAPSHOT")
    id("com.modrinth.minotaur") version("2.8.7")
}

group = "net.nutils"
version = "1.4-1"

repositories {
    mavenCentral()
    maven(uri("https://maven.shedaniel.me/"))
    maven(uri("https://maven.terraformersmc.com/releases/"))
}

dependencies {
    minecraft("com.mojang", "minecraft", "1.21.1")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc", "fabric-loader", "0.15.11")
    modApi("com.terraformersmc", "modmenu", "11.0.1")
    modApi("me.shedaniel.cloth", "cloth-config-fabric", "15.0.130") {
        exclude("net.fabricmc.fabric-api")
    }
}

modrinth {
    token.set(properties["modrinthToken"] as String)
    projectId.set(properties["modrinthRainbowTrims"] as String)
    versionNumber.set(version as String)
    versionType.set("release")
    versionName.set("Rainbow Trims ${version as String}")
    uploadFile.set(tasks.remapJar)
    loaders.add("fabric")
    gameVersions.set(listOf("1.21.1"))
    dependencies {
        required.version("xhN1IvHi", "7jtvrmVP")
    }
}

tasks {
    compileJava {
        options.release = 21
        options.encoding = Charsets.UTF_8.name()
    }
}