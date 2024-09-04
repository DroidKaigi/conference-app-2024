package io.github.droidkaigi.confsched.profilecard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import io.github.droidkaigi.confsched.compose.EventFlow
import io.github.droidkaigi.confsched.data.Repositories
import io.github.droidkaigi.confsched.droidkaigiui.composeViewController
import io.github.droidkaigi.confsched.droidkaigiui.presenterStateFlow
import io.github.droidkaigi.confsched.droidkaigiui.toUiImage
import kotlinx.coroutines.flow.Flow
import platform.UIKit.UIImage
import platform.UIKit.UIViewController
import kotlin.reflect.KClass

@Suppress("UNUSED")
fun profileCardViewController(
    repositories: Repositories,
    onClickShareProfileCard: (String, UIImage) -> Unit,
): UIViewController = composeViewController(repositories) {
    ProfileCardScreen(
        onClickShareProfileCard = { shareText, imageBitmap ->
            onClickShareProfileCard(
                shareText,
                imageBitmap.toUiImage() ?: UIImage(),
            )
        },
        // FIXME This is a workaround. For permanent support, we will get the inset value etc. from the iOS side and respond.
        contentPadding = PaddingValues(
            bottom = 30.dp, // Height of bottom tab bar
        ),
    )
}

@Suppress("unused")
fun profileCardScreenPresenterStateFlow(
    repositories: Map<KClass<*>, Any>,
    events: EventFlow<ProfileCardScreenEvent>,
): Flow<ProfileCardScreenState> = presenterStateFlow(
    events = events,
    repositories = repositories,
) {
    profileCardScreenPresenter(events)
}
