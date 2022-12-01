import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

version = "0.0.1-SNAPSHOT"
group = "com.unrec"
description = "imdb-csv-parser"
java.sourceCompatibility = JavaVersion.VERSION_11

object Versions {

    const val KOTLIN = "1.6.21"
    const val GRAPHQL = "14.0.0"
}

plugins {
    val kotlinVersion = "1.6.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.springframework.boot") version "2.5.2"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    id("maven-publish")
}

dependencies {
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = Versions.KOTLIN)

    // spring
    implementation(platform("org.springframework.boot:spring-boot-dependencies:2.7.5"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // graphql
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    implementation("com.graphql-java:graphql-java-extended-scalars:19.1")

    // etc
    implementation("com.unrec", "imdb-csv-parser", "0.0.2")

    // test
    testImplementation(group = "org.jetbrains.kotlin", name = "kotlin-test", version = Versions.KOTLIN)
    testImplementation(group = "io.kotest", name = "kotest-assertions-core-jvm", version = "5.5.1")
}

detekt {
    config = files("$projectDir/detekt-config.yml")
    allRules = true
    parallel = true
}

tasks.apply {
    test {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xinline-classes")
            jvmTarget = "11"
        }
    }

    withType<Detekt>().configureEach {
        jvmTarget = "1.8"
        reports {
            with(html) {
                required.set(true)
                outputLocation.set(file("$buildDir/reports/detekt/detekt.html"))
            }
        }
    }
}

publishing {
    publications.create<MavenPublication>("artifact").from(components["java"])
    repositories.mavenLocal()
}

repositories {
    mavenCentral()
    mavenLocal()
}
