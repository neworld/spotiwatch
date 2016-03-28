package lt.neworld.spotiwatch

import android.os.Handler
import android.os.SystemClock

/**
 * @author Andrius Semionovas
 * @since 2016-03-27
 */

/**
 * @param interval in ms
 */
class IntervalClock(val interval: Long, private val onTick: () -> Unit) {

    private val handler = Handler()
    private val trigger = Runnable { enqueueUpdater(); onTick() }
    private var resumed = false

    fun resume() {
        if (resumed) {
            return
        }
        resumed = true
        onTick.invoke()
        enqueueUpdater()
    }

    fun pause() {
        resumed = false
        clearQueue()
    }

    private fun enqueueUpdater() {
        val delay = interval - SystemClock.currentThreadTimeMillis() % interval
        handler.postDelayed(trigger, delay)
    }

    private fun clearQueue() {
        handler.removeCallbacks(trigger)
    }

}