# DroidKaigi 2024 official app

## Development

<img width="815" alt="image" src="https://github.com/DroidKaigi/conference-app-2024/assets/1386930/f0a9a5a2-e10d-470c-9e7d-0ad15128f1f5">

### A Guide for Contributors 1: Understanding the App's Data Flow

For contributing to the app, it is important to understand the data flow of the app.

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


```                 Timetable
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

For ensuring the stability and correctness of the DroidKaigi 2024 official app, we have a robust testing strategy in place, centered around Behavior Driven Development (BDD), Robolectric, and Roborazzi. This guide will walk you through the key aspects of our testing approach, demonstrating how these tools work together to facilitate reliable UI testing.

 1. Setting the Stage with Robolectric

At the heart of our testing setup lies Robolectric, a framework that allows us to execute Android tests directly on the JVM. This eliminates the dependency on physical devices or emulators, resulting in significantly faster test execution times.

```
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

In the provided snippet, `@RunWith(ParameterizedRobolectricTestRunner::class)` indicates that our test class will be executed using Robolectric. `@HiltAndroidTest`, on the other hand, signals that Hilt, our dependency injection framework, will be actively participating in the test setup.

 2. Humanizing the Tests with BDD

To ensure our tests are easily understandable and maintainable, we've embraced BDD principles. BDD encourages expressing test cases in a clear, human-readable format, much like those commonly seen in Ruby and JavaScript testing frameworks. 

```
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

Here, `describeBehaviors`, `describe', and `itShould` constructs not only structure our tests but also form part of the test names themselves. This human-readable format enhances the clarity of our tests.

 3. Streamlining Interactions with Robots

Central to our BDD implementation is the concept of "Robots." But these are not just any robots; they are meticulously crafted to embody the principles of clear, maintainable testing. Each Robot is dedicated to a specific screen or UI component and operates under a fundamental philosophy: separate the "what" from the "how."

 The "What" - Declaring Intent in Test Cases

 In our test cases, we focus on expressing the "what" – the desired behavior of the application. Consider this example:

itShould("show timetable items") {
    captureScreenWithChecks(checks = {
        checkTimetableItemsDisplayed() 
    })
}

The meaning is evident even without diving into implementation details. We are stating that when certain conditions are met (perhaps "when the server is operational"), we expect the timetable items to be displayed. 

 The "How" -  Abstraction within Robots

The "how" – the intricate steps to interact with UI elements – is neatly tucked away within our Robots. These Robots act as intermediaries, shielding the test cases from the complexities of UI manipulation.

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

Looking at the `TimetableScreenRobot`, the `clickFirstSessionBookmark` function demonstrates this encapsulation. It handles locating the bookmark icon (using `hasTestTag`), performing the click action, and even waiting for the UI to settle (with `waitUntilIdle`).

 4. Integrating Roborazzi for Visual Validation

While functional correctness is paramount, preventing visual regressions is equally important for a polished user experience. This is where Roborazzi comes into play. Roborazzi seamlessly integrates with our Robolectric tests, allowing us to capture screenshots during test execution and compare them against previously approved baseline images.

Test Class ---------> UI Interaction ---------> captureScreenWithChecks() ---------> Image Comparison

```
fun captureScreenWithChecks(checks: () -> Unit) {
        robotTestRule.captureScreen()
        checks()
}
```

We employ `captureScreenWithChecks` to initiate screenshot capture after a specific action is performed. This function is especially powerful when paired with BDD.  Consider the test name `TimetableScreen - when the server is operational - it should display timetable items`. After executing the test actions, `captureScreenWithChecks` takes a screenshot and saves it with the same name, providing a clear visual record of the app's state at that point in the test. 

Furthermore, the `checks` parameter allows for additional assertions after the screenshot is captured. This ensures we're not solely relying on visual comparisons, adding an extra layer of verification to guarantee both functional and visual correctness.

 5. The Power of This Approach

The combined utilization of Robolectric, Roborazzi, BDD, and Robots offers compelling advantages for maintaining the DroidKaigi app:

 Unmatched Speed:  Robolectric minimizes test execution time by running tests directly on the JVM, allowing for quick feedback during development.
 Enhanced Clarity: BDD, coupled with descriptive test names, makes our tests self-documenting, improving readability and maintainability.
 Robust UI Testing:  Robots promote code reuse in UI tests and simplify test maintenance when the UI layout changes.
 Guard Against Visual Regressions: Roborazzi provides a visual safety net, ensuring we catch and address any unintended UI changes early in the development process. 

By understanding and utilizing this testing framework, you contribute to the stability and quality of the DroidKaigi 2024 app, providing attendees with a seamless and enjoyable conference experience. 

### This Year's Experimental Challenges

#### Rewriting Coroutine Flow to Composable Function

In the app, we use Composable functions for ViewModels and Repositories. I believe Composable functions allow us to write more readable and maintainable code.

For example, we used to write a Repository like this.

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
