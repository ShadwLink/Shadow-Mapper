import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "nl.shadowlink.tools.shadowmapper"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.nikhaldimann:inieditor:r6")

    implementation("org.jogamp.jogl:jogl-all-main:2.3.2")
    implementation("org.jogamp.gluegen:gluegen-rt-main:2.3.2")
    implementation("org.netbeans.external:AbsoluteLayout:RELEASE150")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}