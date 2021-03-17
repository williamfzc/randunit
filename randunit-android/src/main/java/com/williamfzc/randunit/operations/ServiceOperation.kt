package com.williamfzc.randunit.operations

import android.app.Service
import org.robolectric.Robolectric
import java.lang.reflect.Method

class ServiceOperation(t: Class<*>) : AbstractAndroidOperation(t) {
    override fun getInstance(): Service {
        @Suppress("UNCHECKED_CAST")
        return Robolectric.buildService(type as Class<Service>).create().get()
    }

    override fun canInvoke(method: Method): Boolean {
        return super.canInvoke(method).and(!method.name.contains("attachToBaseContext"))
    }

    override fun tearDown(caller: Any) {
        (caller as Service).stopSelf()
    }
}
