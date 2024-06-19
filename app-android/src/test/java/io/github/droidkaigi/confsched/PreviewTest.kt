package io.github.droidkaigi.confsched

import android.content.res.Configuration
import com.github.takahirom.roborazzi.DEFAULT_ROBORAZZI_OUTPUT_DIR_PATH
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import sergio.sastre.composable.preview.scanner.android.AndroidComposablePreviewScanner
import sergio.sastre.composable.preview.scanner.android.AndroidPreviewInfo
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview

@RunWith(ParameterizedRobolectricTestRunner::class)
class PreviewTest(
    private val preview: ComposablePreview<AndroidPreviewInfo>,
) {
    object RobolectricPreviewInfosApplier {
        fun applyFor(preview: ComposablePreview<AndroidPreviewInfo>) {
            val uiMode =
                when (preview.previewInfo.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
                    true -> "+night"
                    false -> "+notnight"
                }
            RuntimeEnvironment.setQualifiers(uiMode)
        }
    }

    @Test
    fun previewScreenshot() {
        val filePath =
            DEFAULT_ROBORAZZI_OUTPUT_DIR_PATH + "/" + preview.methodName + ".png"
        RobolectricPreviewInfosApplier.applyFor(preview)
        captureRoboImage(
            filePath = filePath,
        ) {
            preview()
        }
    }

    companion object {
        @ParameterizedRobolectricTestRunner.Parameters
        @JvmStatic
        fun components(): List<ComposablePreview<AndroidPreviewInfo>> {
            return AndroidComposablePreviewScanner()
                .scanPackageTrees("io.github.droidkaigi.confsched")
                .getPreviews()
        }
    }
}
