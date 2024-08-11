package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.sessions.generated.resources.category_title
import conference_app_2024.feature.sessions.generated.resources.content_description_category
import conference_app_2024.feature.sessions.generated.resources.content_description_language
import conference_app_2024.feature.sessions.generated.resources.content_description_location
import conference_app_2024.feature.sessions.generated.resources.content_description_schedule
import conference_app_2024.feature.sessions.generated.resources.language_title
import conference_app_2024.feature.sessions.generated.resources.location_title
import conference_app_2024.feature.sessions.generated.resources.schedule_title
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.model.Locale
import io.github.droidkaigi.confsched.model.TimetableItem
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.model.getDefaultLocale
import io.github.droidkaigi.confsched.model.nameAndFloor
import io.github.droidkaigi.confsched.sessions.SessionsRes
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimetableItemDetailSummaryCard(
    timetableItem: TimetableItem,
    modifier: Modifier = Modifier,
) {
    val primaryColor = LocalRoomTheme.current.primaryColor
    Column(
        modifier = modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 16.dp,
                bottom = 8.dp,
            )
            .drawBehind {
                drawRoundRect(
                    color = primaryColor,
                    style = Stroke(
                        width = 2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f),
                    ),
                    cornerRadius = CornerRadius(4.dp.toPx()),
                )
            }
            .padding(12.dp),
    ) {
        SummaryCardRow(
            modifier = Modifier.fillMaxWidth(),
            imageVector = Icons.Outlined.Schedule,
            contentDescription = stringResource(SessionsRes.string.content_description_schedule),
            title = stringResource(SessionsRes.string.schedule_title),
            description = timetableItem.formattedDateTimeString,
        )
        Spacer(Modifier.height(8.dp))
        SummaryCardRow(
            modifier = Modifier.fillMaxWidth(),
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = stringResource(SessionsRes.string.content_description_location),
            title = stringResource(SessionsRes.string.location_title),
            description = timetableItem.room.nameAndFloor,
        )
        Spacer(Modifier.height(8.dp))
        SummaryCardRow(
            modifier = Modifier.fillMaxWidth(),
            imageVector = Icons.Outlined.Language,
            contentDescription = stringResource(SessionsRes.string.content_description_language),
            title = stringResource(SessionsRes.string.language_title),
            description = timetableItem.getSupportedLangString(
                getDefaultLocale() == Locale.JAPAN,
            ),
        )
        Spacer(Modifier.height(8.dp))
        SummaryCardRow(
            modifier = Modifier.fillMaxWidth(),
            imageVector = Icons.Outlined.Category,
            contentDescription = stringResource(SessionsRes.string.content_description_category),
            title = stringResource(SessionsRes.string.category_title),
            description = timetableItem.category.title.currentLangTitle,
        )
    }
}

@Composable
private fun SummaryCardRow(
    imageVector: ImageVector,
    contentDescription: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = imageVector, contentDescription = contentDescription, tint = LocalRoomTheme.current.primaryColor)
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = LocalRoomTheme.current.primaryColor,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
@Preview
fun TimetableItemDetailSummaryCardPreview() {
    KaigiTheme {
        ProvideFakeRoomTheme {
            Surface {
                TimetableItemDetailSummaryCard(
                    timetableItem = TimetableItem.Session.fake(),
                )
            }
        }
    }
}
