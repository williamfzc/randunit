/**
 * Copyright 2021 williamfzc
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.williamfzc.randunit.env

import com.williamfzc.randunit.exceptions.RUVerifyException
import com.williamfzc.randunit.models.StatementModel
import java.lang.reflect.Method
import java.util.logging.Logger

// runnable statement, generated from statement model
// which contains some real values
data class Statement(
    val method: Method,
    val caller: Any,
    val parameters: Collection<Any?>,
    val model: StatementModel,
    val verifyResult: (Statement) -> String?
) {
    companion object {
        private val logger = Logger.getGlobal()
    }

    var executedResult: Any? = null
    var throwExc: Throwable? = null

    fun invoke(): Any? {
        try {
            method.isAccessible = true
            executedResult = method.invoke(caller, *parameters.toTypedArray())
        } catch (e: Throwable) {
            throwExc = e
        }
        return executedResult
    }

    fun isErrorHappened() = (null != throwExc)

    fun verifyResult() = verifyResult(this)?.let { throw RUVerifyException(it) }
}
