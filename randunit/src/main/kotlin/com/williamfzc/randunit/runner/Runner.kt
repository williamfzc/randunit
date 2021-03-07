package com.williamfzc.randunit.runner

import com.williamfzc.randunit.helper.MethodHelper
import com.williamfzc.randunit.helper.TypeHelper
import com.williamfzc.randunit.models.MethodModel
import com.williamfzc.randunit.models.MockModel
import com.williamfzc.randunit.operations.AbstractOperation
import com.williamfzc.randunit.operations.OperationManager
import java.lang.Exception
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.logging.Logger

class Runner(private val cfg: RunnerConfig = RunnerConfig()) : RunnerHookLayer {
    companion object {
        private val logger = Logger.getLogger("Runner")
    }

    var mockModel: MockModel = MockModel(cfg.mockParameters)

    private fun verifyOperation(operation: AbstractOperation): Boolean {
        operation.type.let {
            return TypeHelper.isValidType(it).and(
                !TypeHelper.hasTypePrefix(it, cfg.filterType)
            )
        }
    }

    private fun run(operation: AbstractOperation, operationManager: OperationManager) {
        if (!verifyOperation(operation)) {
            logger.info("operation ${operation.type} is invalid, skipped")
            return
        }

        for (eachMethod in operation.type.declaredMethods) {
            beforeMethod(eachMethod, operation, operationManager)
            runMethod(eachMethod, operation, operationManager)
            afterMethod(eachMethod, operation, operationManager)
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
                    beforeExec(this)
                    exec()
                    afterExec(this)
                } catch (e: InvocationTargetException) {
                    logger.warning("invoke failed: $e")
                } catch (e: Exception) {
                    logger.warning("unknown error happened")
                    e.printStackTrace()
                }
            }
        }
    }

    fun runAll(operationManager: OperationManager) {
        var op = operationManager.poll()
        while (null != op) {
            beforeOperation(op, operationManager)
            run(op, operationManager)
            afterOperation(op, operationManager)

            // get the next one
            op = operationManager.poll()
        }
        logger.info("run finished")
    }
}
