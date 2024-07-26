package io.github.droidkaigi.confsched.eventmap.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.designsystem.theme.ProvideRoomTheme
import io.github.droidkaigi.confsched.model.EventMapEvent

@Composable
fun EventMapItem(
    eventMapEvent: EventMapEvent,
    @Suppress("UnusedParameter")
    onClick: (url: String) -> Unit,
    onClickFavorite: (eventMapEvent: EventMapEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    ProvideRoomTheme(eventMapEvent.roomName) {
        val green = LocalRoomTheme.current.primaryColor
        val gray = Color(0xFFC5C7C4)
        Column(
            modifier = modifier
                .border(1.dp, gray, RoundedCornerShape(5.dp))
                .background(Color.Transparent, RoundedCornerShape(5.dp))
                .clickable {
//                eventMapEvent.profileUrl?.let(onClick)
                }
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                ToolTip(
                    text = eventMapEvent.roomName,
                    icon = Icons.Filled.Star,
                    color = green,
                )
                Spacer(Modifier.width(4.dp))
                ToolTip(
                    text = eventMapEvent.dateLabel,
                    color = Color(0xFFBFC9C2),
                )
                Spacer(Modifier.weight(1F))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp).clickable { onClickFavorite(eventMapEvent) },
                    tint = if (eventMapEvent.isFavorite) green else gray,
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = eventMapEvent.name,
                fontSize = 17.sp,
                lineHeight = 23.8.sp,
                fontWeight = FontWeight.W600,
                letterSpacing = 0.1.sp,
                color = gray,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = eventMapEvent.description,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.W400,
                letterSpacing = 0.25.sp,
                color = Color.White.copy(alpha = 0.7F),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = eventMapEvent.timeDuration,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.W600,
                letterSpacing = 0.1.sp,
                color = green,
            )
        }
    }
}

@Composable
private fun ToolTip(
    text: String,
    icon: ImageVector? = null,
    color: Color = Color(0xFFC5C7C4),
    backgroundColor: Color = Color.Transparent,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .border(1.dp, color)
            .background(backgroundColor, RoundedCornerShape(0.dp))
            .padding(vertical = 4.5.dp, horizontal = 8.dp),
    ) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(12.dp),
            )
            Spacer(Modifier.width(3.dp))
        }
        Text(
            text = text,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight.W500,
            color = color,
        )
    }
}
