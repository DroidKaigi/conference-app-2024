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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.about.generated.resources.contributor
import conference_app_2024.feature.about.generated.resources.credits_title
import conference_app_2024.feature.about.generated.resources.sponsor
import conference_app_2024.feature.about.generated.resources.staff
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.component.AboutContentColumn
import io.github.droidkaigi.confsched.about.section.AboutCreditsSectionTestTag.ContributorsItem
import io.github.droidkaigi.confsched.about.section.AboutCreditsSectionTestTag.SponsorsItem
import io.github.droidkaigi.confsched.about.section.AboutCreditsSectionTestTag.StaffItem
import org.jetbrains.compose.resources.stringResource

@Suppress("ConstPropertyName")
object AboutCreditsSectionTestTag {
    const val Title = "AboutCreditsTitle"
    const val StaffItem = "AboutCreditsStaffItem"
    const val ContributorsItem = "AboutCreditsContributorsItem"
    const val SponsorsItem = "AboutCreditsSponsorsItem"
}

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
                .testTag(AboutCreditsSectionTestTag.Title)
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
            testTag = ContributorsItem,
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
            testTag = StaffItem,
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
            label = stringResource(AboutRes.string.sponsor),
            testTag = SponsorsItem,
            onClickAction = onSponsorsItemClick,
            modifier = modifier
                .padding(
                    horizontal = 16.dp,
                ),
        )
    }
}
