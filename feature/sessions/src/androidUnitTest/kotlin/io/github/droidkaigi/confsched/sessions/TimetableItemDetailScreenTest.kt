package io.github.droidkaigi.confsched.sessions

import android.os.Bundle
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedTestCase
import io.github.droidkaigi.confsched.testing.RobotTestRule
import io.github.droidkaigi.confsched.testing.TimetableServerRobot.ServerStatus
import io.github.droidkaigi.confsched.testing.describeTests
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.TimetableItemDetailScreenRobot
import io.github.droidkaigi.confsched.testing.runRobot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class TimetableItemDetailScreenTest(private val testCase: DescribedTestCase<TimetableItemDetailScreenRobot>) {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(
        testInstance = this,
        bundle = Bundle().apply {
            putString(
                timetableItemDetailScreenRouteItemIdParameterName,
                TimetableItemDetailScreenRobot.defaultSessionId,
            )
        },
    )

    @Inject
    lateinit var timetableItemDetailScreenRobot: TimetableItemDetailScreenRobot

    @Test
    fun checkLaunchShot() {
        runRobot(timetableItemDetailScreenRobot) {
            testCase.execute(timetableItemDetailScreenRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun testCases(): List<DescribedTestCase<TimetableItemDetailScreenRobot>> {
            return describeTests<TimetableItemDetailScreenRobot> {
                describe("when server is operational") {
                    run {
                        setupTimetableServer(ServerStatus.Operational)
                    }
                    describe("when launch") {
                        run {
                            setupScreenContent()
                        }
                        check("should show session detail title") {
                            // FIXME: Add check for session detail title
                            captureScreenWithChecks()
                        }
                        check("check accessibility") {
                            checkAccessibilityCapture()
                        }
                        describe("click bookmark button") {
                            run {
                                clickBookmarkButton()
                            }
                            check("should show bookmarked session") {
                                // FIXME: Add check for bookmarked session
                                captureScreenWithChecks()
                            }
                            describe("click bookmark button again") {
                                run {
                                    clickBookmarkButton()
                                }
                                check("should show unbookmarked session") {
                                    // FIXME: Add check for unbookmarked session
                                    captureScreenWithChecks()
                                }
                            }
                        }
                        describe("scroll") {
                            run {
                                scroll()
                            }
                            check("should show scrolled session detail") {
                                // FIXME: Add check for scrolled session detail
                                captureScreenWithChecks()
                            }
                        }
                    }
                    describe("when font scale is small") {
                        run {
                            setFontScale(0.5f)
                            setupScreenContent()
                        }
                        check("should show session detail with small font scale") {
                            captureScreenWithChecks()
                        }
                    }
                    describe("when font scale is large") {
                        run {
                            setFontScale(1.5f)
                            setupScreenContent()
                        }
                        check("should show session detail with large font scale") {
                            captureScreenWithChecks()
                        }
                    }
                    describe("when font scale is huge") {
                        run {
                            setFontScale(2.0f)
                            setupScreenContent()
                        }
                        check("should show session detail with huge font scale") {
                            captureScreenWithChecks()
                        }
                    }
                }
            }
        }
    }
}
