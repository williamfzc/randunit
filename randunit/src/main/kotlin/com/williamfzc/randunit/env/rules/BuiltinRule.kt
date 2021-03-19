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
package com.williamfzc.randunit.env.rules

import com.williamfzc.randunit.env.Statement
import com.williamfzc.randunit.extensions.firstTraceContains
import com.williamfzc.randunit.extensions.msgContains
import io.mockk.MockKException
import org.mockito.exceptions.base.MockitoException

open class BuiltinRule : AbstractRule() {
    // NOTICE:
    // by default, randunit ignores some errors for stability
    private val buildInIgnoreExceptions = mutableSetOf(
        // mock errors
        MockKException::class.java,
        MockitoException::class.java,
        // java inner
        VerifyError::class.java,
        // TODO: improvement in the future
        IllegalAccessException::class.java,
        IllegalStateException::class.java
    )

    // ignore current exception if these words appeared in traceback msg
    private val ignoredExceptionWords = mutableSetOf(
        "org.mockito",
        ".mockk",
        "MockitoMock",
        "com.williamfzc.randunit"
    )

    override fun judge(statement: Statement, e: Throwable): Boolean {
        for (eachIgnoreException in buildInIgnoreExceptions) {
            if (e::class.java == eachIgnoreException)
                return true
        }

        for (eachStopWord in ignoredExceptionWords) {
            // and msg check
            if (e.msgContains(eachStopWord))
                return true

            // first line of stacktrace
            if (e.firstTraceContains(eachStopWord))
                return true
        }
        // ignore inner classes and methods
        // TODO: not ready currently, i have to ignore them
        e.stackTrace.firstOrNull()?.let {
            // directly caused by inner classes
            if (it.className.contains("$") || it.methodName.contains("$"))
                return true
        }

        return false
    }
}
