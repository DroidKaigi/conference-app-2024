package io.github.droidkaigi.confsched.testing.robot

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onFirst
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.provideRoborazziContext
import com.github.takahirom.roborazzi.roboOutputName
import io.github.droidkaigi.confsched.data.contributors.ContributorsApiClient
import io.github.droidkaigi.confsched.data.contributors.FakeContributorsApiClient
import io.github.droidkaigi.confsched.data.eventmap.EventMapApiClient
import io.github.droidkaigi.confsched.data.eventmap.FakeEventMapApiClient
import io.github.droidkaigi.confsched.data.profilecard.ProfileCardDataStore
import io.github.droidkaigi.confsched.data.sessions.FakeSessionsApiClient
import io.github.droidkaigi.confsched.data.sessions.FakeSessionsApiClient.Status
import io.github.droidkaigi.confsched.data.sessions.SessionsApiClient
import io.github.droidkaigi.confsched.data.settings.SettingsDataStore
import io.github.droidkaigi.confsched.data.sponsors.FakeSponsorsApiClient
import io.github.droidkaigi.confsched.data.sponsors.SponsorsApiClient
import io.github.droidkaigi.confsched.data.staff.FakeStaffApiClient
import io.github.droidkaigi.confsched.data.staff.StaffApiClient
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableItemCard
import io.github.droidkaigi.confsched.droidkaigiui.component.TimetableItemCardTestTag
import io.github.droidkaigi.confsched.model.FontFamily
import io.github.droidkaigi.confsched.model.ProfileCard
import io.github.droidkaigi.confsched.model.Settings
import io.github.droidkaigi.confsched.model.fake
import io.github.droidkaigi.confsched.testing.coroutines.runTestWithLogging
import io.github.droidkaigi.confsched.testing.robot.ProfileCardDataStoreRobot.ProfileCardInputStatus
import io.github.droidkaigi.confsched.testing.robot.ProfileCardDataStoreRobot.ProfileCardInputStatus.AllNotEntered
import io.github.droidkaigi.confsched.testing.robot.ProfileCardDataStoreRobot.ProfileCardInputStatus.NoInputOtherThanImage
import io.github.droidkaigi.confsched.testing.robot.SettingsDataStoreRobot.SettingsStatus
import io.github.droidkaigi.confsched.testing.robot.SettingsDataStoreRobot.SettingsStatus.UseDotGothic16FontFamily
import io.github.droidkaigi.confsched.testing.robot.SettingsDataStoreRobot.SettingsStatus.UseSystemDefaultFont
import io.github.droidkaigi.confsched.testing.robot.SponsorsServerRobot.ServerStatus
import io.github.droidkaigi.confsched.testing.robot.TimetableItemCardRobot.Language
import io.github.droidkaigi.confsched.testing.robot.TimetableServerRobot.ServerStatus.Error
import io.github.droidkaigi.confsched.testing.robot.TimetableServerRobot.ServerStatus.Operational
import io.github.droidkaigi.confsched.testing.robot.TimetableServerRobot.ServerStatus.OperationalBothAssetAvailable
import io.github.droidkaigi.confsched.testing.robot.TimetableServerRobot.ServerStatus.OperationalMessageExists
import io.github.droidkaigi.confsched.testing.robot.TimetableServerRobot.ServerStatus.OperationalOnlySlideAssetAvailable
import io.github.droidkaigi.confsched.testing.robot.TimetableServerRobot.ServerStatus.OperationalOnlyVideoAssetAvailable
import io.github.droidkaigi.confsched.testing.rules.RobotTestRule
import io.github.droidkaigi.confsched.testing.utils.assertSemanticsProperty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestDispatcher
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.SensorEventBuilder
import org.robolectric.shadows.ShadowLooper
import org.robolectric.shadows.ShadowSensor
import org.robolectric.shadows.ShadowSensorManager
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

inline fun <reified T : ScreenRobot> runRobot(robot: T, noinline block: suspend T.() -> Unit) {
    robot.run(robot, block)
}

