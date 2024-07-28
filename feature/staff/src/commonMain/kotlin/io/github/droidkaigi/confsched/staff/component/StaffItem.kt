package io.github.droidkaigi.confsched.staff.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.model.Staff
import io.github.droidkaigi.confsched.model.fakes
import io.github.droidkaigi.confsched.ui.previewOverride
import io.github.droidkaigi.confsched.ui.rememberAsyncImagePainter
import org.jetbrains.compose.ui.tooling.preview.Preview

private val staffIconShape = CircleShape

@Composable
fun StaffItem(
    staff: Staff,
    onStaffItemClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(enabled = staff.profileUrl.isNotEmpty()) {
                staff.profileUrl.let(onStaffItemClick)
            }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(23.dp),
    ) {
        Image(
            painter = previewOverride(previewPainter = { rememberVectorPainter(image = Icons.Default.Person) }) {
                rememberAsyncImagePainter(staff.iconUrl)
            },
            contentDescription = null,
            modifier = Modifier
                .size(52.dp)
                .clip(staffIconShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = staffIconShape,
                ),
        )
        Text(
            text = staff.username,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
@Preview
fun StaffItemPreview() {
    KaigiTheme {
        Surface {
            StaffItem(
                staff = Staff.fakes().first(),
                onStaffItemClick = {},
            )
        }
    }
}
