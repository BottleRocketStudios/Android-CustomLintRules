import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.dsl.DependencyHandler

// Provides dependencies that can be used throughout the project build.gradle files

// https://github.com/JetBrains/kotlin/blob/master/ChangeLog.md
// https://github.com/JetBrains/kotlin/releases/latest
// https://plugins.jetbrains.com/plugin/6954-kotlin
// https://kotlinlang.org/docs/reference/whatsnew15.html
// https://kotlinlang.org/docs/releases.html#release-details
// TODO: Update corresponding buildSrc/build.gradle.kts value when updating this version!
const val KOTLIN_VERSION = "1.6.10"
private const val NAVIGATION_VERSION = "2.3.5"

/**
 * Provides the source of truth for version/configuration information to any gradle build file (project and app module build.gradle.kts)
 */
object Config {
    // https://github.com/JLLeitschuh/ktlint-gradle/blob/master/CHANGELOG.md
    // https://github.com/JLLeitschuh/ktlint-gradle/releases
    const val KTLINT_GRADLE_VERSION = "10.2.1"

    // https://github.com/pinterest/ktlint/blob/master/CHANGELOG.md
    // https://github.com/pinterest/ktlint/releases
    const val KTLINT_VERSION = "0.43.2"

    // View how to execute the coverage and verifcations gradle tasks as well as how to view coverage reports in the local jacocoSetup.gradle file
    // http://www.jacoco.org/jacoco/trunk/doc/
    // https://github.com/jacoco/jacoco/releases
    // const val JACOCO_VERSION = "0.8.7" - Helper jacoco gradle files manage the jacoco plugin version due to issues reading this value inside groovy gradle files

    // Website info: https://detekt.github.io/detekt/index.html
    // Rules:
    //   https://detekt.github.io/detekt/comments.html
    //   https://detekt.github.io/detekt/complexity.html
    //   https://detekt.github.io/detekt/coroutines.html
    //   https://detekt.github.io/detekt/empty-blocks.html
    //   https://detekt.github.io/detekt/exceptions.html
    //   https://detekt.github.io/detekt/formatting.html
    //   https://detekt.github.io/detekt/naming.html
    //   https://detekt.github.io/detekt/performance.html
    //   https://detekt.github.io/detekt/style.html
    // Release info: https://github.com/detekt/detekt/releases
    const val DETEKT_VERSION = "1.19.0"

    /**
     * Called from root project buildscript block in the project root build.gradle.kts
     */
    object BuildScriptPlugins {
        // https://developer.android.com/studio/releases/gradle-plugin
        // TODO: Update corresponding buildSrc/build.gradle.kts value when updating this version!
        // TODO: Additionally, update the CUSTOM_LINT_RULE_VERSION below (AGP version + 23)
        const val ANDROID_GRADLE = "com.android.tools.build:gradle:7.0.4"
        const val KOTLIN_GRADLE = "org.jetbrains.kotlin:kotlin-gradle-plugin:$KOTLIN_VERSION"

        // Gradle version plugin; use dependencyUpdates task to view third party dependency updates via `./gradlew dependencyUpdates` or AS Gradle -> [project]] -> Tasks -> help -> dependencyUpdates
        // https://github.com/ben-manes/gradle-versions-plugin/releases
        const val GRADLE_VERSIONS = "com.github.ben-manes:gradle-versions-plugin:0.41.0"
    }

    /**
     * Called in non-root project modules via id()[org.gradle.kotlin.dsl.PluginDependenciesSpecScope.id]
     * or kotlin()[org.gradle.kotlin.dsl.kotlin] (the PluginDependenciesSpec.kotlin extension function) in a build.gradle.kts
     */
    object ApplyPlugins {
        const val ANDROID_APPLICATION = "com.android.application"
        const val ANDROID_LIBRARY = "com.android.library"
        const val GRADLE_VERSIONS = "com.github.ben-manes.versions"
        const val KT_LINT = "org.jlleitschuh.gradle.ktlint"
        const val DETEKT = "io.gitlab.arturbosch.detekt"
        // const val JACOCO = "jacoco" // https://docs.gradle.org/current/userguide/jacoco_plugin.html - Helper jacoco gradle files manage applying the jacoco plugin
        object Kotlin {
            const val ANDROID = "android"
        }
    }

    // What each version represents - https://medium.com/androiddevelopers/picking-your-compilesdkversion-minsdkversion-targetsdkversion-a098a0341ebd
    object AndroidSdkVersions {
        const val COMPILE_SDK = 31

