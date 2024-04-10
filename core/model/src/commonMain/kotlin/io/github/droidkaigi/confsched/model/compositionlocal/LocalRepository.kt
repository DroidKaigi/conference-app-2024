package io.github.droidkaigi.confsched.model.compositionlocal

import androidx.compose.runtime.compositionLocalOf
import kotlin.reflect.KClass

val LocalRepositories = compositionLocalOf<Map<KClass<*>, Any>> {
    error("No LocalRepository provided")
}
