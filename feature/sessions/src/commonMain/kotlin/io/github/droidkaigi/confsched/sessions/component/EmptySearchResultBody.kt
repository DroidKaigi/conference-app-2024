package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import conference_app_2024.feature.sessions.generated.resources.Res
import conference_app_2024.feature.sessions.generated.resources.empty_search_result
import conference_app_2024.feature.sessions.generated.resources.empty_search_result_no_input
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EmptySearchResultBody(
    searchWord: String,
    modifier: Modifier = Modifier,
) {
    val message = if (searchWord.isEmpty()) {
        stringResource(Res.string.empty_search_result_no_input)
    } else {
        stringResource(Res.string.empty_search_result, searchWord)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
    ) {
        Image(
            painter = painterResource(Res.drawable.empty_search_result),
            contentDescription = null,
        )
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun EmptySearchResultBodyPreview() {
    KaigiTheme {
        EmptySearchResultBody(
            searchWord = "Input text",
        )
    }
}