        // https://developer.android.com/studio/releases/build-tools
        const val BUILD_TOOLS = "31.0.0"
        const val MIN_SDK = 21

        // https://developer.android.com/about/versions/12/behavior-changes-12
        const val TARGET_SDK = 31
    }

    // Gradle versions plugin configuration: https://github.com/ben-manes/gradle-versions-plugin#revisions
    fun isNonStable(version: String): Boolean {
        val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        return isStable.not()
    }
}

/**
 * Dependency Version definitions with links to source (if open source)/release notes. Defines the version in one place for multiple dependencies that use the same version.
 * Use [DependencyHandler] extension functions below that provide logical groupings of dependencies including appropriate configuration accessors.
 */
private object Libraries {
    //// Kotlin
    const val KOTLIN_STDLIB_JDK7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$KOTLIN_VERSION"
    const val KOTLIN_STDLIB_JDK8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$KOTLIN_VERSION"
    const val KOTLIN_REFLECT = "org.jetbrains.kotlin:kotlin-reflect:$KOTLIN_VERSION"
}

/**
 * test and/or androidTest specific dependencies.
 * Use [DependencyHandler] extension functions below that provide logical groupings of dependencies including appropriate configuration accessors.
 */
private object TestLibraries {
    // https://github.com/junit-team/junit4/releases
    // https://github.com/junit-team/junit4/blob/master/doc/ReleaseNotes4.13.md
    const val JUNIT = "junit:junit:4.13.2"

    // https://github.com/junit-team/junit5/releases
    // https://junit.org/junit5/docs/current/release-notes/
    private const val JUNIT_JUPITER_VERSION = "5.8.2"
    const val JUNIT_JUPITER_API = "org.junit.jupiter:junit-jupiter-api:$JUNIT_JUPITER_VERSION"
    const val JUNIT_JUPITER_ENGINE = "org.junit.jupiter:junit-jupiter-engine:$JUNIT_JUPITER_VERSION"
}

private object LintLibraries {
    private const val CUSTOM_LINT_RULE_VERSION = "30.0.4" // Must be ANDROID_GRADLE plugin version + 23 (see https://github.com/googlesamples/android-custom-lint-rules#lint-version and https://youtu.be/jCmJWOkjbM0?t=89 for more info)
    const val LINT = "com.android.tools.lint:lint:$CUSTOM_LINT_RULE_VERSION"
    const val LINT_API = "com.android.tools.lint:lint-api:$CUSTOM_LINT_RULE_VERSION"
    const val LINT_CHECKS = "com.android.tools.lint:lint-checks:$CUSTOM_LINT_RULE_VERSION"
    const val LINT_TESTS = "com.android.tools.lint:lint-tests:$CUSTOM_LINT_RULE_VERSION"
}

//// Dependency Groups - to be used inside dependencies {} block instead of declaring all necessary lines for a particular dependency
//// See DependencyHandlerUtils.kt to define DependencyHandler extension functions for types not handled (ex: compileOnly).
//// More info in BEST_PRACTICES.md -> Build section
fun DependencyHandler.kotlinDependencies() {
    implementation(Libraries.KOTLIN_STDLIB_JDK7)
    implementation(Libraries.KOTLIN_STDLIB_JDK8)
    implementation(Libraries.KOTLIN_REFLECT)
}
fun DependencyHandler.kotlinCompileOnlyDependencies() {
    compileOnly(Libraries.KOTLIN_STDLIB_JDK7)
    compileOnly(Libraries.KOTLIN_STDLIB_JDK8)
    compileOnly(Libraries.KOTLIN_REFLECT)
}

// Test specific dependency groups
fun DependencyHandler.junitDependencies() {
    testImplementation(TestLibraries.JUNIT)
}
fun DependencyHandler.junitJupiterDependencies() {
    testImplementation(TestLibraries.JUNIT_JUPITER_API)
    testRuntimeOnly(TestLibraries.JUNIT_JUPITER_ENGINE)
}

fun DependencyHandler.customLintRuleDependencies() {
    compileOnly(LintLibraries.LINT_API)
    compileOnly(LintLibraries.LINT_CHECKS)
    compileOnly(LintLibraries.LINT_TESTS)
    testImplementation(LintLibraries.LINT_TESTS)
    testImplementation(LintLibraries.LINT)
}
