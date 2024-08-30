![image](https://github.com/user-attachments/assets/11e73360-7a23-4e95-a549-da612aa20d7d)

# DroidKaigi 2024 official app

[DroidKaigi](https://2024.droidkaigi.jp/) is celebrating 10th year this time! This is a conference tailored for Android developers for enhancing sharing knowledge and communication.
It's scheduled to take place for 3 days, on 11-13 September 2024.

## Features

In addition to the standard features of a conference app, the DroidKaigi 2024 official app offers the following functionalities:

- **Timetable**: View the conference schedule and bookmark sessions.
- **Profile cards**: Create and share your profile card with other attendees.
- **Contributors**: Discover the contributors behind the app.
...and more!

![image](https://github.com/user-attachments/assets/d1aeccc1-1e8e-475f-9c51-72fb595c6563)

## Try the app

You can try the app on your device by clicking the button below. 
[<img src="https://dply.me/wfhpc2/button/large" alt="Try it on your device via DeployGate">](https://dply.me/wfhpc2#install)

## Contributing

We always welcome contributions!

For a detailed step-by-step guide on how to contribute, please see [CONTRIBUTING.md](CONTRIBUTING.md). This guide will walk you through the process from setting up your environment to submitting your pull request.

For Japanese speakers, a Japanese version of the contribution guide is available at [CONTRIBUTING.ja.md](CONTRIBUTING.ja.md).

コントリビューションの詳細な手順については、[CONTRIBUTING.ja.md](CONTRIBUTING.ja.md)をご覧ください。初めての方でも分かりやすいステップバイステップのガイドを用意しています。

## Requirements
Stable Android Studio Koala or higher. You can download it from [this page](https://developer.android.com/studio).

## Design

You can check out the design on Figma.

[DroidKaigi 2024 App UI](https://www.figma.com/design/XUk8WMbKCeIdWD5cz9P9JC/DroidKaigi-2024-App-UI?node-id=54795-26746&t=DgZuFVd0sduq6vUy-0)

**Designer**: [nobonobopurin](https://github.com/nobonobopurin)

## Development

### Overview of the architecture

In addition to general Android practices, we are exploring and implementing various concepts. Details for each are discussed further in this README.

<img width="806" alt="image" src="https://github.com/user-attachments/assets/b4f5682d-839b-4ab9-9f43-bcfde9a8eefb">

### Understanding the App's Data Flow

To contribute to the app effectively, understanding its data flow is crucial for comprehending the app's code structure. Let's examine this further.

#### 1. Displaying Sessions on the Timetable Screen

This section explains how the TimetableScreen is set up to display sessions, detailing the flow from the presenter to the UI state. We are categorizing UI Composable functions according to [last year's categorization](https://github.com/DroidKaigi/conference-app-2023?tab=readme-ov-file#composable-function-categorization).


```
              TimetableScreenUiState
timetableScreenPresenter ----> TimetableScreen
```

<img width="711" alt="image" src="https://github.com/user-attachments/assets/2032e34b-933d-4964-92a8-831ea254cedd">

```kotlin
@Composable
fun TimetableScreen(
    ...
    eventFlow: EventFlow<TimetableScreenEvent> = rememberEventFlow<TimetableScreenEvent>(),
    uiState: TimetableScreenUiState = timetableScreenPresenter(
        events = eventFlow,
    ),
) {
    ...
    TimetableScreen(
        uiState = uiState,
        onBookmarkClick = { item, bookmarked ->
            eventFlow.tryEmit(TimetableScreenEvent.Bookmark(item, bookmarked))
        },
```

#### 2. User Interaction with the Bookmark Button

Here, the interaction of bookmarking a session is detailed, showcasing how events trigger updates within the presenter.

<img width="494" alt="image" src="https://github.com/user-attachments/assets/715c053a-1b06-4523-8016-44d16f1a34f2">

```
      TimetableScreenEvent.Bookmark
TimetableScreen ----> timetableScreenPresenter -> sessionsRepository
```

```kotlin
@Composable
fun timetableScreenPresenter(
    events: EventFlow<TimetableScreenEvent>,
    sessionsRepository: SessionsRepository = localSessionsRepository(),
): TimetableScreenUiState = providePresenterDefaults { userMessageStateHolder ->
    ...
    EventEffect(Unit) { event ->
        when (event) {
            is Bookmark -> {
                sessionsRepository.toggleBookmark(event.timetableItem.id)
            }
            ...
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
For more information about retained states, refer to the [Rin](https://github.com/takahirom/Rin) library.

#### 5. Passing the Updated Timetable to the Presenter

Describes the flow of updated session data back to the screen presenter, highlighting how the UI state is refreshed.


<img width="386" alt="image" src="https://github.com/user-attachments/assets/6b69304a-ee4f-4ed7-93ff-6c4256264f8a">

```
                 Timetable
SessionsRepository ----> timetableScreenPresenter
```

```kotlin
@Composable
fun timetableScreenPresenter(
    events: EventFlow<TimetableScreenEvent>,
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
    EventEffect(events) { event ->
        ...
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
        events = eventFlow,
    ),
) {
    ...
    TimetableScreen(
        uiState = uiState,
```        

### How to Check Composable Preview


Currently, Android Studio doesn't support Composable Preview in the commonMain sourceset. Therefore, we are using the Roborazzi IDE Plugin to check Composable Preview.

When you open a Composable file, you can see the RoborazziPreview on the right side of the file.

<img width="48" alt="image" src="https://github.com/user-attachments/assets/3d0308d2-f435-4553-968c-1dcba77f615f">

To capture a screenshot of the Composable Preview, run the Roborazzi Gradle task in the RoborazziPreview.

<img width="319" alt="image" src="https://github.com/user-attachments/assets/52d2d386-188c-4d26-8c33-d8f4f769927e">

After running the task, you should see the screenshot in the RoborazziPreview.

![image](https://github.com/user-attachments/assets/8b38eb69-b737-4c2f-8e86-e3e5805d82e5)

###  Understanding the App's Testing

The DroidKaigi 2024 official app utilizes a comprehensive testing strategy that combines:

- **Behavior Driven Development (BDD)**: For clear, readable test scenarios
- **Robolectric**: For fast, JVM-based Android tests
- **Roborazzi**: For visual regression testing and providing debugging hints through screenshots
- **Robot Pattern**: For maintainable UI test code

This integrated approach enhances app stability, ensures UI correctness, and streamlines the testing process.

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
            .onAllNodes(hasTestTag(TimetableItemCardBookmarkIconTestTag))
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

We aim to enhance our app's quality by adopting BDD methodologies similar to Ruby and JavaScript tests, alongside implementing screenshot testing.   
We used to have a test like `@Test fun launchTimetableShot(){}` that captures a screenshot of the timetable screen. But we found that we don't know what to check in the screenshot.
The reason why we chose BDD is that it clearly defines the app's behavior and ensures that the app functions as expected.  
To effectively capture screenshots, we utilize Robolectric integrated with [Roborazzi](https://github.com/takahirom/roborazzi). Below is the Kotlin code snippet we employ for our BDD tests. The `describeBehaviors()` function used here is from the [RoboSpec](https://github.com/takahirom/robospec) library:

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
This feature demonstrates the practicality of Compose Multiplatform by showcasing its adaptability at various levels within an iOS application.  
We introduce a settings screen that allows toggling Compose Multiplatform integration for:

* Full app integration (runs the entire app on iOS)
* Screen-level integration (e.g., Using @ContributorScreen in the iOS app)
* Presenter (ViewModel) integration (e.g., Using @contributorScreenPresenter in the iOS app with SwiftUI)

This approach demonstrates flexible adaptation between iOS and Android platforms, enabling performance optimization by using native components and versatile development strategies.
