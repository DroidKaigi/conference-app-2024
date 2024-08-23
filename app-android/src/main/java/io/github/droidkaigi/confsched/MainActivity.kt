package io.github.droidkaigi.confsched

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.window.layout.DisplayFeature
import androidx.window.layout.WindowInfoTracker
import dagger.hilt.android.AndroidEntryPoint
import io.github.droidkaigi.confsched.data.di.RepositoryProvider
import io.github.droidkaigi.confsched.designsystem.theme.dotGothic16FontFamily
import io.github.droidkaigi.confsched.droidkaigiui.compositionlocal.LocalClock
import io.github.droidkaigi.confsched.model.FontFamily.DotGothic16Regular
import io.github.droidkaigi.confsched.model.FontFamily.SystemDefault
import io.github.droidkaigi.confsched.model.Settings.DoesNotExists
import io.github.droidkaigi.confsched.model.Settings.Exists
import io.github.droidkaigi.confsched.model.Settings.Loading
import io.github.droidkaigi.confsched.model.SettingsRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var clockProvider: ClockProvider

    @Inject
    lateinit var repositoryProvider: RepositoryProvider

    @Inject
    lateinit var settingRepository: SettingsRepository

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Navigation icon color can be changed since API 26(O)
        if (VERSION.SDK_INT < VERSION_CODES.O) {
            enableEdgeToEdge()
        } else {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    lightScrim = Color.Transparent.toArgb(),
                    darkScrim = Color.Transparent.toArgb(),
                ),
                navigationBarStyle = SystemBarStyle.auto(
                    lightScrim = Color.Transparent.toArgb(),
                    darkScrim = Color.Transparent.toArgb(),
                ),
            )

            // For API29(Q) or higher and 3-button navigation,
            // the following code must be written to make the navigation color completely transparent.
            if (VERSION.SDK_INT >= VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
        }

        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = false
        setContent {
            val settings = settingRepository.settings()

            val windowSize = calculateWindowSizeClass()
            val displayFeatures = calculateDisplayFeatures(this)

            val fontFamily = when (settings) {
                DoesNotExists, Loading -> dotGothic16FontFamily()
                is Exists -> {
                    when (settings.useFontFamily) {
                        DotGothic16Regular -> dotGothic16FontFamily()
                        SystemDefault -> null
                    }
                }
            }

            CompositionLocalProvider(
                LocalClock provides clockProvider.clock(),
            ) {
                repositoryProvider.Provide {
                    KaigiApp(
                        windowSize = windowSize,
                        fontFamily = fontFamily,
                        displayFeatures = displayFeatures,
                    )
                }
            }
        }
    }
}

@Composable
private fun calculateDisplayFeatures(activity: ComponentActivity): PersistentList<DisplayFeature> {
    val windowLayoutInfo = remember(activity) {
        WindowInfoTracker.getOrCreate(activity).windowLayoutInfo(activity)
    }
    val displayFeatures by produceState(
        initialValue = persistentListOf(),
        key1 = windowLayoutInfo,
    ) {
        windowLayoutInfo.collect { info ->
            value = info.displayFeatures.toPersistentList()
        }
    }

    return displayFeatures
}
