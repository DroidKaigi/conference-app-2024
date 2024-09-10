package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import conference_app_2024.feature.sessions.generated.resources.search_sessions
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.sessions.SessionsRes
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

const val SearchTextFieldAppBarTextFieldTestTag = "SearchTextFieldAppBarTextField"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextFieldAppBar(
    searchWord: String,
    onChangeSearchWord: (String) -> Unit,
    onClickClear: () -> Unit,
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = SearchTextFieldAppBarDefaults.windowInsets(),
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
                    },
                ),
                decorationBox = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = searchWord,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = interactionSource,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        placeholder = {
                            Text(
                                text = stringResource(SessionsRes.string.search_sessions),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontFamily = FontFamily.Default,
                                ),
                            )
                        },
                    )
                },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .testTag(SearchTextFieldAppBarTextFieldTestTag),
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
        windowInsets = windowInsets,
    )
}

object SearchTextFieldAppBarDefaults {
    @Composable
    fun windowInsets() = WindowInsets.displayCutout.union(WindowInsets.systemBars).only(
        WindowInsetsSides.Horizontal + WindowInsetsSides.Top,
    )
}

@Preview
@Composable
fun SearchTextFieldAppBarPreview_FilledSearchWord() {
    KaigiTheme {
        SearchTextFieldAppBar(
            searchWord = "Input text",
            onChangeSearchWord = {},
            onClickClear = {},
            onClickBack = {},
        )
    }
}

@Preview
@Composable
fun SearchTextFieldAppBarPreview_EmptySearchWord() {
    KaigiTheme {
        SearchTextFieldAppBar(
            searchWord = "",
            onChangeSearchWord = {},
            onClickClear = {},
            onClickBack = {},
        )
    }
}
