import java.net.URI
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.3.70"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "com.lavans"
version = "1.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = URI("https://oss.sonatype.org/content/repositories/snapshots") }
    maven { url = URI("https://jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.vavr:vavr-kotlin:1.0.0-SNAPSHOT")
    implementation("io.vavr:vavr-jackson:1.0.0-SNAPSHOT")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.2") {
        exclude(group = "org.jetbrains.kotlin")
    }

    testImplementation("junit:junit:4.13")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.withType<ShadowJar> {
    mergeServiceFiles()
    manifest {
        attributes(mapOf("Main-Class" to "com.exwzd.dia.MainKt"))
    }
}




repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}