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
    tagColor: Color,
    modifier: Modifier = Modifier,
    icon: DrawableResource? = null,
) {
    Row(
        modifier = modifier
            .border(
                border = BorderStroke(width = 1.dp, color = tagColor),
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
                tint = tagColor,
            )
        }
        Text(
            color = tagColor,
            text = tagText,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
