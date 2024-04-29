plugins {
    kotlin("jvm") version("1.9.23")
    id("fabric-loom") version("1.6-SNAPSHOT")
}

group = "net.nutils"
version = "1.2-1"

repositories {
    mavenCentral()
    maven(uri("https://maven.shedaniel.me/"))
    maven(uri("https://maven.terraformersmc.com/releases/"))
}

dependencies {
    minecraft("com.mojang", "minecraft", "1.20.5")
    mappings("net.fabricmc:yarn:1.20.5+build.1:v2")
    modImplementation("net.fabricmc", "fabric-loader", "0.15.10")
    modImplementation("net.fabricmc.fabric-api", "fabric-api", "0.97.6+1.20.5")
    modApi("com.terraformersmc", "modmenu", "10.0.0-beta.1")
    modApi("me.shedaniel.cloth", "cloth-config-fabric", "14.0.126") {
        exclude("net.fabricmc.fabric-api")
    }
}

tasks {
    compileJava {
        options.release.set(21)
        options.encoding = "UTF-8"
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }
}