# DroidKaigi 2024 official app

## Development

<img width="815" alt="image" src="https://github.com/DroidKaigi/conference-app-2024/assets/1386930/f0a9a5a2-e10d-470c-9e7d-0ad15128f1f5">

### A Guide for Contributors 1: Understanding the App's Data Flow

To contribute to the app effectively, understanding its data flow is crucial for comprehending the app's code structure. Let's examine this further.

#### 1. Displaying Sessions on the Timetable Screen

This section explains how the TimetableScreen is set up to display sessions, detailing the flow from the presenter to the UI state.

```
              TimetableScreenUiState
timetableScreenPresenter ----> TimetableScreen
```

<img width="603" alt="image" src="https://github.com/DroidKaigi/conference-app-2024/assets/1386930/f8b2b564-6a0b-4617-83cb-02068459dc0a">


```kotlin
@Composable
fun TimetableScreen(
    ...
    eventEmitter: EventEmitter<TimetableScreenEvent> = rememberEventEmitter<TimetableScreenEvent>(),
    uiState: TimetableScreenUiState = timetableScreenPresenter(
        events = eventEmitter,
    ),
) {
    ...
    TimetableScreen(
        uiState = uiState,
        onBookmarkClick = { item, bookmarked ->
            eventEmitter.tryEmit(TimetableScreenEvent.Bookmark(item, bookmarked))
        },
```

#### 2. User Interaction with the Bookmark Button

Here, the interaction of bookmarking a session is detailed, showcasing how events trigger updates within the presenter.

<img width="428" alt="image" src="https://github.com/DroidKaigi/conference-app-2024/assets/1386930/23109f98-dd7c-4d8c-bc2b-55ad7f259680">

```
      TimetableScreenEvent.Bookmark
TimetableScreen ----> timetableScreenPresenter
```

```kotlin
@Composable
fun timetableScreenPresenter(
    events: Flow<TimetableScreenEvent>,
    sessionsRepository: SessionsRepository = localSessionsRepository(),
): TimetableScreenUiState = providePresenterDefaults { userMessageStateHolder ->
    ...
    SafeLaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is Bookmark -> {
                    sessionsRepository.toggleBookmark(event.timetableItem.id)
                }
                ...
            }
        }
    }
    ...
}
```

#### 3. Saving the bookmarked session

This part outlines how bookmark changes are persisted in the user's data store, demonstrating the repository's role in data handling.



```
        TimetableItemId
SessionsRepository ----> userDataStore
```




```kotlin
    override suspend fun toggleBookmark(id: TimetableItemId) {
        userDataStore.toggleFavorite(id)
    }
```

#### 4. Recomposing the Repository's Timetable Upon Bookmarking

Focuses on how user actions (like bookmarking) cause the repository to update and recompose the timetable data.

```
      favoriteSessions
userDataStore ----> Repository
```

```kotlin
@Composable
public override fun timetable(): Timetable {
    val timetable by remember {
        ...
    }.safeCollectAsRetainedState(Timetable())
    val favoriteSessions by remember {
        userDataStore.getFavoriteSessionStream()
    }.safeCollectAsRetainedState(persistentSetOf())

    return timetable.copy(bookmarks = favoriteSessions)
}
```

