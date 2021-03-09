package com.williamfzc.randunit.env

import com.williamfzc.randunit.extensions.isStatic
import com.williamfzc.randunit.models.StatementModel
import java.lang.Exception
import java.util.logging.Logger

class NormalTestEnv(envConfig: EnvConfig = EnvConfig()) : AbstractTestEnv(envConfig) {
    companion object {
        private val logger = Logger.getLogger("StandardTestEnv")
    }

    private fun generateCaller(statementModel: StatementModel): Any? {
        val callOperation = statementModel.callerOperation
        try {
            if (statementModel.method.isStatic())
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

    private fun generateParameters(statementModel: StatementModel): List<Any?> {
        val ret = mutableListOf<Any?>()
        for (each in statementModel.parametersTypes) {
            mockModel.mock(each)?.let {
                ret.add(it)
            }
        }
        return ret
    }

    override fun run(statementModel: StatementModel) {
        logger.info("running: ${statementModel.getDesc()}")

        val caller = generateCaller(statementModel)
        caller ?: run {
            logger.warning("generate caller failed, skipped")
            return
        }

        val parameters = generateParameters(statementModel)
        if (parameters.size != statementModel.parametersTypes.size) {
            logger.warning("parameters mock failed, skipped")
            return
        }

        statementModel.method.invoke(caller, *parameters.toTypedArray())
    }


}
