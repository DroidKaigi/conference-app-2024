package io.github.droidkaigi.confsched.staff

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val staffScreenRoute = "staff"

fun NavGraphBuilder.staffScreens(
) {
    composable(staffScreenRoute) {
        StaffScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffScreen(
    modifier: Modifier = Modifier,
) {
}
