package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.ViewTimeline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import io.github.droidkaigi.confsched.designsystem.preview.MultiLanguagePreviews
import io.github.droidkaigi.confsched.designsystem.preview.MultiThemePreviews
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.feature.sessions.R
import io.github.droidkaigi.confsched.model.TimetableUiType
import io.github.droidkaigi.confsched.sessions.SessionsStrings.Timetable

const val TimetableUiTypeChangeButtonTestTag = "TimetableUiTypeChangeButton"

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TimetableTopArea(
    timetableUiType: TimetableUiType,
    onTimetableUiChangeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Icon(
                painter = painterResource(id = R.drawable.icon_droidkaigi_logo),
                contentDescription = null,
            )
        },
        modifier = modifier,
        actions = {
            IconButton(
                modifier = Modifier.testTag(TimetableUiTypeChangeButtonTestTag),
                onClick = { onTimetableUiChangeClick() },
            ) {
                Icon(
                    imageVector = if (timetableUiType != TimetableUiType.Grid) {
                        Icons.Outlined.GridView
                    } else {
                        Icons.Outlined.ViewTimeline
                    },
                    contentDescription = Timetable.asString(),
                )
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color.Transparent,
        ),
    )
}

@MultiThemePreviews
@MultiLanguagePreviews
@Composable
fun TimetableTopAreaPreview() {
    KaigiTheme {
        Surface {
            TimetableTopArea(
                timetableUiType = TimetableUiType.Grid,
                onTimetableUiChangeClick = {},
            )
        }
    }
}
