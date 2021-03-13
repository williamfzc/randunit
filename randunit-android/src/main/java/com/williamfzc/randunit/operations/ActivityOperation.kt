package com.williamfzc.randunit.operations

import android.app.Activity
import org.robolectric.Robolectric

class ActivityOperation : AbstractAndroidOperation() {
    override fun getInstance(): Activity {
        @Suppress("UNCHECKED_CAST")
        return Robolectric.buildActivity(type as Class<Activity>).create().get()
    }
}
