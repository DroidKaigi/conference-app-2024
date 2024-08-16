package io.github.droidkaigi.confsched.about.section

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.about.generated.resources.Res
import conference_app_2024.feature.about.generated.resources.about_header
import conference_app_2024.feature.about.generated.resources.description
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.component.AboutDroidKaigiDetailSummaryCard
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

private const val maxAboutHeaderOffsetDp = 40

@Suppress("ConstPropertyName")
object AboutDetailSectionTestTag {
    const val Section = "DetailSection"
}

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
                (maxAboutHeaderOffsetDp * (scrollOffset / height)).roundToInt()
            } else {
                0
            }
        }
    }

    Column(
        modifier = modifier.testTag(AboutDetailSectionTestTag.Section),
    ) {
        Image(
            painter = painterResource(Res.drawable.about_header),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = aboutHeaderOffset.dp),
        )
        Text(
            text = stringResource(AboutRes.string.description),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
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
