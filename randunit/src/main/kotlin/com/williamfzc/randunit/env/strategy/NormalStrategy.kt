package com.williamfzc.randunit.env.strategy

import com.williamfzc.randunit.env.Statement
import com.williamfzc.randunit.extensions.isStatic
import com.williamfzc.randunit.models.MockModel
import com.williamfzc.randunit.models.StatementModel
import java.lang.Exception
import java.util.logging.Logger

object NormalStrategy : AbstractStrategy() {
    private val logger = Logger.getGlobal()

    override fun genStatements(statementModel: StatementModel, mockModel: MockModel): Iterable<Statement> {
        // at least it will not cause any crashes because of mock
        var caller = generateCaller(statementModel)
        caller ?: run {
            // use the mock one
            val op = statementModel.callerOperation
            logger.warning("generate caller $op failed, use the mock one")
            caller = mockModel.mock(op.type)
        }

        if (!verifyCallerInst(caller))
            return setOf()
        // caller is ready

        val parameters = generateParameters(statementModel)
        if (parameters.size != statementModel.parametersTypes.size) {
            logger.warning("parameters mock failed, skipped")
            return setOf()
        }

        // by default, gen only one statement
        return setOf(Statement(statementModel.method, caller!!, parameters, statementModel))
    }

    private fun generateCaller(statementModel: StatementModel): Any? {
        val callOperation = statementModel.callerOperation
        try {
            if (statementModel.method.isStatic())
                return callOperation.type
            return callOperation.getInstanceSafely()
        } catch (e: Exception) {
            // failed to create a real caller
            e.printStackTrace()
            return null
        }
    }

    private fun generateParameters(statementModel: StatementModel): List<Any?> {
        val ret = mutableListOf<Any?>()
        for (each in statementModel.parametersTypes) {
            mockModel.mock(each)?.let {
                ret.add(it)
            } ?: run {
                logger.warning("mock $each failed finally")
            }
        }
        return ret
    }

    private fun verifyCallerInst(caller: Any?): Boolean {
        caller ?: run {
            logger.warning("generate caller failed, skipped")
            return false
        }
        // base types (kt issues ...
        if (caller is Unit)
            return false
        return true
    }
}
