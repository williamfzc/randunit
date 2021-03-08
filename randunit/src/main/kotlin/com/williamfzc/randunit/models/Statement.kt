package com.williamfzc.randunit.models

import com.williamfzc.randunit.extensions.isStatic
import com.williamfzc.randunit.operations.AbstractOperation
import java.lang.Exception
import java.lang.reflect.Method
import java.util.logging.Logger

class Statement(
    // a `method call` may looks like:
    // methodA(caller, param1, param2)
    private val method: Method,
    private val callerOperation: AbstractOperation,
    private val parametersTypes: List<Class<*>>,

    // others
    private val mockModel: MockModel
) {
    companion object {
        private val logger = Logger.getLogger("Statement")
    }

    // this method should run inside test case
    fun exec() {
        logger.info("[caller ${callerOperation.type}] invoking [method $method] with [params $parametersTypes]")

        val caller = generateCaller()
        if (null == caller) {
            logger.warning("generate caller failed, skipped")
            return
        }

        val parameters = generateParameters()
        if (parameters.size != parametersTypes.size) {
            logger.warning("parameters mock failed, skipped")
            return
        }

        // ok, invoke
        method.invoke(caller, *parameters.toTypedArray())
    }

    fun getName(): String {
        return "statement_${method}_with_$parametersTypes"
    }

    private fun generateCaller(): Any? {
        try {
            if (method.isStatic())
                return callerOperation.type
            return callerOperation.getInstance()
        } catch (e: Exception) {
            // failed to create a real caller
            // use the mock one
            logger.warning("generate caller $callerOperation failed, use the mock one")
            e.printStackTrace()
            return mockModel.mock(callerOperation.type)
        }
    }

    private fun generateParameters(): List<Any?> {
        val ret = mutableListOf<Any?>()
        for (each in parametersTypes) {
            mockModel.mock(each)?.let {
                ret.add(it)
            }
        }
        return ret
    }
}
