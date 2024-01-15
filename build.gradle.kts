plugins {
    kotlin("jvm") version "1.9.21"
}

dependencies {
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.16.1"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
