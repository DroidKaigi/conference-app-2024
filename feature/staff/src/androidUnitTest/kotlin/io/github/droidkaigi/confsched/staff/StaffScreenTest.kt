package io.github.droidkaigi.confsched.about

import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched.testing.DescribedBehavior
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner

@RunWith(ParameterizedRobolectricTestRunner::class)
@HiltAndroidTest
class StaffScreenTest(
    private val testCase: DescribedBehavior<StaffScreenRobot>,
) {
}
