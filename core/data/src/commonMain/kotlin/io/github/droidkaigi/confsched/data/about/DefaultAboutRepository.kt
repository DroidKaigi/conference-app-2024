package io.github.droidkaigi.confsched.data.about

import androidx.compose.runtime.Composable
import io.github.droidkaigi.confsched.model.AboutRepository
import io.github.droidkaigi.confsched.model.BuildConfigProvider

public class DefaultAboutRepository(
    private val buildConfigProvider: BuildConfigProvider,
) : AboutRepository {
    @Composable
    override fun versionName(): String {
        return buildConfigProvider.versionName
    }
}
