package io.github.droidkaigi.confsched.data.sessions

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.GET
import io.github.droidkaigi.confsched.data.NetworkService
import io.github.droidkaigi.confsched.data.sessions.response.LocaledResponse
import io.github.droidkaigi.confsched.data.sessions.response.SessionAssetResponse
import io.github.droidkaigi.confsched.data.sessions.response.SessionMessageResponse
import io.github.droidkaigi.confsched.data.sessions.response.SessionsAllResponse
import io.github.droidkaigi.confsched.model.MultiLangText
import io.github.droidkaigi.confsched.model.RoomType.RoomF
import io.github.droidkaigi.confsched.model.RoomType.RoomG
import io.github.droidkaigi.confsched.model.RoomType.RoomH
import io.github.droidkaigi.confsched.model.RoomType.RoomI
import io.github.droidkaigi.confsched.model.RoomType.RoomIJ
import io.github.droidkaigi.confsched.model.RoomType.RoomJ
import io.github.droidkaigi.confsched.model.Timetable
import io.github.droidkaigi.confsched.model.TimetableAsset
import io.github.droidkaigi.confsched.model.TimetableCategory
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import io.github.droidkaigi.confsched.model.TimetableItem.Special
import io.github.droidkaigi.confsched.model.TimetableItemId
import io.github.droidkaigi.confsched.model.TimetableItemList
import io.github.droidkaigi.confsched.model.TimetableLanguage
import io.github.droidkaigi.confsched.model.TimetableRoom
import io.github.droidkaigi.confsched.model.TimetableSessionType
import io.github.droidkaigi.confsched.model.TimetableSessionType.Companion
import io.github.droidkaigi.confsched.model.TimetableSpeaker
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

internal interface SessionApi {
    @GET("events/droidkaigi2024/timetable")
    suspend fun getTimetable(): SessionsAllResponse
}

public class DefaultSessionsApiClient internal constructor(
    private val networkService: NetworkService,
    ktorfit: Ktorfit,
) : SessionsApiClient {

    private val sessionApi = ktorfit.create<SessionApi>()

    override suspend fun sessionsAllResponse(): SessionsAllResponse {
        return networkService {
            sessionApi.getTimetable()
        }
    }
}

public fun SessionsAllResponse.toTimetable(): Timetable {
    val timetableContents = this
    val speakerIdToSpeaker: Map<String, TimetableSpeaker> = timetableContents.speakers
        .groupBy { it.id }
        .mapValues { (_, apiSpeakers) ->
            apiSpeakers.map { apiSpeaker ->
                TimetableSpeaker(
                    id = apiSpeaker.id,
                    name = apiSpeaker.fullName,
                    bio = apiSpeaker.bio ?: "",
                    iconUrl = apiSpeaker.profilePicture.orEmpty(),
                    tagLine = apiSpeaker.tagLine ?: "",
                )
            }.first()
        }
    val categoryIdToCategory: Map<Int, TimetableCategory> = timetableContents.categories
        .flatMap { it.items }
        .groupBy { it.id }
        .mapValues { (_, apiCategories) ->
            apiCategories.map { apiCategory ->
                TimetableCategory(
                    id = apiCategory.id,
                    title = apiCategory.name.toMultiLangText(),
                )
            }.first()
        }
    val roomIdToRoom: Map<Int, TimetableRoom> = timetableContents.rooms
        .associateBy(
            keySelector = { room -> room.id },
            valueTransform = { room ->
                TimetableRoom(
                    id = room.id,
                    name = room.name.toMultiLangText(),
                    type = room.name.toRoomType(),
                    sort = room.sort,
                )
            },
        )

    return Timetable(
        TimetableItemList(
            timetableContents.sessions.map { apiSession ->
                if (!apiSession.isServiceSession) {
                    Session(
                        id = TimetableItemId(apiSession.id),
                        title = apiSession.title.toMultiLangText(),
                        startsAt = apiSession.startsAt.toInstantAsJST(),
                        endsAt = apiSession.endsAt.toInstantAsJST(),
                        category = categoryIdToCategory[apiSession.sessionCategoryItemId]!!,
                        sessionType = TimetableSessionType.ofOrNull(apiSession.sessionType)!!,
                        room = roomIdToRoom[apiSession.roomId]!!,
                        targetAudience = apiSession.targetAudience,
                        language = TimetableLanguage(
                            langOfSpeaker = apiSession.language,
                            isInterpretationTarget = apiSession.interpretationTarget,
                        ),
                        asset = apiSession.asset.toTimetableAsset(),
                        description = if (
                            apiSession.i18nDesc?.ja == null &&
                            apiSession.i18nDesc?.en == null
                        ) {
                            MultiLangText(
                                jaTitle = apiSession.description ?: "",
                                enTitle = apiSession.description ?: "",
                            )
                        } else {
                            apiSession.i18nDesc.toMultiLangText()
                        },
                        speakers = apiSession.speakers
                            .map { speakerIdToSpeaker[it]!! }
                            .toPersistentList(),
                        message = apiSession.message?.toMultiLangText(),
                        levels = apiSession.levels.toPersistentList(),
                    )
                } else {
                    Special(
                        id = TimetableItemId(apiSession.id),
                        title = apiSession.title.toMultiLangText(),
                        startsAt = apiSession.startsAt.toInstantAsJST(),
                        endsAt = apiSession.endsAt.toInstantAsJST(),
                        category = categoryIdToCategory[apiSession.sessionCategoryItemId]!!,
                        sessionType = Companion.ofOrNull(apiSession.sessionType)!!,
                        room = roomIdToRoom[apiSession.roomId]!!,
                        targetAudience = apiSession.targetAudience,
                        language = TimetableLanguage(
                            langOfSpeaker = apiSession.language,
                            isInterpretationTarget = apiSession.interpretationTarget,
                        ),
                        asset = apiSession.asset.toTimetableAsset(),
                        speakers = apiSession.speakers
                            .map { speakerIdToSpeaker[it]!! }
                            .toPersistentList(),
                        levels = apiSession.levels.toPersistentList(),
                        description = if (
                            apiSession.i18nDesc?.ja == null &&
                            apiSession.i18nDesc?.en == null
                        ) {
                            MultiLangText(
                                jaTitle = apiSession.description ?: "",
                                enTitle = apiSession.description ?: "",
                            )
                        } else {
                            apiSession.i18nDesc.toMultiLangText()
                        },
                        message = apiSession.message?.toMultiLangText(),
                    )
                }
            }
                .sortedWith(
                    compareBy<TimetableItem> { it.startsAt }
                        .thenBy { it.room },
                )
                .toPersistentList(),
        ),
    )
}

private fun LocaledResponse.toMultiLangText() =
    MultiLangText(jaTitle = ja ?: "", enTitle = en ?: "")

private fun SessionMessageResponse.toMultiLangText() = MultiLangText(jaTitle = ja, enTitle = en)
private fun SessionAssetResponse.toTimetableAsset() = TimetableAsset(videoUrl, slideUrl)
private fun LocaledResponse.toRoomType() = when (en?.lowercase()) {
    "flamingo" -> RoomF
    "giraffe" -> RoomG
    "hedgehog" -> RoomH
    "iguana" -> RoomI
    "jellyfish" -> RoomJ
    // Assume the room on the first day.
    else -> RoomIJ
}

internal fun String.toInstantAsJST(): Instant {
    val (date, _) = split("+")
    return LocalDateTime.parse(date).toInstant(TimeZone.of("UTC+9"))
}
