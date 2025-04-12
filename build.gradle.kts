plugins {
    kotlin("jvm") version("2.1.20")
    id("fabric-loom") version("1.10-SNAPSHOT")
}

group = "net.nutils"
version = "1.7-0"

repositories {
    mavenCentral()
    maven(uri("https://maven.shedaniel.me/"))
    maven(uri("https://maven.terraformersmc.com/releases/"))
}

dependencies {
    minecraft("com.mojang", "minecraft", "1.21.5")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc", "fabric-loader", "0.16.13")

    modApi("com.terraformersmc", "modmenu", "14.0.0-rc.2")
    modApi("me.shedaniel.cloth", "cloth-config-fabric", "18.0.145") {
        exclude("net.fabricmc.fabric-api")
    }
}

loom {
    accessWidenerPath = file("src/main/resources/rainbowtrims.accessWidener")
}

tasks {
    compileJava {
        options.release = 21
        options.encoding = Charsets.UTF_8.name()
    }
}