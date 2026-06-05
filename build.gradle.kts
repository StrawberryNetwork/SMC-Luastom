plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
}

group = "LuaCraft.LuaStom"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom:2026.05.11-1.21.11")
    implementation(files("libs/luaj-jse-3.0.2.jar"))
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("de.articdive:jnoise-pipeline:4.1.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "LuaCraft.LuaStom.Main"
        }
    }

    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveBaseName.set("LuaStom")
        archiveClassifier.set("")
        archiveVersion.set("1.0-SNAPSHOT")
    }
}