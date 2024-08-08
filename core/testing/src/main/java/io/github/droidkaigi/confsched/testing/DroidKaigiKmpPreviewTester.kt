package io.github.droidkaigi.confsched.testing

import com.github.takahirom.roborazzi.ComposePreviewTester
import com.github.takahirom.roborazzi.ComposePreviewTester.Options
import com.github.takahirom.roborazzi.ComposePreviewTester.Options.JUnit4TestLifecycleOptions
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.captureRoboImage
import io.github.droidkaigi.confsched.testing.rules.CoilRule
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview
import sergio.sastre.composable.preview.scanner.jvm.JvmAnnotationScanner
import sergio.sastre.composable.preview.scanner.jvm.JvmAnnotationScanner.DesktopPreviewInfo

@OptIn(ExperimentalRoborazziApi::class)
class DroidKaigiKmpPreviewTester : ComposePreviewTester<JvmAnnotationScanner.DesktopPreviewInfo> {
    override fun options(): Options {
        return super.options().copy(
            testLifecycleOptions = JUnit4TestLifecycleOptions {
                CoilRule()
            },
        )
    }
    override fun previews(): List<ComposablePreview<DesktopPreviewInfo>> {
        return JvmAnnotationScanner("org.jetbrains.compose.ui.tooling.preview.Preview")
            .scanPackageTrees(*options().scanOptions.packages.toTypedArray())
            .getPreviews()
    }

    override fun test(preview: ComposablePreview<JvmAnnotationScanner.DesktopPreviewInfo>) {
        captureRoboImage("${preview.methodName}.png") {
            println(preview.methodName)
            preview()
        }
    }
}
