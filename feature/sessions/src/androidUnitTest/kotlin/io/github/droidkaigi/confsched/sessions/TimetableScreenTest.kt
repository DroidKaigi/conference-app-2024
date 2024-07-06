package io.github.droidkaigi.confsched.sessions

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedTestCase
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.TimetableServerRobot.ServerStatus
import io.github.droidkaigi.confsched.testing.describeTests
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.TimetableScreenRobot
import io.github.droidkaigi.confsched.testing.runRobot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class TimetableScreenTest(private val testCase: DescribedTestCase<TimetableScreenRobot>) {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(this)

    @Inject
    lateinit var timetableScreenRobot: TimetableScreenRobot

    @Test
    fun runTest() {
        runRobot(timetableScreenRobot) {
            testCase.execute(timetableScreenRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun tests(): List<DescribedTestCase<TimetableScreenRobot>> {
            return describeTests<TimetableScreenRobot> {
                describe("when server is operational") {
                    run { robot ->
                        robot.setupTimetableServer(ServerStatus.Operational)
                        robot.setupTimetableScreenContent()
                    }
                    check("should show timetable items") { robot ->
                        robot.captureScreenWithChecks(checks = {
                            robot.checkTimetableItemsDisplayed()
                        })
                    }
                    describe("click first session bookmark") {
                        run { robot ->
                            robot.clickFirstSessionBookmark()
                        }
                        check("should show bookmarked session") { robot ->
                            // FIXME: Add check for bookmarked session
                            robot.captureScreenWithChecks()
                        }
                    }
                    describe("click first session") {
                        run { robot ->
                            robot.clickFirstSession()
                        }
                        check("should show session detail") { robot ->
                            // FIXME: Add check for navigation to session detail
                            robot.captureScreenWithChecks()
                        }
                    }
                    describe("click timetable ui type change button") {
                        run { robot ->
                            robot.clickTimetableUiTypeChangeButton()
                        }
                        check("should change timetable ui type") { robot ->
                            // FIXME: Add check for timetable ui type change
                            robot.captureScreenWithChecks()
                        }
                    }
                }
                describe("when server is down") {
                    run { robot ->
                        robot.setupTimetableServer(ServerStatus.Error)
                        robot.setupTimetableScreenContent()
                    }
                    check("should show error message") { robot ->
                        // FIXME: Add check for error message
                        robot.captureScreenWithChecks()
                    }
                }
            }
        }
    }
}
