package io.github.droidkaigi.confsched.data.profilecard

import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.ProfileCardType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProfileCardJsonTest {

    @Test
    fun `returns decoded ProfileCardJson from v1 formatted json`() {
        val jsonString = readJsonString(1)
        val profileCardJson = Json.decodeFromString<ProfileCardJson>(jsonString)

        assertEquals("test nickname", profileCardJson.nickname)
        assertEquals("test occupation", profileCardJson.occupation)
        assertEquals("https://github.com/DroidKaigi/conference-app-2024", profileCardJson.link)
        assertEquals("BASE64_ENCODED_SAMPLE_IMAGE_STRING", profileCardJson.image)
        assertEquals("Iguana", profileCardJson.theme)
        assertNull(profileCardJson.cardType)
    }

    @Test
    fun `returns ProfileCard from v1 formatted json`() {
        val jsonString = readJsonString(1)
        val profileCard = Json.decodeFromString<ProfileCardJson>(jsonString).toModel()

        assertProfileCard(profileCard)
    }

    @Test
    fun `returns decoded ProfileCardJson from v2 formatted json`() {
        val jsonString = readJsonString(2)
        val profileCardJson = Json.decodeFromString<ProfileCardJson>(jsonString)
        assertEquals("test nickname", profileCardJson.nickname)
        assertEquals("test occupation", profileCardJson.occupation)
        assertEquals("https://github.com/DroidKaigi/conference-app-2024", profileCardJson.link)
        assertEquals("BASE64_ENCODED_SAMPLE_IMAGE_STRING", profileCardJson.image)
        assertNull(profileCardJson.theme)
        assertEquals("Iguana", profileCardJson.cardType)
    }

    @Test
    fun `returns ProfileCard from v2 formatted json`() {
        val jsonString = readJsonString(2)
        val profileCard = Json.decodeFromString<ProfileCardJson>(jsonString).toModel()

        assertProfileCard(profileCard)
    }

    @Test
    @OptIn(ExperimentalSerializationApi::class)
    fun `returns serialized json string from ProfileCard`() {
        val profileCard = ProfileCard.Exists(
            nickname = "test nickname",
            occupation = "test occupation",
            link = "https://github.com/DroidKaigi/conference-app-2024",
            image = "BASE64_ENCODED_SAMPLE_IMAGE_STRING",
            cardType = ProfileCardType.Iguana,
        )
        val json = Json {
            prettyPrint = true
            prettyPrintIndent = "  "
        }
        val jsonString = json.encodeToString(profileCard.toJson())

        assertSerializedJson(jsonString)
    }

    private fun readJsonString(version: Int): String {
        return FileSystem.SYSTEM.read("src/commonTest/resources/profilecard/v${version}.json".toPath()) {
            readUtf8()
        }
    }

    private fun assertProfileCard(profileCard: ProfileCard.Exists) {
        assertEquals("test nickname", profileCard.nickname)
        assertEquals("test occupation", profileCard.occupation)
        assertEquals("https://github.com/DroidKaigi/conference-app-2024", profileCard.link)
        assertEquals("BASE64_ENCODED_SAMPLE_IMAGE_STRING", profileCard.image)
        assertEquals(ProfileCardType.Iguana, profileCard.cardType)
    }

    private fun assertSerializedJson(actual: String) {
        val expected =
            FileSystem.SYSTEM.read("src/commonTest/resources/profilecard/v2.json".toPath()) {
                readUtf8()
            }

        assertEquals(expected.trim(), actual.trim())
    }
}
