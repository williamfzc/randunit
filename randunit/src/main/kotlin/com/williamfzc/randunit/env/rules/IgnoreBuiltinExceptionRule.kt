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
