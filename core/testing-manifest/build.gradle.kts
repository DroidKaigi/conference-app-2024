plugins {
    id("droidkaigi.primitive.android")
    id("droidkaigi.primitive.android.kotlin")
    id("droidkaigi.primitive.android.hilt")
}

android.namespace = "io.github.droidkaigi.confsched.core.testing"

dependencies {
    implementation(projects.core.droidkaigiui)
}
