plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
}


group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom:2026.05.11-1.21.11")
    implementation(files("libs/luaj-jse-3.0.2.jar"))
    implementation("ch.qos.logback:logback-classic:1.5.6")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "org.example.Main"
        }
    }

    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
    }
}