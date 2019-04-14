import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String = "1.3.30"
val springBootVersion: String = "2.1.2.RELEASE"

plugins {
    val kotlinVersion = "1.3.21"
    kotlin("jvm") version kotlinVersion
    kotlin("kapt") version "1.3.21"
    id("org.springframework.boot") version "2.1.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.7.RELEASE"
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.noarg") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
    id("com.google.cloud.tools.jib") version "1.0.2"
    id("com.diffplug.gradle.spotless") version "3.21.1"
}

val versionObj = Version(major = 0, minor = 0, revision = 1)
group = "com.grosslicht"
version = "$versionObj"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.auth0:auth0-spring-security-api:1.2.1")
    implementation("com.google.maps:google-maps-services:0.9.3")
    implementation("at.favre.lib:id-mask:0.3.0")

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    implementation("io.github.microutils:kotlin-logging:1.6.24")

    //implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.0-alpha-2")
    runtimeOnly("com.h2database:h2:1.4.197")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.springframework.security:spring-security-test")
}

val dockerImage = System.getenv("CONTAINER_NAME")

jib.to {
    image = dockerImage
    tags = setOf("latest", "${project.version}")
    auth {
        username = "pdgwien"
        password = System.getenv("DOCKER_PASSWORD")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

spotless {
    kotlin {
        ktlint()
    }
    kotlinGradle {
        target("*.gradle.kts", "additionalScripts/*.gradle.kts")
        ktlint()
    }
}

data class Version(val major: Int, val minor: Int, val revision: Int) {
    private val buildNumber: String = System.getenv("TRAVIS_BUILD_NUMBER") ?: "SNAPSHOT"

    override fun toString(): String = "$major.$minor.$revision-$buildNumber"
}
