package com.williamfzc.randunit.env

import com.williamfzc.randunit.extensions.isStatic
import com.williamfzc.randunit.models.Statement
import java.lang.Exception
import java.util.logging.Logger

class SimpleTestEnv(envConfig: EnvConfig = EnvConfig()) : AbstractTestEnv(envConfig) {
    companion object {
        private val logger = Logger.getLogger("StandardTestEnv")
    }

    private fun generateCaller(statement: Statement): Any? {
        val callOperation = statement.callerOperation
        try {
            if (statement.method.isStatic())
                return callOperation.type
            return callOperation.getInstance()
        } catch (e: Exception) {
            // failed to create a real caller
            // use the mock one
            logger.warning("generate caller $callOperation failed, use the mock one")
            e.printStackTrace()
            return mockModel.mock(callOperation.type)
        }
    }

    private fun generateParameters(statement: Statement): List<Any?> {
        val ret = mutableListOf<Any?>()
        for (each in statement.parametersTypes) {
            mockModel.mock(each)?.let {
                ret.add(it)
            }
        }
        return ret
    }

    override fun run(statement: Statement) {
        logger.info(statement.getDesc())

        val caller = generateCaller(statement)
        if (null == caller) {
            logger.warning("generate caller failed, skipped")
            return
        }

        val parameters = generateParameters(statement)
        if (parameters.size != statement.parametersTypes.size) {
            logger.warning("parameters mock failed, skipped")
            return
        }

        statement.method.invoke(caller, *parameters.toTypedArray())
    }
}
