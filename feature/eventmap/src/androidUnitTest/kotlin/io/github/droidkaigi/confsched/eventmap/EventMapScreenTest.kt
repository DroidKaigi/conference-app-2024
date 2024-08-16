package io.github.droidkaigi.confsched.eventmap

import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import io.github.droidkaigi.confsched.testing.describeBehaviors
import io.github.droidkaigi.confsched.testing.execute
import io.github.droidkaigi.confsched.testing.robot.EventMapScreenRobot
import io.github.droidkaigi.confsched.testing.robot.EventMapServerRobot.ServerStatus
import io.github.droidkaigi.confsched.testing.robot.runRobot
import io.github.droidkaigi.confsched.testing.robot.todoChecks
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
                    run {
                        setupScreenContent()
                    }
                    describe("when click floor level ground") {
                        run {
                            clickEventMapTab("1F")
                        }
                        itShould("showed ground floor level map") {
                            captureScreenWithChecks {
                                checkEventMap("1F")
                            }
                        }
                    }
                    describe("when click floor level basement") {
                        run {
                            clickEventMapTab("B1F")
                        }
                        itShould("showed basement floor level map") {
                            captureScreenWithChecks {
                                checkEventMap("B1F")
                            }
                        }
                    }
                }
                describe("when server is operational") {
                    run {
                        setupEventMapServer(ServerStatus.Operational)
                        setupScreenContent()
                        scrollLazyColumnByIndex(1)
                    }
                    itShould("ensure that the room types for Flamingo and Giraffe are displayed.") {
                        captureScreenWithChecks(
                            checks = {
                                checkEventMapItemByRoomName(roomName = "Flamingo")
                                checkEventMapItemByRoomName(roomName = "Giraffe")
                            },
                        )
                    }
                    run {
                        scrollLazyColumnByIndex(3)
                    }
                    itShould("ensure that the room types for Hedgehog and Iguana are displayed.") {
                        captureScreenWithChecks(
                            checks = {
                                checkEventMapItemByRoomName(roomName = "Hedgehog")
                                checkEventMapItemByRoomName(roomName = "Iguana")
                            },
                        )
                    }
                    run {
                        scrollLazyColumnByIndex(4)
                    }
                    itShould("ensure that the room types for Jellyfish are displayed.") {
                        captureScreenWithChecks(
                            checks = { checkEventMapItemByRoomName(roomName = "Jellyfish") },
                        )
                    }
                }
                describe("when server is error") {
                    run {
                        setupEventMapServer(ServerStatus.Error)
                        setupScreenContent()
                    }
                    itShould("show error message") {
                        captureScreenWithChecks(
                            checks = todoChecks("This screen is still empty now. Please add some checks."),
                        )
                    }
                }
            }
        }
    }
}
