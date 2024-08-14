package io.github.droidkaigi.confsched

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.droidkaigi.confsched.designsystem.theme.KaigiTheme

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KaigiApp",
        ) {
            KaigiTheme {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Hello KaigiApp!")
                }
            }
        }
    }
}
