package com.williamfzc.randunit.operations

import android.app.Activity
import android.app.Service
import android.content.ContentProvider
import java.util.logging.Logger

abstract class AbstractAndroidOperation : AbstractOperation() {
    companion object {
        private val logger = Logger.getLogger("AndroidOperation")

        fun of(t: Class<*>): AbstractAndroidOperation {
            // classify
            val op = when {
                Activity::class.java.isAssignableFrom(t) -> ActivityOperation()
                Service::class.java.isAssignableFrom(t) -> ServiceOperation()
                ContentProvider::class.java.isAssignableFrom(t) -> ContentProviderOperation()
                else -> OtherAndroidOperation()
            }
            logger.info("new op: $op")

            op.type = t
            return op
        }
    }
}
