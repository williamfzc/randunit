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
    // todo: statement is a data
    fun exec() {
        logger.info("invoking method $method with $parameters")
        method.invoke(caller, *parameters.toTypedArray())
    }
}
