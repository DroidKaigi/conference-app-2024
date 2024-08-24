package io.github.droidkaigi.confsched.sessions

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.SearchScreenRobot
import io.github.droidkaigi.confsched.testing.robot.SearchScreenRobot.Category
import io.github.droidkaigi.confsched.testing.robot.SearchScreenRobot.ConferenceDay
import io.github.droidkaigi.confsched.testing.robot.SearchScreenRobot.Language
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
                    doIt {
                        setupTimetableServer(ServerStatus.Operational)
                        setupSearchScreenContent()
                    }
                    itShould("no timetable items are displayed") {
                        captureScreenWithChecks {
                            checkTimetableListExists()
                            checkTimetableListItemsNotDisplayed()
                        }
                    }
                    describe("input search word to TextField") {
                        doIt {
                            inputDemoSearchWord()
                        }
                        itShould("show search word and filtered items") {
                            captureScreenWithChecks {
                                checkDemoSearchWordDisplayed()
                                checkTimetableListItemsHasDemoText()
                                checkTimetableListDisplayed()
                                checkTimetableListItemsDisplayed()
                            }
                        }
                    }
                    describe("when filter day chip click") {
                        doIt {
                            clickFilterDayChip()
                        }
                        itShould("show drop down menu") {
                            captureScreenWithChecks {
                                checkDisplayedFilterDayChip()
                            }
                        }
                        ConferenceDay.entries.forEach { conference ->
                            describe("when click conference day ${conference.day}") {
                                doIt {
                                    clickConferenceDay(
                                        clickDay = conference,
                                    )
                                }
                                itShould("selected day ${conference.day}") {
                                    captureScreenWithChecks {
                                        checkTimetableListItemByConferenceDay(
                                            checkDay = conference,
                                        )
                                        checkTimetableListDisplayed()
                                        checkTimetableListItemsDisplayed()
                                    }
                                }
                            }
                        }
                    }
                    describe("when filter category chip click") {
                        doIt {
                            clickFilterCategoryChip()
                        }
                        itShould("show drop down menu") {
                            captureScreenWithChecks {
                                checkDisplayedFilterCategoryChip()
                            }
                        }
                        Category.entries.forEach { category ->
                            describe("when click category ${category.categoryName}") {
                                doIt {
                                    clickCategory(
                                        category = category,
                                    )
                                }
                                itShould("selected category ${category.categoryName}") {
                                    captureScreenWithChecks {
                                        checkTimetableListItemByCategory(category)
                                        checkTimetableListDisplayed()
                                        checkTimetableListItemsDisplayed()
                                    }
                                }
                            }
                        }
                    }
                    describe("when filter language chip click") {
                        doIt {
                            scrollToFilterLanguageChip()
                            clickFilterLanguageChip()
                        }
                        itShould("show drop down menu") {
                            captureScreenWithChecks {
                                checkDisplayedFilterLanguageChip()
                            }
                        }
                        Language.entries.forEach { language ->
                            describe("when click language ${language.name}") {
                                doIt {
                                    clickLanguage(
                                        language = language,
                                    )
                                }
                                itShould("selected language ${language.name}") {
                                    captureScreenWithChecks {
                                        checkTimetableListItemByLanguage(language)
                                        checkTimetableListDisplayed()
                                        checkTimetableListItemsDisplayed()
                                    }
                                }
                            }
                        }
                    }
                }

                describe("when device is tablet") {
                    doIt {
                        setupTabletDevice()
                        setupTimetableServer(ServerStatus.Operational)
                        setupSearchScreenContent()
                    }
                    itShould("no timetable items are displayed") {
                        captureScreenWithChecks {
                            checkTimetableListExists()
                            checkTimetableListItemsNotDisplayed()
                        }
                    }

                    describe("input search word to TextField") {
                        doIt {
                            inputDemoSearchWord()
                        }
                        itShould("show search word and filtered items") {
                            captureScreenWithChecks {
                                checkDemoSearchWordDisplayed()
                                checkTimetableListItemsHasDemoText()
                                checkTimetableListDisplayed()
                                checkTimetableListItemsDisplayed()
                            }
                        }
                    }
                }
            }
        }
    }
}
