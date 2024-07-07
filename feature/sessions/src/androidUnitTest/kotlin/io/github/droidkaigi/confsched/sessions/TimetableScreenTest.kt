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
        fun testCases(): List<DescribedTestCase<TimetableScreenRobot>> {
            return describeTests<TimetableScreenRobot> {
                describe("when server is operational") {
                    run {
                        setupTimetableServer(ServerStatus.Operational)
                        setupTimetableScreenContent()
                    }
                    it("should show timetable items") {
                        captureScreenWithChecks(checks = {
                            checkTimetableItemsDisplayed()
                        })
                    }
                    describe("click first session bookmark") {
                        run {
                            clickFirstSessionBookmark()
                        }
                        it("should show bookmarked session") {
                            // FIXME: Add check for bookmarked session
                            captureScreenWithChecks()
                        }
                    }
                    describe("click first session") {
                        run {
                            clickFirstSession()
                        }
                        it("should show session detail") {
                            checkClickedItemsExists()
                        }
                    }
                    describe("click timetable ui type change button") {
                        run {
                            clickTimetableUiTypeChangeButton()
                        }
                        it("should change timetable ui type") {
                            // FIXME: Add check for timetable ui type change
                            captureScreenWithChecks()
                        }
                    }
                }
                describe("when server is down") {
                    run {
                        setupTimetableServer(ServerStatus.Error)
                        setupTimetableScreenContent()
                    }
                    it("should show error message") {
                        // FIXME: Add check for error message
                        captureScreenWithChecks()
                    }
                }
            }
        }
    }
}
