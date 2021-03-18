package com.williamfzc.randunit.env

import com.williamfzc.randunit.models.StatementModel
import java.lang.reflect.Method
import java.util.logging.Logger

// runnable statement, generated from statement model
// which contains some real values
data class Statement(
    val method: Method,
    val caller: Any,
    val parameters: Collection<Any?>,
    val model: StatementModel
) {
    companion object {
        private val logger = Logger.getGlobal()
    }

    fun invoke(): Any? {
        method.isAccessible = true
        return method.invoke(caller, *parameters.toTypedArray())
    }
}
