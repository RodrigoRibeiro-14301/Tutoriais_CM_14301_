plugins {
    kotlin("jvm")
    kotlin("kapt") // Needed for annotation processing
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))
    // Include the annotations module
    implementation(project(":annotations"))
    // Use the annotation processor
    kapt(project(":processor"))
}

application {
    mainClass.set("com.dam.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
}