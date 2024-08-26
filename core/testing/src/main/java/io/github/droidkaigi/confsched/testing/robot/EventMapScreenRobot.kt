package io.github.droidkaigi.confsched.testing.robot

import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.droidkaigiui.Inject
import io.github.droidkaigi.confsched.eventmap.EventMapItemTestTag
import io.github.droidkaigi.confsched.eventmap.EventMapLazyColumnTestTag
import io.github.droidkaigi.confsched.eventmap.EventMapScreen
import io.github.droidkaigi.confsched.eventmap.component.EventMapTabImageTestTag
import io.github.droidkaigi.confsched.eventmap.component.EventMapTabTestTagPrefix

class EventMapScreenRobot @Inject constructor(
    screenRobot: DefaultScreenRobot,
    eventMapServerRobot: DefaultEventMapServerRobot,
) : ScreenRobot by screenRobot,
    EventMapServerRobot by eventMapServerRobot {
    private enum class FloorLevel(
        val floorName: String,
    ) {
        Basement("B1F"),
        Ground("1F"),
    }

    private enum class RoomType {
        Flamingo,
        Giraffe,
        Hedgehog,
        Iguana,
        Jellyfish,
    }

    fun setupScreenContent() {
        robotTestRule.setContent {
            KaigiTheme {
                EventMapScreen(
                    onEventMapItemClick = { },
                )
            }
        }
        waitUntilIdle()
    }

    fun scrollToFlamingoRoomEvent() {
        scrollLazyColumnByRoomName(RoomType.Flamingo)
    }

    fun scrollToGiraffeRoomEvent() {
        scrollLazyColumnByRoomName(RoomType.Giraffe)
    }

    fun scrollToHedgehogRoomEvent() {
        scrollLazyColumnByRoomName(RoomType.Hedgehog)
    }

    fun scrollToIguanaRoomEvent() {
        scrollLazyColumnByRoomName(RoomType.Iguana)
    }

    fun scrollToJellyfishRoomEvent() {
        scrollLazyColumnByRoomName(RoomType.Jellyfish)
    }

    private fun scrollLazyColumnByRoomName(
        roomType: RoomType,
    ) {
        composeTestRule
            .onNode(hasTestTag(EventMapLazyColumnTestTag))
            .performScrollToNode(hasTestTag(EventMapItemTestTag.plus(roomType.name)))
        wait5Seconds()
    }

    fun clickEventMapTabOnGround() {
        clickEventMapTab(FloorLevel.Ground)
    }

    fun clickEventMapTabOnBasement() {
        clickEventMapTab(FloorLevel.Basement)
    }

    private fun clickEventMapTab(
        floorLevel: FloorLevel,
    ) {
        composeTestRule
            .onNode(hasTestTag(EventMapTabTestTagPrefix.plus(floorLevel.floorName)))
            .performClick()
        waitUntilIdle()
    }

    fun checkEventMapItemFlamingo() {
        checkEventMapItemByRoomName(roomType = RoomType.Flamingo)
    }

    fun checkEventMapItemGiraffe() {
        checkEventMapItemByRoomName(roomType = RoomType.Giraffe)
    }

    fun checkEventMapItemHedgehog() {
        checkEventMapItemByRoomName(roomType = RoomType.Hedgehog)
    }

    fun checkEventMapItemIguana() {
        checkEventMapItemByRoomName(roomType = RoomType.Iguana)
    }

    fun checkEventMapItemJellyfish() {
        checkEventMapItemByRoomName(roomType = RoomType.Jellyfish)
    }

    private fun checkEventMapItemByRoomName(
        roomType: RoomType,
    ) {
        composeTestRule
            .onAllNodes(hasTestTag(EventMapItemTestTag.plus(roomType.name)))
            .onFirst()
            .assertExists()
    }

    fun checkEventMapOnGround() {
        checkEventMap(FloorLevel.Ground)
    }

    fun checkEventMapOnBasement() {
        checkEventMap(FloorLevel.Basement)
    }

    private fun checkEventMap(
        floorLevel: FloorLevel,
    ) {
        composeTestRule
            .onNode(hasTestTag(EventMapTabImageTestTag))
            .assertContentDescriptionEquals("Map of ${floorLevel.floorName}")
    }
}
