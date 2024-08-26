package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import conference_app_2024.feature.sessions.generated.resources.calendar_add_on
import conference_app_2024.feature.sessions.generated.resources.content_description_calendar
import conference_app_2024.feature.sessions.generated.resources.content_description_share
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.sessions.SessionsRes
import io.github.droidkaigi.confsched.sessions.TimetableItemDetailBookmarkIconTestTag
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimetableItemDetailBottomAppBar(
    timetableItem: TimetableItem,
    isBookmarked: Boolean,
    onBookmarkClick: (TimetableItem) -> Unit,
    onCalendarRegistrationClick: (TimetableItem) -> Unit,
    modifier: Modifier = Modifier,
    onShareClick: (TimetableItem) -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    BottomAppBar(
        modifier = modifier,
        actions = {
            IconButton(onClick = { onShareClick(timetableItem) }) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = stringResource(SessionsRes.string.content_description_share),
                )
            }
            IconButton(onClick = { onCalendarRegistrationClick(timetableItem) }) {
                Icon(
                    painter = painterResource(SessionsRes.drawable.calendar_add_on),
                    contentDescription = stringResource(SessionsRes.string.content_description_calendar),
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.testTag(TimetableItemDetailBookmarkIconTestTag),
                onClick = {
                    if (!isBookmarked) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    }

                    onBookmarkClick(timetableItem)
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ) {
                val contentDescription = if (isBookmarked) {
                    "Bookmarked"
                } else {
                    "Not Bookmarked"
                }
                Icon(
                    imageVector = if (isBookmarked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = contentDescription,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    )
}

@Composable
@Preview
fun TimetableItemDetailBottomAppBarPreview() {
    KaigiTheme {
        Surface {
            TimetableItemDetailBottomAppBar(
                timetableItem = TimetableItem.Session.fake(),
                isBookmarked = false,
                onBookmarkClick = {},
                onCalendarRegistrationClick = {},
                onShareClick = {},
            )
        }
    }
}

@Composable
@Preview
fun TimetableItemDetailBottomAppBarBookmarkedPreview() {
    KaigiTheme {
        Surface {
            TimetableItemDetailBottomAppBar(
                timetableItem = TimetableItem.Session.fake(),
                isBookmarked = true,
                onBookmarkClick = {},
                onCalendarRegistrationClick = {},
                onShareClick = {},
            )
        }
    }
}
