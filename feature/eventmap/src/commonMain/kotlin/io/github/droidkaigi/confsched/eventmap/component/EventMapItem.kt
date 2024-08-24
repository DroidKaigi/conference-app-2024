package io.github.droidkaigi.confsched.eventmap.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.eventmap.generated.resources.read_more
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.droidkaigiui.toResDrawable
import io.github.droidkaigi.confsched.eventmap.EventMapRes
import io.github.droidkaigi.confsched.model.EventMapEvent
import io.github.droidkaigi.confsched.model.RoomIcon
import io.github.droidkaigi.confsched.model.fakes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EventMapItem(
    eventMapEvent: EventMapEvent,
    onClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ProvideRoomTheme(eventMapEvent.roomName.currentLangTitle) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                ToolTip(
                    text = eventMapEvent.roomName.currentLangTitle,
                    roomIcon = eventMapEvent.roomIcon,
                    color = LocalRoomTheme.current.primaryColor,
                    backgroundColor = LocalRoomTheme.current.containerColor,
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = eventMapEvent.name.currentLangTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = LocalRoomTheme.current.primaryColor,
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = eventMapEvent.description.currentLangTitle,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7F),
            )
            eventMapEvent.message?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = it.currentLangTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
            eventMapEvent.moreDetailsUrl?.let {
                Spacer(Modifier.height(height = 8.dp))
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onClick(it) },
                ) {
                    Text(
                        text = stringResource(EventMapRes.string.read_more),
                        style = MaterialTheme.typography.labelLarge,
                        color = LocalRoomTheme.current.primaryColor,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun EventMapItemPreview() {
    KaigiTheme {
        Surface {
            EventMapItem(
                eventMapEvent = EventMapEvent.fakes().first(),
                onClick = {},
            )
        }
    }
}

@Composable
private fun ToolTip(
    text: String,
    roomIcon: RoomIcon,
    color: Color = Color(0xFFC5C7C4),
    backgroundColor: Color = Color.Transparent,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .border(1.dp, color, RoundedCornerShape(4.dp))
            .padding(vertical = 4.5.dp, horizontal = 8.dp),
    ) {
        roomIcon.toResDrawable()?.let {
            Icon(
                painter = painterResource(it),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(12.dp),
            )
            Spacer(Modifier.width(3.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = color,
        )
    }
}
