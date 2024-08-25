package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.GTranslate
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import conference_app_2024.feature.sessions.generated.resources.english
import conference_app_2024.feature.sessions.generated.resources.japanese
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched.designsystem.theme.LocalRoomTheme
import io.github.droidkaigi.confsched.model.Lang
import io.github.droidkaigi.confsched.sessions.SessionsRes
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableItemDetailTopAppBar(
    isLangSelectable: Boolean,
    onNavigationIconClick: () -> Unit,
    onSelectedLanguage: (Lang) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
) {
    // Allow content to be displayed at the statusBar when scrolling
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val variableTopPaddingHeight by remember(scrollBehavior.state.collapsedFraction) {
        mutableStateOf(statusBarHeight * (1f - scrollBehavior.state.collapsedFraction))
    }

    TopAppBar(
        modifier = modifier
            .background(LocalRoomTheme.current.dimColor)
            .padding(top = variableTopPaddingHeight)
            .consumeWindowInsets(WindowInsets.statusBars),
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = LocalRoomTheme.current.dimColor,
            scrolledContainerColor = LocalRoomTheme.current.dimColor,
        ),
        title = {},
        navigationIcon = {
            IconButton(onClick = { onNavigationIconClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                )
            }
        },
        actions = {
            if (isLangSelectable) {
                var expanded by remember { mutableStateOf(false) }

                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Outlined.GTranslate,
                        contentDescription = "Select Language",
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(SessionsRes.string.japanese),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        },
                        onClick = {
                            onSelectedLanguage(Lang.JAPANESE)
                            expanded = false
                        },
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(SessionsRes.string.english),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        },
                        onClick = {
                            onSelectedLanguage(Lang.ENGLISH)
                            expanded = false
                        },
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TimetableItemDetailTopAppBarPreview() {
    KaigiTheme {
        ProvideFakeRoomTheme {
            Surface {
                TimetableItemDetailTopAppBar(
                    isLangSelectable = true,
                    onNavigationIconClick = {},
                    onSelectedLanguage = {},
                    scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TimetableItemDetailTopAppBarUnSelectablePreview() {
    KaigiTheme {
        ProvideFakeRoomTheme {
            Surface {
                TimetableItemDetailTopAppBar(
                    isLangSelectable = false,
                    onNavigationIconClick = {},
                    onSelectedLanguage = {},
                    scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                )
            }
        }
    }
}
