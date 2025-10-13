package com.sopt.clody

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.ab180.airbridge.Airbridge

class ClodyDeeplinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        val isFirstCalled = Airbridge.handleDeferredDeeplink { uri ->
            // when handleDeferredDeeplink is called firstly after install
            if (uri != null) {
                // show proper content using uri (YOUR_SCHEME://...)
            }
        }
    }
}
