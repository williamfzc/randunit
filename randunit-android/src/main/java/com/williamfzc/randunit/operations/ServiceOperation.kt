package com.williamfzc.randunit.operations

import android.app.Service
import org.robolectric.Robolectric

class ServiceOperation : AbstractAndroidOperation() {
    override fun getInstance(): Service {
        @Suppress("UNCHECKED_CAST")
        return Robolectric.buildService(type as Class<Service>).get()
    }
}
