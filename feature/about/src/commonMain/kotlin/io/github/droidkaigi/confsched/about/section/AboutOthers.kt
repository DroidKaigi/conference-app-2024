package io.github.droidkaigi.confsched.about.section

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.FileCopy
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.about.generated.resources.code_of_conduct
import conference_app_2024.feature.about.generated.resources.license
import conference_app_2024.feature.about.generated.resources.others_title
import conference_app_2024.feature.about.generated.resources.privacy_policy
import io.github.droidkaigi.confsched.about.AboutRes
import io.github.droidkaigi.confsched.about.component.AboutContentColumn
import io.github.droidkaigi.confsched.about.section.AboutOthersSectionTestTag.CodeOfConductItem
import io.github.droidkaigi.confsched.about.section.AboutOthersSectionTestTag.LicenseItem
import io.github.droidkaigi.confsched.about.section.AboutOthersSectionTestTag.PrivacyPolicyItem
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Suppress("ConstPropertyName")
object AboutOthersSectionTestTag {
    const val Title = "AboutOthersTitle"
    const val CodeOfConductItem = "AboutOthersCodeOfConductItem"
    const val LicenseItem = "AboutOthersLicenseItem"
    const val PrivacyPolicyItem = "AboutOthersPrivacyPolicyItem"
}

fun LazyListScope.aboutOthers(
    modifier: Modifier = Modifier,
    onCodeOfConductItemClick: () -> Unit,
    onLicenseItemClick: () -> Unit,
    onPrivacyPolicyItemClick: () -> Unit,
) {
    item {
        Text(
            text = stringResource(AboutRes.string.others_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier
                .testTag(AboutOthersSectionTestTag.Title)
                .padding(
                    start = 16.dp,
                    top = 32.dp,
                    end = 16.dp,
                ),
        )
    }
    item {
        AboutContentColumn(
            leadingIcon = Outlined.Gavel,
            label = stringResource(AboutRes.string.code_of_conduct),
            testTag = CodeOfConductItem,
            onClickAction = onCodeOfConductItemClick,
            modifier = modifier
                .padding(
                    horizontal = 16.dp,
                ),
        )
    }
    item {
        AboutContentColumn(
            leadingIcon = Outlined.FileCopy,
            label = stringResource(AboutRes.string.license),
            testTag = LicenseItem,
            onClickAction = onLicenseItemClick,
            modifier = modifier
                .padding(
                    horizontal = 16.dp,
                ),
        )
    }
    item {
        AboutContentColumn(
            leadingIcon = Outlined.PrivacyTip,
            label = stringResource(AboutRes.string.privacy_policy),
            testTag = PrivacyPolicyItem,
            onClickAction = onPrivacyPolicyItemClick,
            modifier = modifier
                .padding(
                    horizontal = 16.dp,
                ),
        )
    }
}

@Preview
@Composable
internal fun AboutOthersPreview() {
    KaigiTheme {
        Surface {
            LazyColumn {
                aboutOthers(
                    onCodeOfConductItemClick = {},
                    onLicenseItemClick = {},
                    onPrivacyPolicyItemClick = {},
                )
            }
        }
    }
}
