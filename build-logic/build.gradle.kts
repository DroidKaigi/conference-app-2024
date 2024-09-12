import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "io.github.droidkaigi.confsched.buildlogic"

repositories {
    google {
        content {
            includeGroupByRegex("com\\.android.*")
            includeGroupByRegex("com\\.google.*")
            includeGroupByRegex("androidx.*")
        }
    }
    mavenCentral()
    gradlePluginPortal()
}

// We are using JDK 17 for build process but we are targeting JDK 11 for the app
// If we use jvmToolchain, we need to install JDK 11
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "17"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.bundles.plugins)
    // https://github.com/google/dagger/issues/3068#issuecomment-1470534930
    implementation(libs.javaPoet)
}

gradlePlugin {
    plugins {
        // Primitives
        register("androidApplication") {
            id = "droidkaigi.primitive.androidapplication"
            implementationClass = "io.github.droidkaigi.confsched.primitive.AndroidApplicationPlugin"
        }
        register("android") {
            id = "droidkaigi.primitive.android"
            implementationClass = "io.github.droidkaigi.confsched.primitive.AndroidPlugin"
        }
        register("androidKotlin") {
            id = "droidkaigi.primitive.android.kotlin"
            implementationClass = "io.github.droidkaigi.confsched.primitive.AndroidKotlinPlugin"
        }
        register("androidCompose") {
            id = "droidkaigi.primitive.android.compose"
            implementationClass = "io.github.droidkaigi.confsched.primitive.AndroidComposePlugin"
        }
        register("androidHilt") {
            id = "droidkaigi.primitive.android.hilt"
            implementationClass = "io.github.droidkaigi.confsched.primitive.AndroidHiltPlugin"
        }
        register("androidCrashlytics") {
            id = "droidkaigi.primitive.android.crashlytics"
            implementationClass = "io.github.droidkaigi.confsched.primitive.AndroidCrashlyticsPlugin"
        }
        register("androidFirebase") {
            id = "droidkaigi.primitive.android.firebase"
            implementationClass = "io.github.droidkaigi.confsched.primitive.AndroidFirebasePlugin"
        }
        register("androidRoborazzi") {
            id = "droidkaigi.primitive.android.roborazzi"
            implementationClass = "io.github.droidkaigi.confsched.primitive.AndroidRoborazziPlugin"
        }
        register("kotlinMpp") {
            id = "droidkaigi.primitive.kmp"
            implementationClass = "io.github.droidkaigi.confsched.primitive.KmpPlugin"
        }
        register("kotlinMppIos") {
            id = "droidkaigi.primitive.kmp.ios"
            implementationClass = "io.github.droidkaigi.confsched.primitive.KmpIosPlugin"
        }
        register("kotlinMppAndroid") {
            id = "droidkaigi.primitive.kmp.android"
            implementationClass = "io.github.droidkaigi.confsched.primitive.KmpAndroidPlugin"
        }
        register("kotlinMppCompose") {
            id = "droidkaigi.primitive.kmp.compose"
            implementationClass = "io.github.droidkaigi.confsched.primitive.KmpComposePlugin"
        }
        register("kotlinMppRoborazzi") {
            id = "droidkaigi.primitive.kmp.roborazzi"
            implementationClass = "io.github.droidkaigi.confsched.primitive.KmpRoborazziPlugin"
        }
        register("kotlinMppKtorfit") {
            id = "droidkaigi.primitive.kmp.ktorfit"
            implementationClass = "io.github.droidkaigi.confsched.primitive.KmpKtorfitPlugin"
        }
        register("kotlinMppSkie") {
            id = "droidkaigi.primitive.kmp.skie"
            implementationClass = "io.github.droidkaigi.confsched.primitive.KmpSkiePlugin"
        }
        register("kotlinMppAndroidHilt") {
            id = "droidkaigi.primitive.kmp.android.hilt"
            implementationClass = "io.github.droidkaigi.confsched.primitive.KmpAndroidHiltPlugin"
        }
        register("kotlinMppKotlinSerialization") {
            id = "droidkaigi.primitive.kmp.serialization"
            implementationClass = "io.github.droidkaigi.confsched.primitive.KotlinSerializationPlugin"
        }
        register("ComposeInvestigator") {
            id = "droidkaigi.primitive.compose.investigator"
            implementationClass = "io.github.droidkaigi.confsched.primitive.ComposeInvestigatorPlugin"
        }

        register("Molecule") {
            id = "droidkaigi.primitive.molecule"
            implementationClass = "io.github.droidkaigi.confsched.primitive.MoleculePlugin"
        }
        register("koverEntryPoint") {
            id = "droidkaigi.primitive.kover.entrypoint"
            implementationClass = "io.github.droidkaigi.confsched.primitive.KoverEntryPointPlugin"
        }
        register("detekt") {
            id = "droidkaigi.primitive.detekt"
            implementationClass = "io.github.droidkaigi.confsched.primitive.DetektPlugin"
        }
        register("oss-licenses") {
            id = "droidkaigi.primitive.android.osslicenses"
            implementationClass = "io.github.droidkaigi.confsched.primitive.OssLicensesPlugin"
        }

        // Conventions
        register("androidFeature") {
            id = "droidkaigi.convention.androidfeature"
            implementationClass = "io.github.droidkaigi.confsched.convention.AndroidFeaturePlugin"
        }
        register("kmpFeature") {
            id = "droidkaigi.convention.kmpfeature"
            implementationClass = "io.github.droidkaigi.confsched.convention.KmpFeaturePlugin"
        }
    }
}
