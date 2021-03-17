package com.williamfzc.randunit.operations

import android.app.Activity
import org.robolectric.Robolectric

class ActivityOperation(t: Class<*>) : AbstractAndroidOperation(t) {
    override fun getInstance(): Activity {
        @Suppress("UNCHECKED_CAST")
        return Robolectric.buildActivity(type as Class<Activity>).create().get()
    }

    override fun tearDown(caller: Any) {
        (caller as Activity).finish()
    }
}