class DefaultScreenRobot @Inject constructor(
    override val robotTestRule: RobotTestRule,
    override val testDispatcher: TestDispatcher,
    private val composeScreenRobot: DefaultComposeScreenRobot,
    private val captureScreenRobot: DefaultCaptureScreenRobot,
    private val waitRobot: DefaultWaitRobot,
) :
    ScreenRobot,
    ComposeScreenRobot by composeScreenRobot,
    CaptureScreenRobot by captureScreenRobot,
    WaitRobot by waitRobot

interface ScreenRobot : ComposeScreenRobot, CaptureScreenRobot, WaitRobot {
    val robotTestRule: RobotTestRule
    val testDispatcher: TestDispatcher

    fun <T : ScreenRobot> run(thiz: T, block: suspend T.() -> Unit) {
        runTestWithLogging(context = testDispatcher, timeout = 30.seconds) {
            thiz.block()
        }
    }
}

class DefaultComposeScreenRobot @Inject constructor(
    private val robotTestRule: RobotTestRule,
) :
    ComposeScreenRobot {
    override val composeTestRule: ComposeTestRule
        get() = robotTestRule.composeTestRule
}

interface ComposeScreenRobot {
    val composeTestRule: ComposeTestRule
}

interface CaptureScreenRobot {
    fun captureScreenWithChecks(checks: () -> Unit)

    @Deprecated(
        message = "Use captureScreenWithChecks instead and add checks to ensure screen contents are correct",
        replaceWith = ReplaceWith("captureScreenWithChecks(checks)"),
    )
    fun captureScreenWithChecks()
}

fun todoChecks(@Suppress("UNUSED_PARAMETER") reason: String): () -> Unit {
    return {}
}

class DefaultCaptureScreenRobot @Inject constructor(private val robotTestRule: RobotTestRule) :
    CaptureScreenRobot {
    override fun captureScreenWithChecks(checks: () -> Unit) {
        val roboOutputName = roboOutputName()
        if (roboOutputName.contains("[") && roboOutputName.contains("]")) {
            val name = roboOutputName.substringAfter("[").substringBefore("]")
            val className =
                provideRoborazziContext().description?.className?.substringAfterLast(".")
            if (className == null) {
                robotTestRule.captureScreen(name)
                checks()
                return
            }
            robotTestRule.captureScreen("$className[$name]")
            checks()
            return
        }
        robotTestRule.captureScreen()
        checks()
    }

    @Deprecated(
        "Use captureScreenWithChecks instead and add checks to ensure screen contents are correct",
        replaceWith = ReplaceWith("captureScreenWithChecks(checks)"),
    )
    override fun captureScreenWithChecks() {
        captureScreenWithChecks { }
    }
}

interface WaitRobot {
    fun waitUntilIdle()
    fun wait5Seconds()
}

class DefaultWaitRobot @Inject constructor(
    private val robotTestRule: RobotTestRule,
    private val testDispatcher: TestDispatcher,
) : WaitRobot {
    override fun waitUntilIdle() {
        repeat(5) {
            robotTestRule.composeTestRule.waitForIdle()
            testDispatcher.scheduler.advanceUntilIdle()
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
        }
    }

    override fun wait5Seconds() {
        repeat(5) {
            testDispatcher.scheduler.advanceTimeBy(1.seconds)
            robotTestRule.composeTestRule.mainClock.advanceTimeBy(1000)
            ShadowLooper.runUiThreadTasksIncludingDelayedTasks()
        }
    }
}

interface FontScaleRobot {
    fun setFontScale(fontScale: Float)
}

class DefaultFontScaleRobot @Inject constructor() : FontScaleRobot {
    override fun setFontScale(fontScale: Float) {
        RuntimeEnvironment.setFontScale(fontScale)
    }
}

interface DeviceSetupRobot {
    fun setupTabletDevice()
}

class DefaultDeviceSetupRobot @Inject constructor() : DeviceSetupRobot {
    override fun setupTabletDevice() {
        RuntimeEnvironment.setQualifiers(RobolectricDeviceQualifiers.MediumTablet)
    }
}

