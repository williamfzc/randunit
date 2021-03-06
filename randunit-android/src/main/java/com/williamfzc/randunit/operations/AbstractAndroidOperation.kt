package com.williamfzc.randunit.operations

import android.app.Activity
import android.app.Service
import android.content.ContentProvider

abstract class AbstractAndroidOperation : AbstractOperation() {
    companion object {
        fun of(t: Class<*>): AbstractAndroidOperation {
            // classify
            val op = when {
                Activity::class.java.isAssignableFrom(t) -> ActivityOperation()
                Service::class.java.isAssignableFrom(t) -> ServiceOperation()
                ContentProvider::class.java.isAssignableFrom(t) -> ContentProviderOperation()
                else -> OtherAndroidOperation()
            }

            op.type = t
            return op
        }
    }
}
