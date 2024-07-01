package io.github.droidkaigi.confsched.sessions.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.GTranslate
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.droidkaigi.confsched.model.Lang

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableItemDetailTopAppBar(
    isLangSelectable: Boolean,
    onNavigationIconClick: () -> Unit,
    onSelectedLanguage: (Lang) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    TopAppBar(
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
                                text = "日本語",
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
                                text = "English",
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
