package com.williamfzc.randunit.operations

import android.app.Activity
import com.williamfzc.randunit.exceptions.RUInstanceException
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController

class ActivityOperation(t: Class<*>) : AbstractAndroidOperation(t) {
    private var activityController: ActivityController<Activity>? = null

    override fun getInstance(): Activity {
        @Suppress("UNCHECKED_CAST")
        activityController = Robolectric.buildActivity(type as Class<Activity>)

        // robolectric issue #2068
        activityController?.run {
            return this.create().start().resume().visible().get()
        }
        throw RUInstanceException("create activity failed")
    }

    override fun tearDown(caller: Any) {
        activityController?.run {
            (caller as ActivityController<*>).pause().stop().destroy()
            cleanInstanceCache()
        }
    }
}
