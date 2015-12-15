package lt.neworld.spotiwatch

import android.widget.Toast
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import lt.neworld.spotiwatch.shared.MESSAGE_PATH_NEXT

/**
 * @author Andrius Semionovas
 * @since 2015-12-15
 */

class WearListenerService : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        when (messageEvent.path) {
            MESSAGE_PATH_NEXT -> next()
        }
    }

    private fun next() {
        Toast.makeText(applicationContext, "Play next song", Toast.LENGTH_LONG).show()
    }
}
