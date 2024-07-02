package io.github.droidkaigi.confshed.profilecard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

const val profileCardScreenRoute = "profilecard"
const val ProfileCardScreenTestTag = "ProfileCardTestTag"

@Composable
fun ProfileCardScreen(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { padding ->
        Box(
            modifier = Modifier.padding(padding),
        ) {
            Text("ProfileCard")
        }
    }
}
