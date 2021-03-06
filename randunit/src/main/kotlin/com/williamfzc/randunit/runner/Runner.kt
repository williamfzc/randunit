package com.williamfzc.randunit.runner

import com.williamfzc.randunit.helper.MethodHelper
import com.williamfzc.randunit.models.MethodModel
import com.williamfzc.randunit.operations.AbstractOperation
import com.williamfzc.randunit.operations.OperationManager
import java.lang.Exception
import java.util.logging.Logger

class Runner {
    companion object {
        private val logger = Logger.getLogger("Runner")
    }

    private fun run(operation: AbstractOperation, operationManager: OperationManager) {
        if (!operation.isValid()) {
            logger.info("operation ${operation.type} is invalid, skipped")
            return
        }

        methodLoop@ for (eachMethod in operation.type.declaredMethods) {
            MethodHelper.forceMethodAccessible(eachMethod)
            val model = MethodModel(operation, eachMethod)

            // append some rel types
            for (eachRelType in model.getRelativeTypes())
                operationManager.addClazz(eachRelType)

            for (i in 1..10) {
                val stat = model.generateStatement()
                stat?.run {
                    try{
                        logger.info("invoking: $eachMethod")
                        exec()
                    } catch (e: Exception) {
                        logger.warning("invoke failed: $e")
                    }
                }
            }
        }
    }

    fun run(operationManager: OperationManager) {
        var op = operationManager.poll()
        while (null != op) {
            run(op, operationManager)
            op = operationManager.poll()
        }
        logger.info("run finished")
    }
}