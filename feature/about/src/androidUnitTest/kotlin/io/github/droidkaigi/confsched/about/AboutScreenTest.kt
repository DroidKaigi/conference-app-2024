package io.github.droidkaigi.confsched.about

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.AboutScreenRobot
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.rules.RobotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class AboutScreenTest(
    private val testCase: DescribedBehavior<AboutScreenRobot>,
) {

    @get:Rule
    @BindValue
    val robotTestRule: RobotTestRule = RobotTestRule(this)

    @Inject
    lateinit var aboutScreenRobot: AboutScreenRobot

    @Test
    fun runTest() {
        runRobot(aboutScreenRobot) {
            testCase.execute(aboutScreenRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<AboutScreenRobot>> {
            return describeBehaviors("AboutScreen") {
                describe("when launch") {
                    doIt {
                        setupScreenContent()
                    }
                    itShould("show detail section") {
                        captureScreenWithChecks {
                            checkDetailSectionDisplayed()
                        }
                    }
                }
            }
        }
    }
}
