package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextFieldAppBar(
    searchWord: String,
    onChangeSearchWord: (String) -> Unit,
    onClickClear: () -> Unit,
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    TopAppBar(
        title = {
            BasicTextField(
                value = searchWord,
                onValueChange = onChangeSearchWord,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        actions = {
            if (searchWord.isNotEmpty()) {
                IconButton(onClick = onClickClear) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Search Keyword",
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onClickBack) {
                Icon(
                    imageVector = Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        modifier = modifier,
    )
}

@Preview
@Composable
fun SearchTextFieldAppBarPreview() {
    KaigiTheme {
        SearchTextFieldAppBar(
            searchWord = "Input text",
            onChangeSearchWord = {},
            onClickClear = {},
            onClickBack = {},
        )
    }
}
