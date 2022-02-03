plugins {
    id("java-library")
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    kotlinCompileOnlyDependencies()
    customLintRuleDependencies()
    junitDependencies()
    junitJupiterDependencies()
}
