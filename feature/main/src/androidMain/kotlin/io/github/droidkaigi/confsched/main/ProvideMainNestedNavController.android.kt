package io.github.droidkaigi.confsched.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
actual fun provideMainNestedNavController(): NavHostController = rememberNavController()
