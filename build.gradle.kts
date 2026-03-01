plugins {
    kotlin("jvm") version "1.9.22"
    kotlin("kapt") version "1.9.22"

    id("io.micronaut.application") version "4.3.8"
}

version = "0.1"
group = "com.blacksun"

repositories {
    mavenCentral()
}

micronaut {
    version("4.3.8")

    runtime("netty")
    testRuntime("junit5")

    processing {
        incremental(true)
        annotations("com.blacksun.*")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-http-server-netty")

    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut:micronaut-json-core")

    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")

    implementation("io.micronaut:micronaut-http-client")

    implementation("jakarta.annotation:jakarta.annotation-api")

    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("org.yaml:snakeyaml")

    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

application {
    mainClass.set("com.blacksun.ApplicationKt")
}

tasks.test {
    useJUnitPlatform()
}