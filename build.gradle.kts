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
    id("maven-publish")
}

dependencies {
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8", version = Versions.KOTLIN)

    // spring
    implementation(platform("org.springframework.boot:spring-boot-dependencies:2.7.5"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // graphql
    implementation("com.graphql-java-kickstart", "graphql-spring-boot-starter", Versions.GRAPHQL)
    testImplementation("com.graphql-java-kickstart", "graphql-spring-boot-starter-test", Versions.GRAPHQL)
    runtimeOnly("com.graphql-java-kickstart:graphiql-spring-boot-starter:11.1.0")

    // etc
    implementation("com.unrec", "imdb-csv-parser", "0.0.1")

    // test
    testImplementation(group = "org.jetbrains.kotlin", name = "kotlin-test", version = Versions.KOTLIN)
    testImplementation(group = "io.kotest", name = "kotest-assertions-core-jvm", version = "5.5.1")

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
}

publishing {
    publications.create<MavenPublication>("artifact").from(components["java"])
    repositories.mavenLocal()
}

repositories {
    mavenCentral()
    mavenLocal()
}
