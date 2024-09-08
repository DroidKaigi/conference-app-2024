package io.github.droidkaigi.confsched.sessions

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.core.SearchScreenCoreRobot
import io.github.droidkaigi.confsched.testing.robot.core.SearchScreenCoreRobot.Category
import io.github.droidkaigi.confsched.testing.robot.core.SearchScreenCoreRobot.ConferenceDay
import io.github.droidkaigi.confsched.testing.robot.TimetableItemCardRobot.Language
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
    private val testCase: DescribedBehavior<SearchScreenCoreRobot>,
) {
    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(testInstance = this)

    @Inject
    lateinit var searchScreenCoreRobot: SearchScreenCoreRobot

    @Test
    fun runTest() {
        runRobot(searchScreenCoreRobot) {
            testCase.execute(searchScreenCoreRobot)
        }
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<SearchScreenCoreRobot>> {
            return describeBehaviors<SearchScreenCoreRobot>(name = "SearchScreen") {
                describe("when server is operational") {
                    doIt {
                        setupTimetableServer(ServerStatus.Operational)
                        setupSearchScreenContent()
                    }
                    itShould("no timetable items are displayed") {
                        captureScreenWithChecks {
                            runRobot(verifyRobot) {
                                checkTimetableListExists()
                                checkTimetableListItemsNotDisplayed()
                            }
                        }
                    }
                    describe("input search word to TextField") {
                        doIt {
                            runRobot(actionRobot) {
                                inputDemoSearchWord()
                            }
                        }
                        itShould("show search word and filtered items") {
                            captureScreenWithChecks {
                                runRobot(verifyRobot) {
                                    checkDemoSearchWordDisplayed()
                                    checkTimetableListItemsHasDemoText()
                                    checkTimetableListDisplayed()
                                    checkTimetableListItemsDisplayed()
                                }
                            }
                        }
                    }
                    describe("when filter day chip click") {
                        doIt {
                            runRobot(actionRobot) {
                                clickFilterDayChip()
                            }
                        }
                        itShould("show drop down menu") {
                            captureScreenWithChecks {
                                runRobot(verifyRobot) {
                                    checkDisplayedFilterDayChip()
                                }
                            }
                        }
                        ConferenceDay.entries.forEach { conference ->
                            describe("when click conference day ${conference.day}") {
                                doIt {
                                    runRobot(actionRobot) {
                                        clickConferenceDay(
                                            clickDay = conference,
                                        )
                                    }
                                }
                                itShould("selected day ${conference.day}") {
                                    captureScreenWithChecks {
                                        runRobot(verifyRobot) {
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
                    }
                    describe("when filter category chip click") {
                        doIt {
                            runRobot(actionRobot) {
                                clickFilterCategoryChip()
                            }
                        }
                        itShould("show drop down menu") {
                            captureScreenWithChecks {
                                runRobot(verifyRobot) {
                                    checkDisplayedFilterCategoryChip()
                                }
                            }
                        }
                        Category.entries.forEach { category ->
                            describe("when click category ${category.categoryName}") {
                                doIt {
                                    runRobot(actionRobot) {
                                        clickCategory(
                                            category = category,
                                        )
                                    }
                                }
                                itShould("selected category ${category.categoryName}") {
                                    captureScreenWithChecks {
                                        runRobot(verifyRobot) {
                                            checkTimetableListItemByCategory(category)
                                            checkTimetableListDisplayed()
                                            checkTimetableListItemsDisplayed()
                                        }
                                    }
                                }
                            }
                        }
                    }
                    describe("when filter language chip click") {
                        doIt {
                            runRobot(actionRobot) {
                                scrollToFilterLanguageChip()
                                clickFilterLanguageChip()
                            }
                        }
                        itShould("show drop down menu") {
                            captureScreenWithChecks {
                                runRobot(verifyRobot) {
                                    checkDisplayedFilterLanguageChip()
                                }
                            }
                        }
                        Language.entries.forEach { language ->
                            describe("when click language ${language.name}") {
                                doIt {
                                    runRobot(actionRobot) {
                                        clickLanguage(
                                            language = language,
                                        )
                                    }
                                }
                                itShould("selected language ${language.name}") {
                                    captureScreenWithChecks {
                                        checkTimetableListItemByLanguage(language)
                                        runRobot(verifyRobot) {
                                            checkTimetableListDisplayed()
                                            checkTimetableListItemsDisplayed()
                                        }
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
                            runRobot(verifyRobot) {
                                checkTimetableListExists()
                                checkTimetableListItemsNotDisplayed()
                            }
                        }
                    }

                    describe("input search word to TextField") {
                        doIt {
                            runRobot(actionRobot) {
                                inputDemoSearchWord()
                            }
                        }
                        itShould("show search word and filtered items") {
                            captureScreenWithChecks {
                                runRobot(verifyRobot) {
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
}
