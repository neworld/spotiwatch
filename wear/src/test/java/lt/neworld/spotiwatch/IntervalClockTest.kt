package lt.neworld.spotiwatch

import android.os.SystemClock
import com.nhaarman.mockito_kotlin.*
import lt.neworld.spotiwatch.test.TestFunction0
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.shadows.ShadowLooper
import org.robolectric.shadows.ShadowSystemClock

/**
 * @author Andrius Semionovas
 * @since 2016-03-27
 */
@RunWith(RobolectricGradleTestRunner::class)
class IntervalClockTest {

    val interval = 5L
    val onTick: TestFunction0<Unit> = mock()

    val fixture: IntervalClock = IntervalClock(interval, onTick)

    @Test
    fun testResume_immediateTick() {
        fixture.resume()

        verify(onTick).invoke()
    }

    @Test
    fun testResume_rewindLooper5Seconds_tickSecondTime() {
        fixture.resume()
        reset(onTick)

        ShadowLooper.idleMainLooper(interval)

        verify(onTick).invoke()
    }

    @Test
    fun testResume_rewindLooperDoubleInterval_doubleTick() {
        fixture.resume()
        reset(onTick)

        ShadowLooper.idleMainLooper(interval * 2)

        verify(onTick, times(2)).invoke()
    }

    @Test
    fun testResume_resumeTwoTimes_tick() {
        fixture.resume()
        fixture.resume()

        verify(onTick).invoke()
    }

    @Test
    fun testResume_resumeAfterPause_doubleTick() {
        fixture.resume()
        fixture.pause()
        fixture.resume()

        verify(onTick, times(2)).invoke()
    }

    @Test
    fun testPause_rewindLooper_noTick() {
        fixture.resume()
        fixture.pause()
        reset(onTick)

        ShadowLooper.idleMainLooper(interval)

        verify(onTick, never()).invoke()
    }

    @Test
    fun testPause_rewindLooperToNextDivisorInterval_tick() {
        val currentTime = SystemClock.currentThreadTimeMillis()
        val delay = currentTime + currentTime % interval + interval - 2
        ShadowSystemClock.setCurrentTimeMillis(delay)
        fixture.resume()
        reset(onTick)

        ShadowLooper.idleMainLooper(2)

        verify(onTick).invoke()
    }
}