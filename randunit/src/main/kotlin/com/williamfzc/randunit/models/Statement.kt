package com.williamfzc.randunit.models

import java.lang.reflect.Method
import java.util.logging.Logger

class Statement(
    private val method: Method,
    private val caller: Any,
    private val parameters: List<Any?>
) {
    companion object {
        private val logger = Logger.getLogger("Statement")
    }

    // todo: statement is a data model
    // should contain types only (and how to get their instances
    // values will be generated when exec() called
    fun exec() {
        logger.info("$caller invoking method $method with $parameters")
        method.invoke(caller, *parameters.toTypedArray())
    }

    fun getName(): String {
        return "statement_${caller}_${method}_with_$parameters"
    }
}