`safeCollectAsRetainedState()` is a utility function that allows us to safely collect a Flow in a Composable function. It retains the state across recompositions and Compose navigation, ensuring that the data is not lost when the Composable function is recomposed.
For more information, see the [Rin](https://github.com/takahirom/Rin) library.

#### 5. Passing the Updated Timetable to the Presenter

Describes the flow of updated session data back to the screen presenter, highlighting how the UI state is refreshed.


<img width="518" alt="image" src="https://github.com/DroidKaigi/conference-app-2024/assets/1386930/9ad59696-0b94-4d3f-84c4-71ae0402680b">


```
                 Timetable
SessionsRepository ----> timetableScreenPresenter
```

```kotlin
@Composable
fun timetableScreenPresenter(
    events: Flow<TimetableScreenEvent>,
    sessionsRepository: SessionsRepository = localSessionsRepository(),
): TimetableScreenUiState = providePresenterDefaults { userMessageStateHolder ->
    // Sessions are updated in the timetable() function
    val sessions by rememberUpdatedState(sessionsRepository.timetable())
    ...
    val timetableUiState by rememberUpdatedState(
        timetableSheet(
            sessionTimetable = sessions,
            uiType = timetableUiType,
        ),
    )
    ...
    SafeLaunchedEffect(Unit) {
        events.collect { event ->
            ...
        }
    }
    TimetableScreenUiState(
        contentUiState = timetableUiState,
        timetableUiType = timetableUiType,
        userMessageStateHolder = userMessageStateHolder
    )
}
```

#### 6. Displaying the Updated Timetable on the Screen

This final step illustrates how the updated timetable is displayed on the screen, completing the cycle of user interaction and data update.
<img width="483" alt="image" src="https://github.com/DroidKaigi/conference-app-2024/assets/1386930/0f4e3371-5a2e-47c4-a065-f0ad676b766f">


```
          TimetableScreenUiState
timetableScreenPresenter ----> TimetableScreen
```

```kotlin
@Composable
fun TimetableScreen(
    ...,
    uiState: TimetableScreenUiState = timetableScreenPresenter(
        events = eventEmitter,
    ),
) {
    ...
    TimetableScreen(
        uiState = uiState,
```        

###  A Guide for Contributors 2: Understanding the App's Testing

The DroidKaigi 2024 official app utilizes a comprehensive testing strategy that combines Behavior Driven Development (BDD), Robolectric, and Roborazzi. This integrated approach enhances app stability, ensures UI correctness, and streamlines the testing process for Android development.

#### Key Components

**Robolectric**: A framework that executes Android tests directly on the JVM, allowing tests to run without requiring a physical device or emulator. This approach significantly speeds up test execution and allows for easier integration with continuous integration systems.


```kotlin
@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class TimetableScreenTest(private val testCase: DescribedBehavior<TimetableScreenRobot>) {

    @get:Rule
    @BindValue val robotTestRule: RobotTestRule = RobotTestRule(this)

    @Inject
    lateinit var timetableScreenRobot: TimetableScreenRobot

    @Test
    fun runTest() {
        runRobot(timetableScreenRobot) {
            testCase.execute(timetableScreenRobot)
        }
    }
```

**BDD**: Expresses clear behavior of the app.

We will delve into BDD aspect in the next section.

```kotlin
companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun behaviors(): List<DescribedBehavior<TimetableScreenRobot>> {
            return describeBehaviors<TimetableScreenRobot>(name = "TimetableScreen") {
                describe("when server is operational") {
                    run {
                        setupTimetableServer(ServerStatus.Operational)
                        setupTimetableScreenContent()
                    }
                    itShould("show timetable items") {
                        captureScreenWithChecks(checks = {
                            checkTimetableItemsDisplayed()
                        })
```

This will generate test names like `TimetableScreen - when the server is operational - it should display timetable items`.  
And generate a image named `TimetableScreen - when the server is operational - it should display timetable items.png`.

**Robot Pattern**: Robots separate the "what" (test intent) from the "how" (UI interactions).

Test Cases (What):

```kotlin
itShould("show timetable items") {
    captureScreenWithChecks(checks = {
        checkTimetableItemsDisplayed() 
    })
}
```

Robot Implementation (How):

```kotlin
class TimetableScreenRobot {
    ...
    fun clickFirstSessionBookmark() {
        composeTestRule 
            .onAllNodes(hasTestTag(TimetableListItemBookmarkIconTestTag))
            .onFirst()
            .performClick()
        waitUntilIdle() 
    }
    ...
}
```

**Roborazzi Integration**: Roborazzi captures screenshots during tests for visual regression detection.

```kotlin
fun captureScreenWithChecks(checks: () -> Unit) {
        robotTestRule.captureScreen()
        checks()
}
```

**Advantages**

* Speed: JVM-based tests run significantly faster than traditional instrumented tests, allowing for quicker feedback during development.
* Clarity: BDD improves test readability, making it easier for both developers and non-technical stakeholders to understand test scenarios.
* Maintainability: The Robot Pattern simplifies UI test maintenance by centralizing UI interaction logic, reducing the impact of UI changes on test code.
* Visual Consistency: Roborazzi's screenshot comparison feature helps detect unintended UI changes early in the development process, ensuring a consistent user experience.

### This Year's Experimental Challenges

#### Rewriting Coroutine Flow to Composable Function

This year, we've taken a significant step in our app architecture by leveraging Composable functions not just for UI, but also for ViewModels and Repositories. This approach aligns with the growing understanding in the Android community that Compose's runtime is a powerful tool for managing tree-like structures and state, extending far beyond its initial UI-focused perception.  
Our motivation stems from the belief that Composable functions can lead to more readable, maintainable, and conceptually unified code across our application layers. This shift represents a move towards treating our entire app as a composable structure, not just its visual elements.
Let's look at how this transformation has impacted our Repository implementation:

Flow-based Repository (Old version)

```kotlin
override fun getTimetableStream(): Flow<Timetable> = flow {
    var first = true
    combine(
        sessionCacheDataStore.getTimetableStream().catch { e ->
            Logger.d(
                "DefaultSessionsRepository sessionCacheDataStore.getTimetableStream catch",
                e,
            )
            sessionCacheDataStore.save(sessionsApi.sessionsAllResponse())
            emitAll(sessionCacheDataStore.getTimetableStream())
        },
        userDataStore.getFavoriteSessionStream(),
    ) { timetable, favorites ->
        timetable.copy(bookmarks = favorites)
    }.collect {
        if (!it.isEmpty()) {
            emit(it)
        }
        if (first) {
            first = false
            Logger.d("DefaultSessionsRepository onStart getTimetableStream()")
            sessionCacheDataStore.save(sessionsApi.sessionsAllResponse())
            Logger.d("DefaultSessionsRepository onStart fetched")
        }
    }
}
```

Now we can write a Repository like this. We don't need to use combine.

Composable Function-based Repository (New version)

```kotlin
@Composable
public override fun timetable(): Timetable {
    var first by remember { mutableStateOf(true) }
    SafeLaunchedEffect(first) {
        if (first) {
            Logger.d("DefaultSessionsRepository onStart getTimetableStream()")
            sessionCacheDataStore.save(sessionsApi.sessionsAllResponse())
            Logger.d("DefaultSessionsRepository onStart fetched")
            first = false
        }
    }

    val timetable by remember {
        sessionCacheDataStore.getTimetableStream().catch { e ->
            Logger.d(
                "DefaultSessionsRepository sessionCacheDataStore.getTimetableStream catch",
                e,
            )
            sessionCacheDataStore.save(sessionsApi.sessionsAllResponse())
            emitAll(sessionCacheDataStore.getTimetableStream())
        }
    }.safeCollectAsRetainedState(Timetable())
    val favoriteSessions by remember {
        userDataStore.getFavoriteSessionStream()
    }.safeCollectAsRetainedState(persistentSetOf())

    Logger.d { "DefaultSessionsRepository timetable() count=${timetable.timetableItems.size}" }
    return timetable.copy(bookmarks = favoriteSessions)
}
```

We are exploring the possibility of using Compose.

#### Behavior driven development and screenshot testing

We aim to enhance our app's quality by adopting BDD methodologies similar to Ruby and JavaScript, alongside implementing screenshot testing.   
We used to have a test like `launchTimetableShot()` that captures a screenshot of the timetable screen. But we found that we don't know what to check in the screenshot.
The reason why we chose BDD is that it clearly defines the app's behavior and ensures that the app functions as expected.  
To effectively capture screenshots, we utilize Robolectric integrated with Roborazzi. Below is the Kotlin code snippet we employ for our BDD tests:  

```kotlin
companion object {
@JvmStatic
@ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
fun behaviors(): List<DescribedBehavior<TimetableScreenRobot>> {
    return describeBehaviors<TimetableScreenRobot>(name = "TimetableScreen") {
        describe("when server is operational") {
            run {
                setupTimetableServer(ServerStatus.Operational)
                setupTimetableScreenContent()
            }
            itShould("show timetable items") {
                captureScreenWithChecks(checks = {
                    checkTimetableItemsDisplayed()
                })
            }
            describe("click first session bookmark") {
                run {
                    clickFirstSessionBookmark()
                }
                itShould("show bookmarked session") {
                    captureScreenWithChecks{
                        checkFirstSessionBookmarked()
                    }
                }
            }
```

The test names are formatted as follows: `TimetableScreen - when the server is operational - it should display timetable items`.   
Correspondingly, screenshots are saved with names like `TimetableScreen - when the server is operational - it should display timetable items.png`.   
While screenshots are invaluable for debugging, they alone do not suffice to ensure app quality, as changes can be missed. Therefore, we enforce rigorous content checks during screenshot capture using the `captureScreenWithChecks` function.

#### Flexible Integration of Compose Multiplatform in iOS Apps
This chapter demonstrates the practicality of Compose Multiplatform by showcasing its adaptability at various levels within an iOS application. We introduce a settings screen that allows toggling Compose Multiplatform integration for:

* Full app integration (default: off, runs natively on iOS)
* Screen-level integration (e.g., Screen A)
* Component-level integration (e.g., Screen B's presenter)

This approach demonstrates flexible adaptation between iOS and Android platforms, enabling performance optimization and versatile development strategies.
