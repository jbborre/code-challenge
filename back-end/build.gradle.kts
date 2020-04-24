import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("jvm") version "1.3.70"
    kotlin("plugin.spring") version "1.3.61"
    kotlin("plugin.allopen") version "1.3.61"
    kotlin("kapt") version "1.3.61"

}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.microutils:kotlin-logging:1.7.9")
//    implementation("org.apache.kafka:kafka-streams:2.5.0")
//    implementation("org.apache.kafka:kafka-clients:2.5.0")
//    testImplementation("org.springframework.boot:spring-boot-starter-test") {
//        exclude(module = "junit")
//        exclude(module = "mockito-core")
//    }
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka:3.0.4.RELEASE")
    implementation("org.springframework.kafka:spring-kafka:2.4.5.RELEASE")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("com.ninja-squad:springmockk:1.1.3")
}

configurations {
    implementation {
        resolutionStrategy.failOnVersionConflict()
    }
}


springBoot {
    mainClassName = "com.code.challenge.Application"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

sourceSets {
    main {
        java.srcDir("src/core/java")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Test> {
    useJUnitPlatform()
}