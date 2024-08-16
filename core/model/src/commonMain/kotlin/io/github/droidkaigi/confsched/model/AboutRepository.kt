package io.github.droidkaigi.confsched.model

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories

interface AboutRepository {
    @Composable
    fun versionName(): String
}

@Composable
fun localAboutRepository(): AboutRepository {
    return LocalRepositories.current[AboutRepository::class] as AboutRepository
}
