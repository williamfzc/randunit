package com.williamfzc.randunit.operations

import android.app.Activity
import android.app.Service
import android.content.ContentProvider
import android.util.Log

abstract class AbstractAndroidOperation : AbstractOperation() {
    companion object {
        private const val TAG = "AndroidOperation"

        fun of(t: Class<*>): AbstractAndroidOperation {
            // classify
            val op = when {
                Activity::class.java.isAssignableFrom(t) -> ActivityOperation()
                Service::class.java.isAssignableFrom(t) -> ServiceOperation()
                ContentProvider::class.java.isAssignableFrom(t) -> ContentProviderOperation()
                else -> OtherAndroidOperation()
            }
            Log.d(TAG, "new op: $op")

            op.type = t
            return op
        }
    }
}
