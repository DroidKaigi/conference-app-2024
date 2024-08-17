package io.github.droidkaigi.confsched.sessions

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.SearchScreenRobot
import io.github.droidkaigi.confsched.testing.robot.SearchScreenRobot.ConferenceDay
import io.github.droidkaigi.confsched.testing.robot.TimetableServerRobot.ServerStatus
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.rules.RobotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class SearchScreenTest(
    private val testCase: DescribedBehavior<SearchScreenRobot>,
) {
    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(testInstance = this)

    @Inject
    lateinit var searchScreenRobot: SearchScreenRobot

    @Test
    fun runTest() {
        runRobot(searchScreenRobot) {
            testCase.execute(searchScreenRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<SearchScreenRobot>> {
            return describeBehaviors<SearchScreenRobot>(name = "SearchScreen") {
                describe("when server is operational") {
                    run {
                        setupTimetableServer(ServerStatus.Operational)
                        setupSearchScreenContent()
                    }
                    itShould("show non-filtered timetable items") {
                        captureScreenWithChecks {
                            checkTimetableListDisplayed()
                            checkTimetableListItemsDisplayed()
                        }
                    }
                    describe("input search word to TextField") {
                        val searchWord = "Demo"
                        run {
                            inputSearchWord(searchWord)
                        }
                        itShould("show search word and filtered items") {
                            captureScreenWithChecks {
                                checkSearchWordDisplayed(searchWord)
                                checkTimetableListItemsHasText(searchWord)
                            }
                        }
                    }
                    describe("when filter day chip click") {
                        run {
                            clickFilterDayChip()
                        }
                        itShould("show drop down menu") {
                            captureScreenWithChecks {
                                checkDisplayedFilterDayChip()
                            }
                        }
                        ConferenceDay.entries.forEach { conference ->
                            describe("when click conference day ${conference.day}") {
                                run {
                                    clickConferenceDay(
                                        clickDay = conference,
                                    )
                                }
                                itShould("selected day ${conference.day}") {
                                    captureScreenWithChecks {
                                        checkTimetableListItemByConferenceDay(
                                            checkDay = conference,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
