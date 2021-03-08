package com.williamfzc.randunit.runner

import com.williamfzc.randunit.extensions.hasTypePrefix
import com.williamfzc.randunit.extensions.isBuiltin
import com.williamfzc.randunit.extensions.isPrivateOrProtected
import com.williamfzc.randunit.extensions.isValidType
import com.williamfzc.randunit.models.MethodModel
import com.williamfzc.randunit.models.MockModel
import com.williamfzc.randunit.operations.AbstractOperation
import com.williamfzc.randunit.operations.OperationManager
import java.lang.Exception
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.logging.Logger

open class Runner(private val cfg: RunnerConfig = RunnerConfig()) : RunnerHookLayer {
    companion object {
        private val logger = Logger.getLogger("Runner")
    }

    var mockModel: MockModel = MockModel(cfg.mockConfig)
    var statementCount = 0
    var opHistory = mutableSetOf<String>()

    private fun verifyOperation(operation: AbstractOperation): Boolean {
        operation.type.let {
            return it.isValidType().and(
                !it.hasTypePrefix(cfg.filterType)
                    .and(!opHistory.contains(operation.getId()))
            )
        }
    }

    private fun verifyMethod(method: Method): Boolean {
        if (method.isBuiltin())
            return false

        // exclude protected and private methods
        method.isAccessible = true
        if (method.isPrivateOrProtected()) {
            if (!cfg.includePrivateMethod)
                return false
        }
        return true
    }

    private fun run(operation: AbstractOperation, operationManager: OperationManager) {
        // todo: depth for avoiding recursively run
        if (!verifyOperation(operation)) {
            logger.info("operation ${operation.type} is invalid, skipped")
            return
        }
        logger.info("start running op: ${operation.type.canonicalName}")

        for (eachMethod in operation.type.declaredMethods) {
            if (!verifyMethod(eachMethod)) {
                logger.info("verify method ${eachMethod.name} false, skipped")
                continue
            }
            beforeMethod(eachMethod, operation, operationManager)
            runMethod(eachMethod, operation, operationManager)
            afterMethod(eachMethod, operation, operationManager)
        }
        logger.info("op $operation end")
        opHistory.add(operation.getId())
    }

    private fun runMethod(
        method: Method,
        operation: AbstractOperation,
        operationManager: OperationManager
    ) {
        val model = MethodModel(operation, method, mockModel)

        // append some rel types
        for (eachRelType in model.getRelativeTypes())
            operationManager.addClazz(eachRelType)

        for (i in 1..cfg.batchSize) {
            val stat = model.generateStatement()
            statementCount++
            stat?.run {
                try {
                    logger.info("invoking: $method")
                    beforeExec(this)
                    if (!cfg.dryRun)
                        exec()
                    afterExec(this)
                } catch (e: InvocationTargetException) {
                    logger.warning("invoke failed: $e")
                } catch (e: Exception) {
                    logger.warning("unknown error happened: $e")
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

            if (statementCount >= cfg.statementLimit) {
                logger.info("statement count reach limit: ${cfg.statementLimit}")
                break
            }
        }
        logger.info("run finished")
    }
}
