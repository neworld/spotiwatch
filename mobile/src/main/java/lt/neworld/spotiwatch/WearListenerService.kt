package lt.neworld.spotiwatch

import android.content.Intent
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import lt.neworld.spotiwatch.shared.MESSAGE_PATH_NEXT
import lt.neworld.spotiwatch.shared.MESSAGE_PATH_PREV

/**
 * @author Andrius Semionovas
 * @since 2015-12-15
 */

class WearListenerService : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        when (messageEvent.path) {
            MESSAGE_PATH_NEXT -> sendIntent(SPOTIFY_NEXT)
            MESSAGE_PATH_PREV -> sendIntent(SPOTIFY_PREV)
        }
    }

    private fun sendIntent(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    companion object {
        private val SPOTIFY_NEXT = "com.spotify.mobile.android.ui.widget.NEXT"
        private val SPOTIFY_PREV = "com.spotify.mobile.android.ui.widget.PREVIOUS"
    }
}
