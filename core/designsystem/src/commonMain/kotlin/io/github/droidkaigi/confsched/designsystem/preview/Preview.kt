package io.github.droidkaigi.confsched.designsystem.preview

@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION,
)
@Repeatable
annotation class Preview(
    val name: String = "",
    val group: String = "",
    val apiLevel: Int = -1,
    val widthDp: Int = -1,
    val heightDp: Int = -1,
    val locale: String = "",
    val fontScale: Float = 1f,
    val showSystemUi: Boolean = false,
    val showBackground: Boolean = false,
    val backgroundColor: Long = 0,
    val uiMode: Int = 0,
    val device: String = "",
    val wallpaper: Int = -1,
)
