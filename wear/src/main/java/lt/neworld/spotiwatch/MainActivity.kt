package lt.neworld.spotiwatch

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.android.synthetic.main.activity_main.*
import lt.neworld.spotiwatch.shared.MESSAGE_PATH_NEXT

class MainActivity : WearableActivity(), GoogleApiClient.ConnectionCallbacks {
    private lateinit var googleApiClient: GoogleApiClient
    private var nodes: List<Node> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAmbientEnabled()
        button_next.setOnClickListener() { sendMessage(MESSAGE_PATH_NEXT) }
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
        button_next.setImageResource(R.drawable.ic_play_arrow_120dp_stroke)
    }

    override fun onExitAmbient() {
        super.onExitAmbient()
        button_next.setImageResource(R.drawable.ic_play_arrow_120dp_fill)
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
