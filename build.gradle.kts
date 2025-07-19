plugins {
    kotlin("jvm") version("2.2.0")
    id("fabric-loom") version("1.11-SNAPSHOT")
}

group = "net.nutils"
version = "1.8-0"

repositories {
    mavenCentral()
    maven(uri("https://maven.shedaniel.me/"))
    maven(uri("https://maven.terraformersmc.com/releases/"))
}

dependencies {
    minecraft("com.mojang", "minecraft", "1.21.7")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc", "fabric-loader", "0.16.14")

    modApi("com.terraformersmc", "modmenu", "15.0.0-beta.3")
    modApi("me.shedaniel.cloth", "cloth-config-fabric", "19.0.147") {
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