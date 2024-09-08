package io.github.droidkaigi.confsched.droidkaigiui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun TimetableItemTag(
    tagText: String,
    modifier: Modifier = Modifier,
) {
    TimetableItemTag(
        tagText = tagText,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        borderColor = MaterialTheme.colorScheme.outline,
        icon = null,
        modifier = modifier,
    )
}

@Composable
fun TimetableItemTag(
    tagText: String,
    tagColor: Color,
    icon: DrawableResource?,
    modifier: Modifier = Modifier,
) {
    TimetableItemTag(
        tagText = tagText,
        contentColor = tagColor,
        borderColor = tagColor,
        icon = icon,
        modifier = modifier,
    )
}

@Composable
private fun TimetableItemTag(
    tagText: String,
    contentColor: Color,
    borderColor: Color,
    icon: DrawableResource?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .border(
                border = BorderStroke(width = 1.dp, color = borderColor),
                shape = RoundedCornerShape(2.dp),
            )
            .padding(
                horizontal = 6.dp,
                vertical = 4.dp,
            ),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon?.let { ico ->
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = vectorResource(ico),
                contentDescription = "",
                tint = contentColor,
            )
        }
        Text(
            color = contentColor,
            text = tagText,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
