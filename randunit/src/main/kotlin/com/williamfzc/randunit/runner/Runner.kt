package com.williamfzc.randunit.runner

import com.williamfzc.randunit.helper.MethodHelper
import com.williamfzc.randunit.models.MethodModel
import com.williamfzc.randunit.models.MockModel
import com.williamfzc.randunit.operations.AbstractOperation
import com.williamfzc.randunit.operations.OperationManager
import java.lang.Exception
import java.lang.reflect.Method
import java.util.logging.Logger

class Runner(private val cfg: RunnerConfig = RunnerConfig()) {
    companion object {
        private val logger = Logger.getLogger("Runner")
    }

    var mockModel: MockModel = MockModel(cfg.mockParameters)

    private fun run(operation: AbstractOperation, operationManager: OperationManager) {
        if (!operation.isValid()) {
            logger.info("operation ${operation.type} is invalid, skipped")
            return
        }

        for (eachMethod in operation.type.declaredMethods) {
            runMethod(eachMethod, operation, operationManager)
        }
    }

    private fun runMethod(
        method: Method,
        operation: AbstractOperation,
        operationManager: OperationManager
    ) {
        MethodHelper.forceMethodAccessible(method)
        val model = MethodModel(operation, method, mockModel)

        // append some rel types
        for (eachRelType in model.getRelativeTypes())
            operationManager.addClazz(eachRelType)

        for (i in 1..cfg.batchSize) {
            val stat = model.generateStatement()
            stat?.run {
                try {
                    logger.info("invoking: $method")
                    exec()
                } catch (e: Exception) {
                    logger.warning("invoke failed: $e")
                }
            }
        }
    }

    fun runAll(operationManager: OperationManager) {
        var op = operationManager.poll()
        while (null != op) {
            run(op, operationManager)
            op = operationManager.poll()
        }
        logger.info("run finished")
    }
}