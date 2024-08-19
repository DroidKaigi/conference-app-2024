package io.github.droidkaigi.confsched.sessions

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.TimetableScreenRobot
import io.github.droidkaigi.confsched.testing.robot.TimetableServerRobot.ServerStatus
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.rules.RobotTestRule
import kotlinx.datetime.LocalDate
import kotlinx.datetime.atTime
import kotlinx.datetime.format
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class TimetableScreenTest(private val testCase: DescribedBehavior<TimetableScreenRobot>) {

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
        fun behaviors(): List<DescribedBehavior<TimetableScreenRobot>> {
            return describeBehaviors<TimetableScreenRobot>(name = "TimetableScreen") {
                describe("when server is operational") {
                    run {
                        setupTimetableServer(ServerStatus.Operational)
                        setupTimetableScreenContent()
                    }
                    itShould("show timetable items") {
                        captureScreenWithChecks(checks = {
                            checkTimetableListDisplayed()
                            checkTimetableListItemsDisplayed()
                            checkTimetableTabSelected(DroidKaigi2024Day.ConferenceDay1)
                        })
                    }
                    describe("click first session bookmark") {
                        run {
                            clickFirstSessionBookmark()
                        }
                        itShould("show bookmarked session") {
                            captureScreenWithChecks(checks = {
                                checkFirstSessionBookmarkedIconDisplayed()
                            })
                        }
                    }
                    describe("click first session") {
                        run {
                            clickFirstSession()
                        }
                        itShould("show session detail") {
                            checkClickedItemsExists()
                        }
                    }
                    describe("scroll timetable") {
                        run {
                            scrollTimetable()
                        }
                        itShould("first session is not displayed") {
                            captureScreenWithChecks(checks = {
                                checkTimetableListFirstItemNotDisplayed()
                            })
                        }
                    }
                    describe("click conference day2 tab") {
                        run {
                            clickTimetableTab(2)
                        }
                        itShould("change displayed day") {
                            captureScreenWithChecks(checks = {
                                checkTimetableListItemsDisplayed()
                            })
                        }
                    }
                    describe("click timetable ui type change") {
                        run {
                            clickTimetableUiTypeChangeButton()
                        }
                        itShould("change timetable ui type") {
                            captureScreenWithChecks(checks = {
                                checkTimetableGridDisplayed()
                                checkTimetableGridItemsDisplayed()
                            })
                        }
                        describe("scroll timetable") {
                            run {
                                scrollTimetable()
                            }
                            itShould("first session is not displayed") {
                                captureScreenWithChecks(checks = {
                                    checkTimetableGridFirstItemNotDisplayed()
                                })
                            }
                        }
                        describe("click conference day2 tab") {
                            run {
                                clickTimetableTab(2)
                            }
                            itShould("change displayed day") {
                                captureScreenWithChecks(checks = {
                                    checkTimetableGridItemsDisplayed()
                                })
                            }
                        }
                    }
                }
                listOf(
                    InitialTabTestSpec(
                        date = LocalDate(2024, 9, 11),
                        expectedInitialTab = DroidKaigi2024Day.ConferenceDay1,
                    ),
                    InitialTabTestSpec(
                        date = LocalDate(2024, 9, 12),
                        expectedInitialTab = DroidKaigi2024Day.ConferenceDay1,
                    ),
                    InitialTabTestSpec(
                        date = LocalDate(2024, 9, 13),
                        expectedInitialTab = DroidKaigi2024Day.ConferenceDay2,
                    ),
                ).forEach { case ->
                    describe("when the current date is ${case.date.format(LocalDate.Formats.ISO)}") {
                        run {
                            setupTimetableServer(ServerStatus.Operational)
                            setupTimetableScreenContent(case.date.atTime(10, 0))
                        }
                        itShould("show timetable items for ${case.expectedInitialTab.name}") {
                            captureScreenWithChecks(checks = {
                                checkTimetableListDisplayed()
                                checkTimetableListItemsDisplayed()
                                checkTimetableTabSelected(case.expectedInitialTab)
                            })
                        }
                        describe("switch to grid timetable") {
                            run {
                                clickTimetableUiTypeChangeButton()
                            }
                            itShould("show timetable items for ${case.expectedInitialTab.name}") {
                                captureScreenWithChecks(checks = {
                                    checkTimetableGridDisplayed()
                                    checkTimetableGridItemsDisplayed()
                                    checkTimetableTabSelected(case.expectedInitialTab)
                                })
                            }
                        }
                    }
                }
                describe("when server is down") {
                    run {
                        setupTimetableServer(ServerStatus.Error)
                        setupTimetableScreenContent()
                    }
                    itShould("show error message") {
                        captureScreenWithChecks(checks = {
                            checkErrorSnackbarDisplayed()
                        })
                    }
                }
            }
        }
    }
}

private data class InitialTabTestSpec(
    val date: LocalDate,
    val expectedInitialTab: DroidKaigi2024Day,
)
