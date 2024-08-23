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
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
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
        @OptIn(FormatStringsInDatetimeFormats::class)
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<TimetableScreenRobot>> {
            return describeBehaviors<TimetableScreenRobot>(name = "TimetableScreen") {
                describe("when server is operational") {
                    doIt {
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
                        doIt {
                            clickFirstSessionBookmark()
                        }
                        itShould("show bookmarked session") {
                            captureScreenWithChecks(checks = {
                                checkFirstSessionBookmarkedIconDisplayed()
                            })
                        }
                    }
                    describe("click first session") {
                        doIt {
                            clickFirstSession()
                        }
                        itShould("show session detail") {
                            checkClickedItemsExists()
                        }
                    }
                    describe("scroll timetable") {
                        doIt {
                            scrollTimetable()
                        }
                        itShould("first session is not displayed") {
                            captureScreenWithChecks(checks = {
                                checkTimetableListFirstItemNotDisplayed()
                            })
                        }
                    }
                    describe("click conference day2 tab") {
                        doIt {
                            clickTimetableTab(2)
                        }
                        itShould("change displayed day") {
                            captureScreenWithChecks(checks = {
                                checkTimetableListItemsDisplayed()
                            })
                        }
                    }
                    describe("click timetable ui type change") {
                        doIt {
                            clickTimetableUiTypeChangeButton()
                        }
                        itShould("change timetable ui type") {
                            captureScreenWithChecks(checks = {
                                checkTimetableGridDisplayed()
                                checkTimetableGridItemsDisplayed()
                            })
                        }
                        describe("scroll timetable") {
                            doIt {
                                scrollTimetable()
                            }
                            itShould("first session is not displayed") {
                                captureScreenWithChecks(checks = {
                                    checkTimetableGridFirstItemNotDisplayed()
                                })
                            }
                        }
                        describe("click conference day2 tab") {
                            doIt {
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
                        doIt {
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
                            doIt {
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
                listOf(
                    TimeLineTestSpec(
                        dateTime = LocalDateTime(
                            year = 2024,
                            monthNumber = 9,
                            dayOfMonth = 11,
                            hour = 10,
                            minute = 0,
                        ),
                        shouldShowTimeLine = false,
                    ),
                    TimeLineTestSpec(
                        dateTime = LocalDateTime(
                            year = 2024,
                            monthNumber = 9,
                            dayOfMonth = 12,
                            hour = 10,
                            minute = 30,
                        ),
                        shouldShowTimeLine = true,
                    ),
                    TimeLineTestSpec(
                        dateTime = LocalDateTime(
                            year = 2024,
                            monthNumber = 9,
                            dayOfMonth = 13,
                            hour = 11,
                            minute = 0,
                        ),
                        shouldShowTimeLine = true,
                    ),
                ).forEach { case ->
                    val formattedDateTime =
                        case.dateTime.format(LocalDateTime.Format { byUnicodePattern("yyyy-MM-dd HH-mm") })
                    describe("when the current datetime is $formattedDateTime") {
                        doIt {
                            setupTimetableServer(ServerStatus.Operational)
                            setupTimetableScreenContent(case.dateTime)
                            clickTimetableUiTypeChangeButton()
                        }

                        val formattedTime =
                            case.dateTime.time.format(LocalTime.Format { byUnicodePattern("HH-mm") })
                        val description = if (case.shouldShowTimeLine) {
                            "show an indicator of the current time at $formattedTime"
                        } else {
                            "not show an indicator of the current time"
                        }
                        itShould(description) {
                            captureScreenWithChecks {
                                checkTimetableGridDisplayed()
                            }
                        }
                    }
                }
                describe("when server is down") {
                    doIt {
                        setupTimetableServer(ServerStatus.Error)
                        setupTimetableScreenContent()
                    }
                    itShould("show error message") {
                        captureScreenWithChecks(checks = {
                            checkErrorSnackbarDisplayed()
                        })
                    }
                }
                describe("when device is tablet") {
                    doIt {
                        setupTabletDevice()
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
                }
            }
        }
    }
}

private data class InitialTabTestSpec(
    val date: LocalDate,
    val expectedInitialTab: DroidKaigi2024Day,
)

private data class TimeLineTestSpec(
    val dateTime: LocalDateTime,
    val shouldShowTimeLine: Boolean,
)