interface SensorRobot {
    fun setupMockSensors(sensorTypes: List<Int>)
    fun cleanUpSensors()
    fun tiltPitch(pitch: Float = 10f)
    fun tiltRoll(roll: Float = 10f)
    fun tiltAzimuth(azimuth: Float = 10f)
    fun tiltAllAxes(pitch: Float = 10f, roll: Float = 10f, azimuth: Float = 10f)
}

class DefaultSensorRobot @Inject constructor() : SensorRobot {
    private val sensorManager: SensorManager =
        getApplicationContext<Context>().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val shadowSensorManager = shadowOf(sensorManager)

    private lateinit var mockAccelerometerSensor: Sensor
    private lateinit var mockMagneticFieldSensor: Sensor

    override fun setupMockSensors(sensorTypes: List<Int>) {
        sensorTypes.forEach { sensorType ->
            val sensor = ShadowSensor.newInstance(sensorType)
            shadowSensorManager.addSensor(sensor)
            when (sensorType) {
                Sensor.TYPE_ACCELEROMETER -> mockAccelerometerSensor = sensor
                Sensor.TYPE_MAGNETIC_FIELD -> mockMagneticFieldSensor = sensor
                else -> throw IllegalArgumentException("Unsupported sensor type: $sensorType")
            }
        }
    }

    override fun cleanUpSensors() {
        CustomShadowSensorManager.setCustomRotationMatrix(floatArrayOf())
        CustomShadowSensorManager.setCustomOrientationAngles(floatArrayOf())
    }

    override fun tiltPitch(pitch: Float) {
        sendTiltEvent(mockAccelerometerSensor, pitch = pitch)
        sendTiltEvent(mockMagneticFieldSensor, pitch = pitch)
    }

    override fun tiltRoll(roll: Float) {
        sendTiltEvent(mockAccelerometerSensor, roll = roll)
        sendTiltEvent(mockMagneticFieldSensor, roll = roll)
    }

    override fun tiltAzimuth(azimuth: Float) {
        sendTiltEvent(mockAccelerometerSensor, azimuth = azimuth)
        sendTiltEvent(mockMagneticFieldSensor, azimuth = azimuth)
    }

    override fun tiltAllAxes(pitch: Float, roll: Float, azimuth: Float) {
        sendTiltEvent(mockAccelerometerSensor, pitch, roll, azimuth)
        sendTiltEvent(mockMagneticFieldSensor, pitch, roll, azimuth)
    }

    private fun sendTiltEvent(
        sensor: Sensor?,
        pitch: Float = 0f,
        roll: Float = 0f,
        azimuth: Float = 0f,
    ) {
        if (sensor != null) {
            val event = createTiltEvent(sensor, pitch, roll, azimuth)
            CustomShadowSensorManager.setCustomRotationMatrix(
                FloatArray(9).apply {
                    SensorManager.getRotationMatrix(
                        this,
                        null,
                        floatArrayOf(pitch, roll, azimuth),
                        floatArrayOf(0f, 0f, 0f),
                    )
                },
            )
            CustomShadowSensorManager.setCustomOrientationAngles(floatArrayOf(azimuth, pitch, roll))
            shadowSensorManager.sendSensorEventToListeners(event)
        }
    }

    private fun createTiltEvent(
        sensor: Sensor,
        pitch: Float,
        roll: Float,
        azimuth: Float,
    ): SensorEvent {
        return SensorEventBuilder.newBuilder()
            .setSensor(sensor)
            .setTimestamp(System.currentTimeMillis())
            .setValues(floatArrayOf(pitch, roll, azimuth))
            .build()
    }

    @Implements(SensorManager::class)
    class CustomShadowSensorManager : ShadowSensorManager() {

