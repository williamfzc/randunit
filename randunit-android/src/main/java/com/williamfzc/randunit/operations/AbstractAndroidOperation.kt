/*
 * Copyright 2021 williamfzc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.williamfzc.randunit.operations

import android.app.Activity
import android.app.Service
import android.content.ContentProvider
import androidx.fragment.app.Fragment
import java.lang.reflect.Method
import java.util.logging.Logger

abstract class AbstractAndroidOperation(t: Class<*>) : AbstractOperation(t) {
    override fun canMock(): Boolean = false

    companion object {
        private val logger = Logger.getGlobal()

        fun of(t: Class<*>): AbstractAndroidOperation {
            // classify
            val op = when {
                Activity::class.java.isAssignableFrom(t) -> ActivityOperation(t)
                Service::class.java.isAssignableFrom(t) -> ServiceOperation(t)
                ContentProvider::class.java.isAssignableFrom(t) -> ContentProviderOperation(t)
                Fragment::class.java.isAssignableFrom(t) -> FragmentOperation(t)
                else -> OtherAndroidOperation(t)
            }
            logger.info("new op: $op, type is: $t")
            return op
        }
    }

    override fun canInvoke(method: Method): Boolean {
//        return !method.name.startsWith("on")
        return true
    }
}
