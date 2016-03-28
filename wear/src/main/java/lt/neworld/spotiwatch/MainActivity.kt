package lt.neworld.spotiwatch

import android.graphics.Color
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.text.format.DateFormat
import android.util.Log
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.android.synthetic.main.activity_main.*
import lt.neworld.spotiwatch.shared.MESSAGE_PATH_NEXT
import lt.neworld.spotiwatch.shared.MESSAGE_PATH_PREV
import java.util.*

class MainActivity : WearableActivity(), GoogleApiClient.ConnectionCallbacks {
    private lateinit var googleApiClient: GoogleApiClient
    private var nodes: List<Node> = listOf()
    private val intervalClock = IntervalClock(60 * 1000, { updateTime() })
    private val timeFormat by lazy {
        DateFormat.getTimeFormat(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAmbientEnabled()
        button_next.setOnClickListener { sendMessage(MESSAGE_PATH_NEXT) }
        button_prev.setOnClickListener { sendMessage(MESSAGE_PATH_PREV) }
        connectGoogleApiClient()
    }

    override fun onStart() {
        super.onStart()
        googleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()
        googleApiClient.disconnect()
    }

    override fun onResume() {
        super.onResume()

        intervalClock.resume()
    }

    override fun onPause() {
        super.onPause()

        intervalClock.pause()
    }

    private fun connectGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build()
    }

    override fun onConnected(bundle: Bundle?) {
        Wearable.NodeApi.getConnectedNodes(googleApiClient).setResultCallback {
            nodes = it.nodes
        }
    }

    override fun onEnterAmbient(ambientDetails: Bundle?) {
        super.onEnterAmbient(ambientDetails)
        button_next.setImageResource(R.drawable.ic_skip_next_stroke)
        button_prev.setImageResource(R.drawable.ic_skip_previous_stroke)
        time.paint.isAntiAlias = false
        time.setTextColor(Color.GRAY)
        intervalClock.pause()
    }

    override fun onExitAmbient() {
        super.onExitAmbient()
        button_next.setImageResource(R.drawable.ic_skip_next_fill)
        button_prev.setImageResource(R.drawable.ic_skip_previous_fill)
        time.paint.isAntiAlias = true
        time.setTextColor(Color.WHITE)
        intervalClock.resume()
    }

    override fun onUpdateAmbient() {
        super.onUpdateAmbient()
        updateTime()
    }

    private fun updateTime() {
        val current = Calendar.getInstance()
        time.text = timeFormat.format(current.time)
    }

    override fun onConnectionSuspended(i: Int) {
    }

    private fun sendMessage(path: String) {
        nodes.forEach {
            Wearable.MessageApi.sendMessage(googleApiClient, it.id, path, null).setResultCallback {
                if (it.status.isSuccess) {
                    Log.i(TAG, "Success send message with path: $path")
                } else {
                    Log.e(TAG, "Failed send message with status code: ${it.status.statusCode}")
                }
            }
        }
    }

    companion object {
        const val TAG = "WearMainActivity"
    }
}
