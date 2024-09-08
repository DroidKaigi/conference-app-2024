package io.github.droidkaigi.confsched.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
expect fun provideMainNestedNavController(): NavHostController
