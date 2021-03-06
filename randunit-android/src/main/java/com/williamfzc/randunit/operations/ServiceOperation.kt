package com.williamfzc.randunit.operations

import android.app.Service
import org.robolectric.Robolectric

class ServiceOperation : AbstractAndroidOperation() {
    override fun getInstance(): Service {
        return Robolectric.setupService(type as Class<Service>)
    }
}
