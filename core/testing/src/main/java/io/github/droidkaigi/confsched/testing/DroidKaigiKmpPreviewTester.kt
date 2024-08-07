package io.github.droidkaigi.confsched.testing

import com.github.takahirom.roborazzi.ComposePreviewTester
import com.github.takahirom.roborazzi.captureRoboImage
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview
import sergio.sastre.composable.preview.scanner.jvm.JvmAnnotationScanner
import sergio.sastre.composable.preview.scanner.jvm.JvmAnnotationScanner.DesktopPreviewInfo

class DroidKaigiKmpPreviewTester : ComposePreviewTester<JvmAnnotationScanner.DesktopPreviewInfo> {
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