        @Suppress("UNUSED_PARAMETER")
        companion object {
            private var customRotationMatrix: FloatArray? = null
            private var customOrientationAngles: FloatArray? = null

            fun setCustomRotationMatrix(rotationMatrix: FloatArray) {
                customRotationMatrix = rotationMatrix
            }

            @Implementation
            @JvmStatic
            fun getRotationMatrix(
                r: FloatArray?,
                i: FloatArray?,
                gravity: FloatArray?,
                geomagnetic: FloatArray?,
            ): Boolean {
                customRotationMatrix?.let {
                    if (r != null && it.size == r.size) {
                        System.arraycopy(it, 0, r, 0, it.size)
                    }
                    return true
                }
                return false
            }

            fun setCustomOrientationAngles(orientationAngles: FloatArray) {
                customOrientationAngles = orientationAngles
            }

            @Implementation
            @JvmStatic
            fun getOrientation(r: FloatArray?, values: FloatArray?): FloatArray {
                customOrientationAngles?.let {
                    if (values != null && it.size == values.size) {
                        System.arraycopy(it, 0, values, 0, it.size)
                    }
                    return it
                }
                return r!!
            }
        }
    }
}

interface TimetableServerRobot {
    enum class ServerStatus {
        Operational,
        OperationalBothAssetAvailable,
        OperationalOnlySlideAssetAvailable,
        OperationalOnlyVideoAssetAvailable,
        OperationalMessageExists,
        Error,
    }

    fun setupTimetableServer(serverStatus: ServerStatus)
}

class DefaultTimetableServerRobot @Inject constructor(sessionsApiClient: SessionsApiClient) :
    TimetableServerRobot {
    private val fakeSessionsApiClient = sessionsApiClient as FakeSessionsApiClient
    override fun setupTimetableServer(serverStatus: TimetableServerRobot.ServerStatus) {
        fakeSessionsApiClient.setup(
            when (serverStatus) {
                Operational -> Status.Operational
                OperationalBothAssetAvailable -> Status.OperationalBothAssetAvailable
                OperationalOnlySlideAssetAvailable -> Status.OperationalOnlySlideAssetAvailable
                OperationalOnlyVideoAssetAvailable -> Status.OperationalOnlyVideoAssetAvailable
                OperationalMessageExists -> Status.OperationalMessageExists
                Error -> Status.Error
            },
        )
    }
}

interface ContributorsServerRobot {
    enum class ServerStatus {
        Operational,
        Error,
    }

    fun setupContributorServer(serverStatus: ServerStatus)
}

class DefaultContributorsServerRobot @Inject constructor(contributorsApiClient: ContributorsApiClient) :
    ContributorsServerRobot {
    private val fakeContributorsApiClient = contributorsApiClient as FakeContributorsApiClient
    override fun setupContributorServer(serverStatus: ContributorsServerRobot.ServerStatus) {
        fakeContributorsApiClient.setup(
            when (serverStatus) {
                ContributorsServerRobot.ServerStatus.Operational -> FakeContributorsApiClient.Status.Operational
                ContributorsServerRobot.ServerStatus.Error -> FakeContributorsApiClient.Status.Error
            },
        )
    }
}

interface EventMapServerRobot {
    enum class ServerStatus {
        Operational,
        Error,
    }

    fun setupEventMapServer(serverStatus: ServerStatus)
}

class DefaultEventMapServerRobot @Inject constructor(contributorsApiClient: EventMapApiClient) :
    EventMapServerRobot {
    private val fakeEventMapApiClient = contributorsApiClient as FakeEventMapApiClient
    override fun setupEventMapServer(serverStatus: EventMapServerRobot.ServerStatus) {
        fakeEventMapApiClient.setup(
            when (serverStatus) {
                EventMapServerRobot.ServerStatus.Operational -> FakeEventMapApiClient.Status.Operational
                EventMapServerRobot.ServerStatus.Error -> FakeEventMapApiClient.Status.Error
            },
        )
    }
}

interface StaffServerRobot {
    enum class ServerStatus {
        Operational,
        Error,
    }

    fun setupStaffServer(serverStatus: ServerStatus)
}

class DefaultStaffServerRobot @Inject constructor(staffApiClient: StaffApiClient) :
    StaffServerRobot {
    private val fakeStaffApiClient = staffApiClient as FakeStaffApiClient
    override fun setupStaffServer(serverStatus: StaffServerRobot.ServerStatus) {
        fakeStaffApiClient.setup(
            when (serverStatus) {
                StaffServerRobot.ServerStatus.Operational -> FakeStaffApiClient.Status.Operational
                StaffServerRobot.ServerStatus.Error -> FakeStaffApiClient.Status.Error
            },
        )
    }
}

