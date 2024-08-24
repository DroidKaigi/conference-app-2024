package io.github.droidkaigi.confsched.about.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import conference_app_2024.core.droidkaigiui.generated.resources.about_header_year
import conference_app_2024.feature.about.generated.resources.description
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.component.AboutDroidKaigiDetailSummaryCard
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.droidkaigiui.DroidKaigiUiRes
import io.github.droidkaigi.confsched.droidkaigiui.canShowLargeVector
import io.github.droidkaigi.confsched.droidkaigiui.provideAboutHeaderTitlePainter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

private const val MaxAboutHeaderOffsetDp = 40

const val AboutDetailTestTag = "AboutDetailTestTag"

@Composable
fun AboutDroidKaigiDetail(
    screenScrollState: LazyListState,
    modifier: Modifier = Modifier,
    onViewMapClick: () -> Unit,
) {
    // Parallax effect for the header image
    val aboutHeaderOffset by remember(screenScrollState) {
        derivedStateOf {
            if (screenScrollState.layoutInfo.visibleItemsInfo.isNotEmpty() && screenScrollState.firstVisibleItemIndex == 0) {
                val scrollOffset = screenScrollState.firstVisibleItemScrollOffset.toFloat()
                val height = screenScrollState.layoutInfo.visibleItemsInfo.first().size
                (MaxAboutHeaderOffsetDp * (scrollOffset / height)).roundToInt()
            } else {
                0
            }
        }
    }

    Column(
        modifier = modifier.testTag(AboutDetailTestTag),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            val imageModifier = if (canShowLargeVector()) {
                Modifier
                    .fillMaxWidth()
                    .offset(y = aboutHeaderOffset.dp)
            } else {
                // Some API Levels are not optimized to handle VectorDrawable, so OOM occurs when large VectorDrawable is displayed.
                // Therefore, depending on the API Level, whether or not to display an Image in its full width should be separated.
                Modifier
            }
            Image(
                painter = painterResource(DroidKaigiUiRes.drawable.about_header_year),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = imageModifier,
            )
            Image(
                painter = provideAboutHeaderTitlePainter(canShowLargeVector()),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = imageModifier,
            )
        }
        Text(
            text = stringResource(AboutRes.string.description),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp,
                ),
        )
        AboutDroidKaigiDetailSummaryCard(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 12.dp,
                    end = 16.dp,
                ),
            onViewMapClick = onViewMapClick,
        )
    }
}

@Preview
@Composable
fun AboutDroidKaigiDetailPreview() {
    KaigiTheme {
        Surface {
            AboutDroidKaigiDetail(
                screenScrollState = rememberLazyListState(),
                onViewMapClick = {},
            )
        }
    }
}
