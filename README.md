# DroidKaigi 2024 official app

## Development

### Expanding the Use of Composable Functions Beyond UI

As Coroutines suspend functions replace Callbacks, Compose replaces Kotlin Coroutines Flow.
Compose can make ViewModel and Repository more readable and maintainable by the Composable function.

#### UI

It's almost the same as the normal usage of ViewModel. You can use `hiltViewModel()` to get ViewModel. And you can listen to the ViewModel's `uiState` Flow.

```kotlin
@Composable
fun TimetableItemDetailScreen(
    ...
    viewModel: TimetableItemDetailViewModel = hiltViewModel(),
) {
    ...
    val uiState by viewModel.uiState.collectAsState()
        ...

    TimetableItemDetailScreen(
        uiState = uiState,
        ...
        onBookmarkClick = {
            viewModel.take(TimetableItemDetailEvent.Bookmark(it))
        },
```

#### ViewModel Composable functions

ViewModels now launch Composable function to handle UI events and return UiStates.

```kotlin
@HiltViewModel
class TimetableItemDetailViewModel @Inject constructor(
    private val sessionsRepository: SessionsRepository,
    ...
) : ViewModel(),
    ComposeViewModel<TimetableItemDetailEvent, TimetableItemDetailScreenUiState> by ComposeViewModel(
        ..
        content = { events ->
            timetableItemDetailViewModel(
                events = events,
                ...,
                sessionsRepository = sessionsRepository,
            )
        },
    )


@Composable
fun timetableItemDetailViewModel(
    events: Flow<TimetableItemDetailEvent>,
    sessionsRepository: SessionsRepository,
    ..
): TimetableItemDetailScreenUiState {
    val timetableItemId by remember {
        sessionIdFlow
    }.collectAsState()
    // Caution: We need to use rememberUpdatedState because the state should be updated in the LaunchedEffect.
    val timetableItemStateWithBookmark by rememberUpdatedState(
        sessionsRepository
            .timetableItemWithBookmark(TimetableItemId(timetableItemId)),
    )
    ...
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is Bookmark -> {
                    val timetableItemWithBookmark = timetableItemStateWithBookmark
                    val timetableItem =
                        timetableItemWithBookmark?.first ?: return@collect
                    sessionsRepository.toggleBookmark(timetableItem.id)
                        ...
                }
                ...
            }
        }
    }
        ...
    val (timetableItem, bookmarked) = timetableItemStateWithBookmarkValue
    return TimetableItemDetailScreenUiState.Loaded(
        timetableItem = timetableItem,
        ...
    )
}
```

#### Repository Composable functions

Repository can also use Composable function. It allows you to use `remember` and `LaunchedEffect` in the Repository.

```kotlin
public class DefaultSessionsRepository(
    private val sessionsApi: SessionsApiClient,
    private val userDataStore: UserDataStore,
    private val sessionCacheDataStore: SessionCacheDataStore,
) : SessionsRepository {
    @Composable
    public override fun timetable(): Timetable {
        ...

        val timetable by remember {
            sessionCacheDataStore.getTimetableStream()
                .catch { e ->
                    ...
                }
        }.safeCollectAsState(Timetable())
        val favoriteSessions by remember {
            userDataStore.getFavoriteSessionStream()
        }.safeCollectAsState(persistentSetOf())

        return timetable.copy(bookmarks = favoriteSessions)
    }
```

#### Lifecycle aware ViewModel Composable functions

ViewModels are not lifecycle aware by default. 
For example, there are screens A and B, Screen**A**ViewModel will continue to work even if the screen is not visible.

Screen A(Screen**A**ViewModel) -> Screen B(Screen**B**ViewModel)

To solve this problem, you can pass the `Lifecycle` to ViewModel in the screen.

```kotin
@Composable
fun TimetableItemDetailScreen(
...
    viewModel: TimetableItemDetailViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        // pass the lifecycle to ViewModel
        viewModel.activeLifecycleWhile(lifecycleOwner.lifecycle)
    }
    val uiState by viewModel.uiState.collectAsState()
```

The mechanism is implemented in `DefaultComposeViewModel`. 
We uses [PausableMonotonicFrameClock](https://developer.android.com/reference/kotlin/androidx/compose/runtime/PausableMonotonicFrameClock) to pause and resume the Composable function.

> PausableMonotonicFrameClock should be used in cases where frames should not be produced under some conditions, such as when a window hosting a UI is not currently visible.

https://developer.android.com/reference/kotlin/androidx/compose/runtime/PausableMonotonicFrameClock

```kotlin
// When the screen is not visible, the ViewModel will be paused.
(composeCoroutineContext.monotonicFrameClock as PausableMonotonicFrameClock).pause()

// When the screen is visible, the ViewModel will be resumed.
(composeCoroutineContext.monotonicFrameClock as PausableMonotonicFrameClock).resume()
```

### Error handling of ViewModel Composable functions

Composable functions don't allow throwing exceptions and catching them in the parent function.
And if you don't handle errors in `LaunchedEffect` or `Flow.collectAsState` in the Composable function, the app will crash.

To handle errors in Composable functions, we provide `ComposeEffectErrorHandler` by CompositionLocal.

```kotlin
interface ComposeEffectErrorHandler {
    val errors: Flow<Throwable>
    suspend fun emit(throwable: Throwable)
}
```

And for handling errors in the Composable function, you can use `SafeLaunchedEffect{}` and `safeCollectAsState()`.
The function will catch the error and emit it to the `ComposeEffectErrorHandler`.

```kotlin
SafeLaunchedEffect(first) {
    if (first) {
        Logger.d("DefaultSessionsRepository onStart getTimetableStream()")
        sessionCacheDataStore.save(sessionsApi.sessionsAllResponse())
        Logger.d("DefaultSessionsRepository onStart fetched")
        first = false
    }
}
```
