package com.williamfzc.randunit.scanner

import com.williamfzc.randunit.extensions.hasTypePrefix
import com.williamfzc.randunit.extensions.isBuiltin
import com.williamfzc.randunit.extensions.isPrivateOrProtected
import com.williamfzc.randunit.extensions.isValidType
import com.williamfzc.randunit.models.MethodModel
import com.williamfzc.randunit.models.MockModel
import com.williamfzc.randunit.operations.AbstractOperation
import com.williamfzc.randunit.operations.OperationManager
import java.lang.Exception
import java.lang.reflect.Method
import java.util.logging.Logger

open class Scanner(private val cfg: ScannerConfig = ScannerConfig()) :
    ScannerHookLayer {
    companion object {
        private val logger = Logger.getLogger("Scanner")
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

    private fun scan(operation: AbstractOperation, operationManager: OperationManager) {
        // todo: depth for avoiding recursively run
        if (!verifyOperation(operation)) {
            logger.info("operation ${operation.type} is invalid, skipped")
            return
        }
        logger.info("start scanning op: ${operation.type.canonicalName}")

        for (eachMethod in operation.type.declaredMethods) {
            if (!verifyMethod(eachMethod)) {
                logger.info("verify method ${eachMethod.name} false, skipped")
                continue
            }
            beforeMethod(eachMethod, operation, operationManager)
            scanMethod(eachMethod, operation, operationManager)
            afterMethod(eachMethod, operation, operationManager)
        }
        logger.info("op $operation end")
        opHistory.add(operation.getId())
    }

    private fun scanMethod(
        method: Method,
        operation: AbstractOperation,
        operationManager: OperationManager
    ) {
        val model = MethodModel(operation, method, mockModel)

        // append some rel types
        for (eachRelType in model.getRelativeTypes())
            operationManager.addClazz(eachRelType)

        val stat = model.generateStatement()
        statementCount++
        stat?.run {
            try {
                handle(this)
            } catch (e: Exception) {
                logger.warning("unknown error happened: $e")
                e.printStackTrace()
            }
        }
    }

    fun scanAll(operationManager: OperationManager) {
        var op = operationManager.poll()
        while (null != op) {
            beforeOperation(op, operationManager)
            scan(op, operationManager)
            afterOperation(op, operationManager)

            // get the next one
            op = operationManager.poll()

            if (statementCount >= cfg.statementLimit) {
                logger.info("statement count reach limit: ${cfg.statementLimit}")
                break
            }
        }
        logger.info("scan finished")
    }
}