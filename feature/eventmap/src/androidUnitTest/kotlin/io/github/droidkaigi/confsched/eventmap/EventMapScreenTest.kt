package io.github.droidkaigi.confsched.eventmap

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.EventMapScreenRobot
import io.github.droidkaigi.confsched.testing.robot.EventMapServerRobot.ServerStatus
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.rules.RobotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import javax.inject.Inject

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class EventMapScreenTest(val behavior: DescribedBehavior<EventMapScreenRobot>) {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(this)

    @Inject
    lateinit var eventMapScreenRobot: EventMapScreenRobot

    @Test
    fun test() = runRobot(eventMapScreenRobot) {
        behavior.execute(eventMapScreenRobot)
    }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<EventMapScreenRobot>> {
            return describeBehaviors<EventMapScreenRobot>(name = "EventMapScreenRobot") {
                describe("when regardless of server status") {
                    doIt {
                        setupScreenContent()
                    }
                    describe("when click floor level ground") {
                        doIt {
                            clickEventMapTabOnGround()
                        }
                        itShould("showed ground floor level map") {
                            captureScreenWithChecks {
                                checkEventMapOnGround()
                            }
                        }
                    }
                    describe("when click floor level basement") {
                        doIt {
                            clickEventMapTabOnBasement()
                        }
                        itShould("showed basement floor level map") {
                            captureScreenWithChecks {
                                checkEventMapOnBasement()
                            }
                        }
                    }
                }
                describe("when server is operational") {
                    doIt {
                        setupEventMapServer(ServerStatus.Operational)
                        setupScreenContent()
                        scrollToFlamingoRoomEvent()
                    }
                    itShould("ensure that the room types for Flamingo are displayed.") {
                        captureScreenWithChecks(
                            checks = {
                                checkEventMapItemFlamingo()
                            },
                        )
                    }
                    doIt {
                        scrollToGiraffeRoomEvent()
                    }
                    itShould("ensure that the room types for Giraffe are displayed.") {
                        captureScreenWithChecks(
                            checks = {
                                checkEventMapItemGiraffe()
                            },
                        )
                    }
                    doIt {
                        scrollToHedgehogRoomEvent()
                    }
                    itShould("ensure that the room types for Hedgehog are displayed.") {
                        captureScreenWithChecks(
                            checks = {
                                checkEventMapItemHedgehog()
                            },
                        )
                    }
                    doIt {
                        scrollToIguanaRoomEvent()
                    }
                    itShould("ensure that the room types for Iguana are displayed.") {
                        captureScreenWithChecks(
                            checks = {
                                checkEventMapItemIguana()
                            },
                        )
                    }
                    doIt {
                        scrollToJellyfishRoomEvent()
                    }
                    itShould("ensure that the room types for Jellyfish are displayed.") {
                        captureScreenWithChecks(
                            checks = {
                                checkEventMapItemJellyfish()
                            },
                        )
                    }
                }
                describe("when server is error") {
                    doIt {
                        setupEventMapServer(ServerStatus.Error)
                        setupScreenContent()
                    }
                    itShould("show error message") {
                        captureScreenWithChecks(
                            checks = {
                                checkErrorSnackbarDisplayed()
                            },
                        )
                    }
                }
            }
        }
    }
}
