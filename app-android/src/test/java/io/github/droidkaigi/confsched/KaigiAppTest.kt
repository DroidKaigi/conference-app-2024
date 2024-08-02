package io.github.droidkaigi.confsched

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.KaigiAppRobot
import io.github.droidkaigi.confsched.testing.runRobot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class KaigiAppTest(private val testCase: DescribedBehavior<KaigiAppRobot>) {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(MainActivity::class, this)

    @Inject lateinit var kaigiAppRobot: KaigiAppRobot

    @Test
    fun runTest() {
        runRobot(kaigiAppRobot) {
            testCase.execute(kaigiAppRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<KaigiAppRobot>> {
            return describeBehaviors<KaigiAppRobot>(name = "KaigiApp") {
                describe("when app is starting") {
                    run {
                        waitUntilIdle()
                    }
                    itShould("show timetable items") {
                        captureScreenWithChecks {
                            runRobot(timetableScreenRobot) {
                                checkTimetableItemsDisplayed()
                            }
                        }
                    }
                }
                describe("when device is mediumTablet") {
                    run {
                        waitUntilIdle()
                    }
                    itShould("show screen correctly") {
                        captureScreenWithChecks()
                    }
                }
            }
        }
    }
}