interface SponsorsServerRobot {
    enum class ServerStatus {
        Operational,
        Error,
    }

    fun setupSponsorsServer(sererStatus: ServerStatus)
}

class DefaultSponsorsServerRobot @Inject constructor(sponsorsApiClient: SponsorsApiClient) :
    SponsorsServerRobot {
    private val fakeSponsorsApiClient = sponsorsApiClient as FakeSponsorsApiClient
    override fun setupSponsorsServer(sererStatus: ServerStatus) {
        fakeSponsorsApiClient.setup(
            when (sererStatus) {
                ServerStatus.Operational -> FakeSponsorsApiClient.Status.Operational
                ServerStatus.Error -> FakeSponsorsApiClient.Status.Error
            },
        )
    }
}

interface ProfileCardDataStoreRobot {
    enum class ProfileCardInputStatus {
        NoInputOtherThanImage,
        AllNotEntered,
    }

    suspend fun setupSavedProfileCard(profileCardInputStatus: ProfileCardInputStatus)
}

class DefaultProfileCardDataStoreRobot @Inject constructor(
    private val profileCardDataStore: ProfileCardDataStore,
) : ProfileCardDataStoreRobot {
    override suspend fun setupSavedProfileCard(profileCardInputStatus: ProfileCardInputStatus) {
        when (profileCardInputStatus) {
            NoInputOtherThanImage -> {
                profileCardDataStore.save(
                    ProfileCard.Exists.fake().copy(
                        nickname = "",
                        occupation = "",
                        link = "",
                    ),
                )
            }

            AllNotEntered -> {
                // Do nothing
            }
        }
    }
}

interface SettingsDataStoreRobot {
    enum class SettingsStatus {
        UseDotGothic16FontFamily,
        UseSystemDefaultFont,
    }

    suspend fun setupSettings(settingsStatus: SettingsStatus)
    fun get(): Flow<Settings>
}

class DefaultSettingsDataStoreRobot @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
) : SettingsDataStoreRobot {
    override suspend fun setupSettings(settingsStatus: SettingsStatus) {
        when (settingsStatus) {
            UseDotGothic16FontFamily -> {
                settingsDataStore.save(
                    Settings.Exists(
                        useFontFamily = FontFamily.DotGothic16Regular,
                    ),
                )
            }

            UseSystemDefaultFont -> {
                settingsDataStore.save(
                    Settings.Exists(
                        useFontFamily = FontFamily.SystemDefault,
                    ),
                )
            }
        }
    }

    override fun get(): Flow<Settings> = settingsDataStore.get()
}

interface TimetableItemCardRobot {
    enum class Language(
        val tagName: String,
    ) {
        MIXED("MIXED"),
        JAPANESE("JA"),
        ENGLISH("EN"),
    }

    fun checkTimetableListItemByLanguage(language: Language)
}

class DefaultTimetableItemCardRobot @Inject constructor(
    private val robotTestRule: RobotTestRule,
) : TimetableItemCardRobot {
    override fun checkTimetableListItemByLanguage(language: Language) {
        val doesNotContains = Language.entries.filterNot { it == language }

        robotTestRule.composeTestRule
            .onAllNodes(hasTestTag(TimetableItemCardTestTag))
            .onFirst()
            .assertSemanticsProperty(SemanticsProperties.TimetableItemCard) { item ->
                item?.language?.toLang()?.tagName == language.tagName
            }

        doesNotContains.forEach { doesNotContain ->
            robotTestRule.composeTestRule
                .onAllNodes(hasTestTag(TimetableItemCardTestTag))
                .onFirst()
                .assertSemanticsProperty(SemanticsProperties.TimetableItemCard) { item ->
                    item?.language?.toLang()?.tagName != doesNotContain.tagName
                }
        }
    }
}
