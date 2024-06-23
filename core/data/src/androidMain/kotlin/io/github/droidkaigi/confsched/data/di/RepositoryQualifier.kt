package io.github.droidkaigi.confsched.data.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.github.droidkaigi.confsched.model.compositionlocal.LocalRepositories
import javax.inject.Inject
import javax.inject.Qualifier

@Qualifier
public annotation class RepositoryQualifier

public class RepositoryProvider @Inject constructor(
    @RepositoryQualifier public val repositories: Map<Class<out Any>, @JvmSuppressWildcards Any>,
) {
    private val repositoriesMap = repositories
        .map { (k, v) ->
            k.kotlin to v as Any
        }.toMap()

    @Composable
    public fun Provide(content: @Composable () -> Unit) {
        CompositionLocalProvider(
            LocalRepositories provides repositoriesMap,
        ) {
            content()
        }
    }
}
