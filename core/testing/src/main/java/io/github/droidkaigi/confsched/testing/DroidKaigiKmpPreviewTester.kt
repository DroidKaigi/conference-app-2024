package io.github.droidkaigi.confsched.testing

import com.github.takahirom.roborazzi.ComposePreviewTester
import com.github.takahirom.roborazzi.captureRoboImage
import sergio.sastre.composable.preview.scanner.core.preview.ComposablePreview
import sergio.sastre.composable.preview.scanner.jvm.JvmAnnotationScanner

class DroidKaigiKmpPreviewTester : ComposePreviewTester<JvmAnnotationScanner.DesktopPreviewInfo> {
    override fun previews(vararg packages: String): List<ComposablePreview<JvmAnnotationScanner.DesktopPreviewInfo>> {
        return JvmAnnotationScanner("org.jetbrains.compose.ui.tooling.preview.Preview")
            .scanPackageTrees(*packages)
            .getPreviews()
    }

    override fun test(preview: ComposablePreview<JvmAnnotationScanner.DesktopPreviewInfo>) {
        captureRoboImage("${preview.methodName}.png") {
            println(preview.methodName)
            preview()
        }
    }
}
