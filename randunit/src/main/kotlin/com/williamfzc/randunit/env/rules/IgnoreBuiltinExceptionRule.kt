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

import com.williamfzc.randunit.models.StatementModel
import io.mockk.MockKException
import org.mockito.exceptions.base.MockitoException

object IgnoreBuiltinExceptionRule : AbstractRule() {
    private val BUILTIN_IGNORED_EXCEPTIONS = setOf(
        MockKException::class.java,
        MockitoException::class.java,
        VerifyError::class.java
    )

    // ignore current exception if these words appeared in traceback msg
    private val IGNORED_EXCEPTIONS_WORDS = setOf(
        "org.mockito",
        ".mockk",
        "MockitoMock",
        "com.williamfzc.randunit"
    )

    override fun judge(statementModel: StatementModel, e: Throwable): Boolean {
        for (eachIgnoreException in BUILTIN_IGNORED_EXCEPTIONS) {
            if (e::class.java == eachIgnoreException)
                return true
        }
        // and msg check
        e.message?.let { errMsg ->
            for (eachStopWord in IGNORED_EXCEPTIONS_WORDS) {
                if (errMsg.contains(eachStopWord))
                    return true
            }
        }
        // first line of stacktrace
        e.stackTrace.firstOrNull()?.let {
            for (eachStopWord in IGNORED_EXCEPTIONS_WORDS) {
                if (it.toString().contains(eachStopWord))
                    return true
            }
        }

        return false
    }
}
