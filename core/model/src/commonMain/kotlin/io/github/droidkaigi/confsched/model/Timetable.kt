package io.github.droidkaigi.confsched.model

import io.github.droidkaigi.confsched.model.RoomType.RoomA
import io.github.droidkaigi.confsched.model.RoomType.RoomB
import io.github.droidkaigi.confsched.model.RoomType.RoomC
import io.github.droidkaigi.confsched.model.RoomType.RoomD
import io.github.droidkaigi.confsched.model.RoomType.RoomE
import io.github.droidkaigi.confsched.model.TimetableItem.Session
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentList
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Immutable
public data class Timetable(
    val timetableItems: TimetableItemList = TimetableItemList(),
    val bookmarks: PersistentSet<TimetableItemId> = persistentSetOf(),
) {
    val contents: List<TimetableItemWithFavorite> by lazy {
        timetableItems.map {
            TimetableItemWithFavorite(it, bookmarks.contains(it.id))
        }
    }

    val rooms: List<TimetableRoom> by lazy {
        timetableItems.map { it.room }.toSet().sorted()
    }

    val categories: List<TimetableCategory> by lazy {
        timetableItems.map { it.category }.toSet().sortedBy { it.id }
    }

    val sessionTypes: List<TimetableSessionType> by lazy {
        timetableItems.map { it.sessionType }.toSet().sorted()
    }

    val languages: List<TimetableLanguage> by lazy {
        timetableItems.map { it.language }.toSet()
            .sortedBy { it.langOfSpeaker }
            .sortedBy { it.isInterpretationTarget }
    }

    public fun dayTimetable(droidKaigi2024Day: DroidKaigi2024Day): Timetable {
        var timetableItems = timetableItems.toList()
        timetableItems = timetableItems.filter { timetableItem ->
            timetableItem.day == droidKaigi2024Day
        }
        return copy(timetableItems = TimetableItemList(timetableItems.toPersistentList()))
    }

    public fun filtered(filters: Filters): Timetable {
        var timetableItems = timetableItems.toList()
        if (filters.days.isNotEmpty()) {
            timetableItems = timetableItems.filter { timetableItem ->
                filters.days.contains(timetableItem.day)
            }
        }
        if (filters.categories.isNotEmpty()) {
            timetableItems = timetableItems.filter { timetableItem ->
                filters.categories.contains(timetableItem.category)
            }
        }
        if (filters.sessionTypes.isNotEmpty()) {
            timetableItems = timetableItems.filter { timetableItem ->
                filters.sessionTypes.contains(timetableItem.sessionType)
            }
        }
        if (filters.languages.isNotEmpty()) {
            timetableItems = timetableItems.filter { timetableItem ->
                filters.languages.contains(timetableItem.language.toLang()) ||
                    timetableItem.language.isInterpretationTarget
            }
        }
        if (filters.filterFavorite) {
            timetableItems = timetableItems.filter { timetableItem ->
                bookmarks.contains(timetableItem.id)
            }
        }
        if (filters.searchWord.isNotBlank()) {
            timetableItems = timetableItems.filter { timetableItem ->
                timetableItem.title.currentLangTitle.contains(
                    filters.searchWord,
                    ignoreCase = true,
                )
            }
        }
        return copy(timetableItems = TimetableItemList(timetableItems.toPersistentList()))
    }

    public fun isEmpty(): Boolean {
        return timetableItems.isEmpty()
    }

    public companion object
}

public fun Timetable?.orEmptyContents(): Timetable = this ?: Timetable()

