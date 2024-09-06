package io.github.droidkaigi.confsched.profilecard

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.CropImageScreenRobot
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.rules.RobotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class CropImageScreenTest(
    private val testCase: DescribedBehavior<CropImageScreenRobot>,
) {

    @get:Rule
    @BindValue
    val robotTestRule: RobotTestRule = RobotTestRule(testInstance = this)

    @Inject
    lateinit var cropImageScreenRobot: CropImageScreenRobot

    @Test
    fun runTest() {
        runRobot(cropImageScreenRobot) {
            testCase.execute(cropImageScreenRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<CropImageScreenRobot>> {
            return describeBehaviors(name = "CropImageScreen") {
                describe("when launch") {
                    doIt {
                        setupScreenContent()
                    }
                    itShould("show screen") {
                        captureScreenWithChecks {
                            checkScreenDisplayed()
                        }
                    }
                }
            }
        }
    }
}
