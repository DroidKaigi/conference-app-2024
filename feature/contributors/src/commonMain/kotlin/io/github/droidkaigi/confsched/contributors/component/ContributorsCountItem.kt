package io.github.droidkaigi.confsched.contributors.component

import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.contributors.generated.resources.contributor_person
import conference_app_2024.feature.contributors.generated.resources.contributor_total
import io.github.droidkaigi.confsched.contributors.ContributorsRes
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ContributorsCountItem(
    totalContributor: Int,
    modifier: Modifier = Modifier,
) {
    var targetValue by rememberSaveable { mutableStateOf(0) }
    val animatedTotalContributor by animateIntAsState(
        targetValue = targetValue,
        animationSpec = tween(
            delayMillis = 300,
            durationMillis = 1000,
            easing = EaseOutQuart,
        ),
    )
    LaunchedEffect(totalContributor) {
        targetValue = totalContributor
    }
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Text(
            text = stringResource(ContributorsRes.string.contributor_total),
            style = MaterialTheme.typography.titleMedium,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = "$animatedTotalContributor",
                style = MaterialTheme.typography.headlineLarge,
            )
            Text(
                text = stringResource(ContributorsRes.string.contributor_person),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
    }
}
