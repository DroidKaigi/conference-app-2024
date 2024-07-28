package io.github.droidkaigi.confsched.about.section

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Diversity1
import androidx.compose.material.icons.outlined.SentimentVerySatisfied
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.about.generated.resources.contributor
import conference_app_2024.feature.about.generated.resources.credits_title
import conference_app_2024.feature.about.generated.resources.staff
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.component.AboutContentColumn
import org.jetbrains.compose.resources.stringResource

const val AboutCreditsStaffItemTestTag = "AboutCreditsStaffItem"
const val AboutCreditsContributorsItemTestTag = "AboutCreditsContributorsItem"
const val AboutCreditsSponsorsItemTestTag = "AboutCreditsSponsorsItem"

fun LazyListScope.aboutCredits(
    modifier: Modifier = Modifier,
    onStaffItemClick: () -> Unit,
    onContributorsItemClick: () -> Unit,
    onSponsorsItemClick: () -> Unit,
) {
    item {
        Text(
            text = stringResource(AboutRes.string.credits_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier
                .padding(
                    start = 16.dp,
                    top = 32.dp,
                    end = 16.dp,
                ),
        )
    }
    item {
        AboutContentColumn(
            leadingIcon = Outlined.Diversity1,
            label = stringResource(AboutRes.string.contributor),
            testTag = AboutCreditsContributorsItemTestTag,
            onClickAction = onContributorsItemClick,
            modifier = modifier
                .padding(
                    horizontal = 16.dp,
                ),
        )
    }
    item {
        AboutContentColumn(
            leadingIcon = Outlined.SentimentVerySatisfied,
            label = stringResource(AboutRes.string.staff),
            testTag = AboutCreditsStaffItemTestTag,
            onClickAction = onStaffItemClick,
            modifier = modifier
                .padding(
                    horizontal = 16.dp,
                ),
        )
    }
    item {
        AboutContentColumn(
            leadingIcon = Outlined.Apartment,
            label = stringResource(AboutRes.string.staff),
            testTag = AboutCreditsSponsorsItemTestTag,
            onClickAction = onSponsorsItemClick,
            modifier = modifier
                .padding(
                    horizontal = 16.dp,
                ),
        )
    }
}
