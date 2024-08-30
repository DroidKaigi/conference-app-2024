package io.github.droidkaigi.confsched.testing.rules

import android.content.Intent
import android.os.Bundle
import android.view.Choreographer
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import co.touchlab.kermit.CommonWriter
import co.touchlab.kermit.Logger
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.RoborazziOptions.CompareOptions
import com.github.takahirom.roborazzi.RoborazziOptions.PixelBitConfig
import com.github.takahirom.roborazzi.RoborazziOptions.RecordOptions
import com.github.takahirom.roborazzi.RoborazziRule
import com.github.takahirom.roborazzi.RoborazziRule.Options
import com.github.takahirom.roborazzi.captureScreenRoboImage
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched.data.di.RepositoryProvider
import io.github.droidkaigi.confsched.testing.HiltTestActivity
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.robolectric.shadows.ShadowLog
import org.robolectric.util.ReflectionHelpers
import javax.inject.Inject
import kotlin.reflect.KClass

fun RobotTestRule(
    testInstance: Any,
    bundle: Bundle? = null,
): RobotTestRule {
    return RobotTestRule<HiltTestActivity>(
        HiltTestActivity::class,
        testInstance,
        bundle,
    )
}

inline fun <reified T : ComponentActivity> RobotTestRule(
    activityClass: KClass<T>,
    testInstance: Any,
    bundle: Bundle? = null,
): RobotTestRule {
    val composeTestRule =
        AndroidComposeTestRule<ActivityScenarioRule<T>, T>(
            activityRule = ActivityScenarioRule(
                Intent(
                    ApplicationProvider.getApplicationContext(),
                    activityClass.java,
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    if (bundle != null) {
                        putExtras(bundle)
                    }
                },
            ),
            activityProvider = { rule ->
                var activity: T? = null
                rule.scenario.onActivity { activity = it }
                if (activity == null) {
                    error("Activity was not set in the ActivityScenarioRule!")
                }
                return@AndroidComposeTestRule activity as T
            },
        )
    return RobotTestRule(
        testInstance,
        composeTestRule as AndroidComposeTestRule<ActivityScenarioRule<*>, *>,
    )
}

class RobotTestRule(
    private val testInstance: Any,
    val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<*>, *>,
) : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return RuleChain
            .outerRule(HiltAndroidAutoInjectRule(testInstance))
            .around(CoroutinesTestRule())
            .around(TimeZoneTestRule())
            .around(object : TestWatcher() {
                override fun starting(description: Description?) {
                    super.starting(description)
                    // https://github.com/robolectric/robolectric/issues/9043#issuecomment-2125998323
                    val uiDispatcher: AndroidUiDispatcher = ReflectionHelpers.getField(AndroidUiDispatcher.Main, "element")
                    ReflectionHelpers.setField(uiDispatcher, "choreographer", Choreographer.getInstance())
                }
            })
            .around(object : TestWatcher() {
                override fun starting(description: Description) {
                    // To see logs in the console
                    Logger.setLogWriters(CommonWriter())
                    ShadowLog.stream = System.out
                }
            })
            .around(
                RoborazziRule(
                    Options(
                        roborazziOptions = RoborazziOptions(
                            compareOptions = CompareOptions(
                                // Detect small changes
                                changeThreshold = 0.000001F,
                            ),
                            recordOptions = RecordOptions(
                                // For saving money
                                pixelBitConfig = PixelBitConfig.Rgb565,
                            ),
                        ),
                    ),
                ),
            )
            .around(CoilRule())
            .around(composeTestRule)
            .apply(base, description)
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface RepositoryProviderEntryPoint {
        fun getRepositoryProvider(): RepositoryProvider
    }

    fun setContent(content: @Composable () -> Unit) {
        val repositoryProvider = EntryPoints.get(
            composeTestRule.activity.application,
            RepositoryProviderEntryPoint::class.java,
        )
            .getRepositoryProvider()
        composeTestRule.setContent {
            repositoryProvider.Provide {
                content()
            }
        }
    }

    fun setContentWithNavigation(
        startDestination: String = "startDestination",
        route: String = "startDestination",
        content: @Composable () -> Unit,
    ) {
        setContent {
            NavHost(
                navController = rememberNavController(),
                startDestination = startDestination,
            ) {
                composable(route) {
                    content()
                }
            }
        }
    }

    inline fun <reified T : Any> setContentWithNavigation(
        crossinline startDestination: () -> T,
        crossinline content: @Composable () -> Unit,
    ) {
        setContent {
            NavHost(
                navController = rememberNavController(),
                startDestination = startDestination(),
            ) {
                composable<T> {
                    content()
                }
            }
        }
    }

    fun captureScreen(name: String? = null) {
        if (name != null) {
            captureScreenRoboImage("$name.png")
        } else {
            captureScreenRoboImage()
        }
    }
}

class RobotTestEnvironment @Inject constructor(
    private val robotTestRule: RobotTestRule,
    private val repositoryProvider: RepositoryProvider,
) {
    val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<*>, *>
        get() = robotTestRule.composeTestRule

    fun setContent(content: @Composable () -> Unit) {
        composeTestRule.setContent {
            repositoryProvider.Provide {
                content()
            }
        }
    }
}
