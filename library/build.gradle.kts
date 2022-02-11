plugins {
    id(Config.ApplyPlugins.ANDROID_LIBRARY)
    kotlin(Config.ApplyPlugins.Kotlin.ANDROID)
    id(Config.ApplyPlugins.MAVEN_PUBLISH)
}

android {
    compileSdk = Config.AndroidSdkVersions.COMPILE_SDK

    defaultConfig {
        minSdk = Config.AndroidSdkVersions.MIN_SDK
        targetSdk = Config.AndroidSdkVersions.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    lint {
        isCheckDependencies = false
    }
}

/** Package the given lint rules into this AAR  */
dependencies {
    lintPublish(project(":lintRules"))
}

// https://docs.jitpack.io/android/
// https://developer.android.com/studio/build/maven-publish-plugin
// https://docs.gradle.org/current/userguide/publishing_maven.html
// Because the components are created only during the afterEvaluate phase, you must configure your publications using the afterEvaluate() lifecycle method.
afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release") {
                // Applies the component for the release build variant.
                from(components["release"])

                groupId = "com.github.BottleRocketStudios"
                artifactId = "Android-CustomLintRules"
                version = "1.0.0"
            }
        }
    }
}
