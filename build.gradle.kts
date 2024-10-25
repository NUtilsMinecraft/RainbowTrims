plugins {
    kotlin("jvm") version("2.0.21")
    id("fabric-loom") version("1.8-SNAPSHOT")
    id("com.modrinth.minotaur") version("2.8.7")
}

group = "net.nutils"
version = "1.5-0"

repositories {
    mavenCentral()
    maven(uri("https://maven.shedaniel.me/"))
    maven(uri("https://maven.terraformersmc.com/releases/"))
}

dependencies {
    minecraft("com.mojang", "minecraft", "1.21.2")
    mappings("net.fabricmc:yarn:1.21.2+build.1")
    modImplementation("net.fabricmc", "fabric-loader", "0.16.7")
    modApi("com.terraformersmc", "modmenu", "12.0.0-beta.1")
    modApi("me.shedaniel.cloth", "cloth-config-fabric", "16.0.141") {
        exclude("net.fabricmc.fabric-api")
    }
}

loom {
    accessWidenerPath = rootDir.resolve("src/main/resources/rainbowtrims.accesswidener")
}

modrinth {
    token.set(properties["modrinthToken"] as String)
    projectId.set(properties["modrinthRainbowTrims"] as String)
    versionNumber.set(version as String)
    versionType.set("release")
    versionName.set("Rainbow Trims ${version as String}")
    uploadFile.set(tasks.remapJar)
    loaders.add("fabric")
    gameVersions.set(listOf("1.21.2"))
    changelog.set("Update to 1.21.2")
    dependencies {
        required.version("PcJvQYqu", "JbVSQUVw")
    }
}

tasks {
    compileJava {
        options.release = 21
        options.encoding = Charsets.UTF_8.name()
    }
}