package io.github.droidkaigi.confsched.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun TimeTableItemTag(
    tagText: String,
    tagColor: Color,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    Row(
        modifier = modifier
            .border(border = BorderStroke(width = 1.dp, color = tagColor))
            .clip(RoundedCornerShape(15.dp))
            .padding(5.dp),
    ) {
        icon?.let { ico ->
            Icon(ico, "", tint = tagColor)
        }
        Spacer(modifier = Modifier.padding(3.dp))
        Text(
            color = tagColor,
            text = tagText,
            modifier = Modifier.padding(end = 5.dp),
        )
    }
}