public fun Timetable.Companion.fake(): Timetable {
    val rooms = mutableListOf(
        TimetableRoom(1, MultiLangText("Arctic Fox", "Arctic Fox"), RoomA, 4),
        TimetableRoom(2, MultiLangText("Bumblebee", "Bumblebee"), RoomB, 5),
        TimetableRoom(3, MultiLangText("Chipmunk", "Chipmunk"), RoomC, 1),
        TimetableRoom(4, MultiLangText("Dolphin", "Dolphin"), RoomD, 2),
        TimetableRoom(5, MultiLangText("Electric Eel", "Electric Eel"), RoomE, 3),
    )
    repeat(10) {
        rooms += rooms
    }
    val roomsIterator = rooms.iterator()
    val timetableItems = buildList {
        add(
            TimetableItem.Special(
                id = TimetableItemId("1"),
                title = MultiLangText("ウェルカムトーク", "Welcome Talk"),
                startsAt = DroidKaigi2024Day.Workday.start + 10.hours,
                endsAt = DroidKaigi2024Day.Workday.start + 10.hours + 20.minutes,
                category = TimetableCategory(
                    id = 28657,
                    title = MultiLangText("その他", "Other"),
                ),
                sessionType = TimetableSessionType.NORMAL,
                room = roomsIterator.next(),
                targetAudience = "TBW",
                language = TimetableLanguage(
                    langOfSpeaker = "JAPANESE",
                    isInterpretationTarget = true,
                ),
                asset = TimetableAsset(null, null),
                levels = persistentListOf(
                    "BEGINNER",
                    "INTERMEDIATE",
                    "ADVANCED",
                ),
                speakers = persistentListOf(
                    TimetableSpeaker(
                        id = "1",
                        name = "taka",
                        iconUrl = "https://github.com/takahirom.png",
                        bio = "Likes Android",
                        tagLine = "Android Engineer",
                    ),
                    TimetableSpeaker(
                        id = "2",
                        name = "ry",
                        iconUrl = "https://github.com/ry-itto.png",
                        bio = "Likes iOS",
                        tagLine = "iOS Engineer",
                    ),
                ),
                description = MultiLangText(
                    jaTitle = "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\n" +
                        "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\n",
                    enTitle = "This is a description\nThis is a description\nThis is a description\n" +
                        "This is a description\nThis is a description\nThis is a description\n",
                ),
            ),
        )
        for (day in -1..1) {
            for (index in 0..20) {
                val dayOffset = day * 24 * 60 * 60
                val start = DroidKaigi2024Day.Workday.start + (index * 25 * 60 + dayOffset).seconds
                val end = DroidKaigi2024Day.Workday.start + (index * 25 * 60 + dayOffset + 40 * 60).seconds
                val fake = Session.fake()
                add(
                    fake
                        .copy(
                            id = TimetableItemId("$day$index"),
                            title = MultiLangText(
                                jaTitle = "${fake.title.jaTitle} $day $index",
                                enTitle = "${fake.title.enTitle} $day $index",
                            ),
                            room = roomsIterator.next(),
                            startsAt = start,
                            endsAt = end
                        ),
                )
            }
        }
        add(
            TimetableItem.Special(
                id = TimetableItemId("3"),
                title = MultiLangText("Closing", "Closing"),
                startsAt = DroidKaigi2024Day.Workday.start + 10.hours,
                endsAt = DroidKaigi2024Day.Workday.start + 10.hours + 20.minutes,
                category = TimetableCategory(
                    id = 28657,
                    title = MultiLangText("その他", "Other"),
                ),
                sessionType = TimetableSessionType.NORMAL,
                room = roomsIterator.next(),
                targetAudience = "TBW",
                language = TimetableLanguage(
                    langOfSpeaker = "ENGLISH",
                    isInterpretationTarget = true,
                ),
                asset = TimetableAsset(null, null),
                levels = persistentListOf(
                    "BEGINNER",
                    "INTERMEDIATE",
                    "ADVANCED",
                ),
                speakers = persistentListOf(
                    TimetableSpeaker(
                        id = "1",
                        name = "taka",
                        iconUrl = "https://github.com/takahirom.png",
                        bio = "Likes Android",
                        tagLine = "Android Engineer",
                    ),
                    TimetableSpeaker(
                        id = "2",
                        name = "ry",
                        iconUrl = "https://github.com/ry-itto.png",
                        bio = "Likes iOS",
                        tagLine = "iOS Engineer",
                    ),
                ),
                description = MultiLangText(
                    jaTitle = "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\n" +
                        "これはディスクリプションです。\nこれはディスクリプションです。\nこれはディスクリプションです。\n",
                    enTitle = "This is a description\nThis is a description\nThis is a description\n" +
                        "This is a description\nThis is a description\nThis is a description\n",
                ),
            ),
        )
    }
    return Timetable(
        timetableItems = TimetableItemList(
            timetableItems.toPersistentList(),
        ),
        bookmarks = persistentSetOf(),
    )
}
