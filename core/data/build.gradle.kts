plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.android")
    id("droidkaigi.primitive.kmp.android.hilt")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.kmp.compose")
    id("droidkaigi.primitive.detekt")
    id("droidkaigi.primitive.kmp.ktorfit")
    id("droidkaigi.primitive.kmp.serialization")
    id("droidkaigi.primitive.compose.investigator")
}

android.namespace = "io.github.droidkaigi.confsched.core.data"

kotlin {
    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.model)
                implementation(projects.core.common)
                implementation(libs.kotlinxCoroutinesCore)
                // We use api for test
                api(libs.androidxDatastoreDatastorePreferences)
                implementation(libs.okIo)
                implementation(libs.ktorClientCore)
                implementation(libs.ktorClientLogging)
                implementation(libs.ktorKotlinxSerialization)
                implementation(libs.ktorContentNegotiation)
                implementation(libs.kermit)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktorClientOkHttp)
                implementation(libs.multiplatformFirebaseAuth)
                implementation(libs.okHttpLoggingInterceptor)
                implementation(libs.okHttpLoggingInterceptor)
                implementation(libs.firebaseRemoteConfig)
                implementation(libs.androidxLifecycleProcess)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktorClientDarwin)
                api(libs.koin)
            }
        }

        commonTest {
            dependencies {
                implementation(projects.core.model)
                implementation(libs.kotlinTest)
                implementation(libs.okIo)
                implementation(libs.ktorKotlinxSerialization)
            }
        }
    }
}
