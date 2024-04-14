# DroidKaigi 2024 official app

## Development

### Points to note
* Use Composable function for ViewModels
* Use Composable function for Repositories
* How the Composable function can be lifecycle aware
* How to handle errors in Composable functions
* ???
* Something related to tests?
  * Roborazzi as debug tool of Robolectric tests
* Something other challenges?

### Things to decide

* [DONE] Which name should we use for the ViewModel function? `presenter` or `viewModel`?

The function will be like this. What this function do is ViewModel. But the function returns UiState.
The thing is Android developers are familiar with Android ViewModel and this function is not ViewModel.
So I think `presenter` is better.

```
@Composable
fun presenter(event: Flow<Event>): UiState
```

* If we use Composable functions for Repositories, how can iOS app use it?

If we use Composable functions for Repositories, iOS app can't use it directly.
We can workaround it by using molecule to convert it to flow.


-------

Work in progress.

### Expanding the Use of Composable Functions Beyond UI

As Coroutines suspend functions replace Callbacks, Compose replaces Kotlin Coroutines Flow.
Compose can make ViewModel and Repository more readable and maintainable by the Composable function.

#### UI

Now we don't use `ViewModel`. Instead, we use `presenter` which is a Composable function.

```kotlin
@Composable
fun TimetableItemDetailScreen(
    onNavigationIconClick: () -> Unit,
    onLinkClick: (url: String) -> Unit,
    onCalendarRegistrationClick: (TimetableItem) -> Unit,
    onShareClick: (TimetableItem) -> Unit,
) {
    val eventEmitter = rememberEventEmitter<TimetableItemDetailEvent>()
    val userMessageStateHolder = rememberUserMessageStateHolder()
    val uiState = timetableItemDetailPresenter(
        events = eventEmitter,
        userMessageStateHolder = userMessageStateHolder,
    )
    
        ...


    TimetableItemDetailScreen(
        uiState = uiState,
        onNavigationIconClick = onNavigationIconClick,
        onBookmarkClick = {
            eventEmitter.tryEmit(TimetableItemDetailEvent.Bookmark(it))
        },
```

#### ViewModel Composable functions

`Presenter` handles UI events and return UiStates.

```kotlin
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
