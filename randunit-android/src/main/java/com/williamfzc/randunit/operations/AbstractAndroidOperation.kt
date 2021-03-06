package com.williamfzc.randunit.operations

import android.app.Activity

abstract class AbstractAndroidOperation : AbstractOperation() {
    companion object {
        fun of(t: Class<*>): AbstractOperation {
            // classify
            val op = when {
                Activity::class.java.isAssignableFrom(t) -> ActivityOperation()
                else -> NormalOperation()
            }

            op.type = t
            return op
        }
    }
}
