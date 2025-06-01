plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    kotlin("jvm") version "1.9.22"
    `maven-publish`
}

group = "com.example.linter"
version = "1.0.0"

gradlePlugin {
    plugins {
        create("projectStructureLinter") {
            id = "com.example.linter.plugin"
            implementationClass = "com.example.linter.ProjectStructureLinterPlugin"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "com.example.linter"
            artifactId = "project-structure-linter"
            version = "1.0.0"
        }
    }

    repositories {
        maven {
            name = "local"
            url = uri("$buildDir/repo") 
        }
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(kotlin("stdlib"))
}

repositories {
    mavenCentral()
}