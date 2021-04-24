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

package com.williamfzc.randunit.env.rules

import com.williamfzc.randunit.env.Statement
import java.util.logging.Logger

interface RuleHelper {
    companion object {
        private val logger = Logger.getGlobal()
    }

    // offer some helper functions for doing some judgements
    // mainly comes from ExceptionExt
    // which it's hard to use for java users directly
    private fun err2Trace(e: Throwable): Array<out StackTraceElement>? {
        e.stackTrace.let {
            if (it.isNotEmpty())
                return it
        }
        logger.warning("trace is empty: $e")
        return null
    }

    fun isHappenedInClass(e: Throwable, className: String): Boolean {
        err2Trace(e)?.forEach {
            // its classname is full path format
            if (it.className.endsWith(className)) {
                logger.info("class name $className match: happened in $it")
                return true
            }
        }
        // not match
        return false
    }

    fun isHappenedInPackage(e: Throwable, packageName: String): Boolean {
        err2Trace(e)?.forEach {
            // its classname is full path format
            if (it.className.startsWith(packageName)) {
                logger.info("package $packageName match: happened in $it")
                return true
            }
        }
        // not match
        return false
    }
}

// rule was designed for judging exceptions
// return true to mark this err invalid, and it will not be reported
abstract class AbstractRule : RuleHelper {
    abstract fun judge(statement: Statement, e: Throwable): Boolean
}
