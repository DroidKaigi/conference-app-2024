package io.github.droidkaigi.confsched.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.github.takahirom.rin.rememberRetained

@Composable
actual fun provideMainNestedNavController(): NavHostController {
    val mainNestedNavController = rememberNavController()
    return rememberRetained { mainNestedNavController }
}
