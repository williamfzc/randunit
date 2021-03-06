package com.williamfzc.randunit.operations

import android.app.Activity
import org.robolectric.Robolectric

class ActivityOperation : AbstractAndroidOperation() {
    override fun getInstance(): Activity {
        return Robolectric.setupActivity(type as Class<Activity>)
    }
}