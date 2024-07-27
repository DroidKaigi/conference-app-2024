package io.github.droidkaigi.confsched.data.sessions

import io.github.droidkaigi.confsched.data.sessions.response.CategoryItemResponse
import io.github.droidkaigi.confsched.data.sessions.response.CategoryResponse
import io.github.droidkaigi.confsched.data.sessions.response.LocaledResponse
import io.github.droidkaigi.confsched.data.sessions.response.RoomResponse
import io.github.droidkaigi.confsched.data.sessions.response.SessionAssetResponse
import io.github.droidkaigi.confsched.data.sessions.response.SessionResponse
import io.github.droidkaigi.confsched.data.sessions.response.SessionsAllResponse
import io.github.droidkaigi.confsched.data.sessions.response.SpeakerResponse
import io.github.droidkaigi.confsched.model.DroidKaigi2024Day
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toLocalDateTime
import okio.IOException
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

public class FakeSessionsApiClient : SessionsApiClient {

    public sealed class Status : SessionsApiClient {
        public data object Operational : Status() {
            override suspend fun sessionsAllResponse(): SessionsAllResponse {
                return SessionsAllResponse.fake()
            }
        }

        public data object Error : Status() {
            override suspend fun sessionsAllResponse(): SessionsAllResponse {
                throw IOException("Fake IO Exception")
            }
        }
    }

    private var status: Status = Status.Operational

    public fun setup(status: Status) {
        this.status = status
    }

    override suspend fun sessionsAllResponse(): SessionsAllResponse {
        return status.sessionsAllResponse()
    }
}

public fun SessionsAllResponse.Companion.fake(): SessionsAllResponse {
    val sessions = mutableListOf<SessionResponse>()
    val speakers = listOf(
        SpeakerResponse(fullName = "taka", id = "1", isTopSpeaker = true),
        SpeakerResponse(fullName = "ry", id = "2", isTopSpeaker = true),
    )
    val rooms = listOf(
        RoomResponse(name = LocaledResponse(ja = "Hedgehog ja", en = "Hedgehog"), id = 1, sort = 1),
        RoomResponse(
            name = LocaledResponse(ja = "Flamingo ja", en = "Flamingo"),
            id = 2,
            sort = 2,
        ),
        RoomResponse(
            name = LocaledResponse(ja = "Giraffe ja", en = "Giraffe"),
            id = 3,
            sort = 3,
        ),
        RoomResponse(name = LocaledResponse(ja = "Iguana ja", en = "Iguana"), id = 4, sort = 3),
    )
    val categories = listOf(
        CategoryResponse(
            id = 1,
            sort = 1,
            title = LocaledResponse(ja = "Category1 ja", en = "Category1 en"),
            items = listOf(
                CategoryItemResponse(
                    id = 1,
                    name = LocaledResponse(ja = "CategoryItem1 ja", en = "CategoryItem1 en"),
                    sort = 1,
                ),
                CategoryItemResponse(
                    id = 2,
                    name = LocaledResponse(ja = "CategoryItem2 ja", en = "CategoryItem2 en"),
                    sort = 2,
                ),
            ),
        ),
    )

    for (dayIndex in 0..2) {
        sessions.add(
            SessionResponse(
                id = "0570556a-8a53-49d6-916c-26ff85635d86",
                title = LocaledResponse(
                    ja = "Demo Welcome Talk $dayIndex",
                    en = "Demo Welcome Talk $dayIndex",
                ),
                description = null,
                startsAt = (DroidKaigi2024Day.Workday.start + 10.hours + dayIndex.days)
                    .toCustomIsoString(),
                endsAt = (DroidKaigi2024Day.Workday.start + 10.hours + 30.minutes + dayIndex.days).toCustomIsoString(),
                isServiceSession = true,
                isPlenumSession = false,
                speakers = emptyList(),
                roomId = 2,
                targetAudience = "TBW",
                language = "JAPANESE",
                sessionCategoryItemId = 1,
                interpretationTarget = false,
                asset = SessionAssetResponse(videoUrl = null, slideUrl = null),
                message = null,
                sessionType = "WELCOME_TALK",
                levels = listOf("UNSPECIFIED"),
            ),
        )
    }

    for (day in 0 until 3) {
        val dayOffsetSeconds = day * 24 * 60 * 60 + 10 * 60 * 60 + (0.5 * 60 * 60).toInt()
        for (room in rooms) {
            for (index in 0 until 4) {
                val start =
                    (DroidKaigi2024Day.Workday.start + (index * 30 * 60 * 60 + dayOffsetSeconds).seconds)
                val end =
                    (DroidKaigi2024Day.Workday.start + (index * 30 * 60 * 60 + dayOffsetSeconds + 30 * 60).seconds)

                val session = SessionResponse(
                    id = "$day$room$index",
                    isServiceSession = false,
                    title = LocaledResponse(
                        ja = "DroidKaigiのアプリのアーキテクチャ day$day room${room.name.ja} index$index",
                        en = "DroidKaigi App Architecture day$day room${room.name.en} index$index",
                    ),
                    speakers = listOf("1", "2"),
                    description = "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\n" +
                        "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\n",
                    i18nDesc = LocaledResponse(
                        ja = "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\n" +
                            "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\n",
                        en = "This is a description\nThis is a description\nThis is a description\n" +
                            "This is a description\nThis is a description\nThis is a description\n",
                    ),
                    startsAt = start.toCustomIsoString(),
                    endsAt = end.toCustomIsoString(),
                    language = "JAPANESE",
                    roomId = room.id,
                    sessionCategoryItemId = 1,
                    sessionType = "NORMAL",
                    message = null,
                    isPlenumSession = false,
                    targetAudience = "For App developer アプリ開発者向け",
                    interpretationTarget = false,
                    asset = SessionAssetResponse(
                        videoUrl = "https://www.youtube.com/watch?v=hFdKCyJ-Z9A",
                        slideUrl = "https://droidkaigi.jp/2021/",
                    ),
                    levels = listOf("INTERMEDIATE"),
                )
                sessions.add(session)
            }
        }
    }

    return SessionsAllResponse(
        sessions = sessions,
        rooms = rooms,
        speakers = speakers,
        categories = categories,
    )
}

private fun Instant.toCustomIsoString(): String {
    val timezone = TimeZone.of("Asia/Tokyo")
    val zonedDateTime = this.toLocalDateTime(timezone)
    val offset = timezone.offsetAt(this)

    return buildString {
        append(zonedDateTime.date)
        append('T')
        append(zonedDateTime.hour.toString().padStart(2, '0'))
        append(':')
        append(zonedDateTime.minute.toString().padStart(2, '0'))
        append(':')
        append(zonedDateTime.second.toString().padStart(2, '0'))
        append(offset.toString())
    }
}
