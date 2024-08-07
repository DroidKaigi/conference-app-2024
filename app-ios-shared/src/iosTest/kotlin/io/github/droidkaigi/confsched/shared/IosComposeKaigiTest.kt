package io.github.droidkaigi.confsched.shared

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass.Companion.calculateFromSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.runComposeUiTest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import io.github.droidkaigi.confsched.data.Repositories
import io.github.droidkaigi.confsched.data.auth.Authenticator
import io.github.droidkaigi.confsched.data.auth.User
import io.github.droidkaigi.confsched.data.remoteconfig.FakeRemoteConfigApi
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import io.github.takahirom.roborazzi.captureRoboImage
import kotlin.test.Test

class IosComposeKaigiTest {
    @OptIn(
        ExperimentalTestApi::class,
        ExperimentalMaterial3WindowSizeClassApi::class,
        ExperimentalRoborazziApi::class
    )
    @Test
    fun get() {
        val kmpEntryPoint = KmpEntryPoint()
        kmpEntryPoint.initForTest(
            FakeRemoteConfigApi(),
            object : Authenticator {
                override suspend fun currentUser(): User? {
                    return User("test token")
                }

                override suspend fun signInAnonymously(): User? {
                    return null
                }
            },
            dataModuleOverride = testOverrideModules
        )
        runComposeUiTest {
            setContent {
                CompositionLocalProvider(
                    LocalLifecycleOwner provides object : LifecycleOwner {
                        override val lifecycle: Lifecycle = LifecycleRegistry(this)
                    },
                    LocalRepositories provides kmpEntryPoint.get<Repositories>().map,
                ) {
                    KaigiApp(
                        windowSize = calculateFromSize(
                            size = Size(
                                width = 375F,
                                height = 812F
                            ),
                            density = LocalDensity.current,
                        )
                    )
                }
            }
            // We are investigating error about Json format
//            onRoot().captureRoboImage(this, filePath = "ios_compose_launch.png")
        }
    }
}


// We are investigating error about Json format
