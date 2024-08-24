package io.github.droidkaigi.confsched.droidkaigiui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.SnackbarResult.Dismissed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import co.touchlab.kermit.Logger
import io.github.takahirom.rin.rememberRetained
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.coroutines.coroutineContext

/**
 * SnackbarMessageEffect shows a snackbar message when a [UserMessage] is emitted by [userMessageStateHolder].
 */
@Composable
fun SnackbarMessageEffect(
    snackbarHostState: SnackbarHostState,
    userMessageStateHolder: UserMessageStateHolder,
) {
    userMessageStateHolder.messageUiState.userMessages.firstOrNull()?.let { userMessage ->
        LaunchedEffect(userMessage) {
            if (userMessage.userMessageResult != null) return@LaunchedEffect
            val snackbarResult: SnackbarResult = if (userMessage.duration == null) {
                snackbarHostState.showSnackbar(
                    message = userMessage.message,
                    actionLabel = userMessage.actionLabel,
                )
            } else {
                snackbarHostState.showSnackbar(
                    message = userMessage.message,
                    actionLabel = userMessage.actionLabel,
                    duration = userMessage.duration,
                )
            }
            userMessageStateHolder.messageShown(
                messageId = userMessage.id,
                userMessageResult = when (snackbarResult) {
                    Dismissed -> UserMessageResult.Dismissed
                    ActionPerformed -> UserMessageResult.ActionPerformed
                },
            )
        }
    }
}

data class UserMessage(
    val message: String,
    val actionLabel: String? = null,
    val duration: SnackbarDuration? = null,
    val id: Int = randomUUIDHash(),
    val userMessageResult: UserMessageResult? = null,
)

data class MessageUiState(
    val userMessages: List<UserMessage> = emptyList(),
)

class UserMessageStateHolderImpl : UserMessageStateHolder {
    private var _messageUiState by mutableStateOf(MessageUiState())
    override val messageUiState get() = _messageUiState
    override fun messageShown(messageId: Int, userMessageResult: UserMessageResult) {
        val messages = _messageUiState.userMessages.toMutableList()
        messages.indexOfFirst { it.id == messageId }.let { userMessageIndex ->
            if (userMessageIndex == -1) return@let
            messages.set(
                userMessageIndex,
                messages[userMessageIndex].copy(userMessageResult = userMessageResult),
            )
        }
        Logger.d { "UserMessageStateHolderImpl.messageShown $messageId messages:$messages" }
        _messageUiState = _messageUiState.copy(userMessages = messages)
    }

    override suspend fun showMessage(
        message: String,
        actionLabel: String?,
        duration: SnackbarDuration?,
    ): UserMessageResult {
        Logger.d { "UserMessageStateHolderImpl.showMessage message:$message" }
        val messages = _messageUiState.userMessages.toMutableList()
        val newMessage = UserMessage(message, actionLabel = actionLabel, duration = duration)
        messages.add(newMessage)
        _messageUiState = _messageUiState.copy(userMessages = messages)
        val messageResult = try {
            snapshotFlow {
                _messageUiState
            }.filter { messageState ->
                val filterResult =
                    messageState.userMessages.find { it.id == newMessage.id }?.let { userMessage ->
                        val messageResult = userMessage.userMessageResult
                        messageResult != null
                    } ?: false
                Logger.d {
                    "UserMessageStateHolderImpl.showMessage filter message messageState:$messageState newMessage:$newMessage filterResult:$filterResult"
                }
                filterResult
            }
                .map { messageState ->
                    val userMessage =
                        checkNotNull(messageState.userMessages.find { it.id == newMessage.id })
                    Logger.d {
                        "UserMessageStateHolderImpl.showMessage map message messageState:$messageState newMessage:$newMessage userMessage:$userMessage"
                    }
                    checkNotNull(
                        userMessage
                            .userMessageResult,
                    )
                }
                .first()
        } catch (e: CancellationException) {
            Logger.d { "UserMessageStateHolderImpl.showMessage CancellationException" }
            removeMessageById(newMessage.id)
            coroutineContext.ensureActive()
            throw e
        }
        Logger.d { "UserMessageStateHolderImpl.showMessage after first messageResult:$messageResult" }
        removeMessageById(newMessage.id)
        Logger.d { "UserMessageStateHolderImpl.showMessage end _messageUiState:$_messageUiState" }
        return messageResult
    }

    private fun removeMessageById(newMessageId: Int) {
        val newMessages = _messageUiState.userMessages.toMutableList()
        newMessages.find { it.id == newMessageId }?.let { userMessage ->
            newMessages.remove(userMessage)
        }
        _messageUiState = _messageUiState.copy(userMessages = newMessages)
    }
}

@Composable
fun rememberUserMessageStateHolder(): UserMessageStateHolder {
    return rememberRetained { UserMessageStateHolderImpl() }
}

interface UserMessageStateHolder {
    val messageUiState: MessageUiState
    fun messageShown(messageId: Int, userMessageResult: UserMessageResult)
    suspend fun showMessage(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration? = null,
    ): UserMessageResult
}

enum class UserMessageResult {
    Dismissed,
    ActionPerformed,
}
