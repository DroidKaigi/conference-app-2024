package io.github.droidkaigi.confsched.testing.rules

import android.graphics.drawable.ColorDrawable
import androidx.test.core.app.ApplicationProvider
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.test.FakeImageLoaderEngine
import coil3.test.default
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class CoilRule : TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
        val engine = FakeImageLoaderEngine.Builder()
            .default(ColorDrawable(android.graphics.Color.BLUE))
            .build()
        val imageLoader =
            ImageLoader.Builder(ApplicationProvider.getApplicationContext())
                .components { add(engine) }
                .build()
        SingletonImageLoader.setUnsafe(imageLoader)
    }
}
