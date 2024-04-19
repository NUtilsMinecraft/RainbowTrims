plugins {
    kotlin("jvm") version("1.9.23")
    id("fabric-loom") version("1.6-SNAPSHOT")
}

group = "net.nutils"
version = "1.0-0"

repositories {
    mavenCentral()
    maven(uri("https://maven.shedaniel.me/"))
    maven(uri("https://maven.terraformersmc.com/releases/"))
}

dependencies {
    minecraft("com.mojang", "minecraft", "1.20.4")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc", "fabric-loader", "0.15.7")
    modImplementation("net.fabricmc.fabric-api", "fabric-api", "0.96.11+1.20.4")
    modApi("com.terraformersmc", "modmenu", "9.0.0")
    modApi("me.shedaniel.cloth", "cloth-config-fabric", "13.0.121") {
        exclude("net.fabricmc.fabric-api")
    }
}

tasks {
    compileJava {
        options.release.set(17)
        options.encoding = "UTF-8"
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}