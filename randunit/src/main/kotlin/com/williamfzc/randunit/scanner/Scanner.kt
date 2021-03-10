package com.williamfzc.randunit.scanner

import com.williamfzc.randunit.extensions.hasTypePrefix
import com.williamfzc.randunit.extensions.isBuiltin
import com.williamfzc.randunit.extensions.isPrivateOrProtected
import com.williamfzc.randunit.extensions.isValidType
import com.williamfzc.randunit.models.MethodModel
import com.williamfzc.randunit.operations.AbstractOperation
import com.williamfzc.randunit.operations.OperationManager
import java.lang.Exception
import java.lang.reflect.Method
import java.util.logging.Logger
import kotlin.reflect.jvm.javaMethod

open class Scanner(private val cfg: ScannerConfig = ScannerConfig()) :
    ScannerHookLayer {
    companion object {
        private val logger = Logger.getLogger("Scanner")
    }

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
        val collectedMethods = mutableListOf(*operation.type.declaredMethods)

        // inner classes should be considered
        for (eachInnerClass in operation.type.classes)
            operationManager.addClazz(eachInnerClass)

        // fields
        for (eachField in operation.type.declaredFields) {
            operationManager.addClazz(eachField.type)

            // access (get and set
            if (eachField.isAccessible) {
                collectedMethods.add(eachField::set.javaMethod)
                collectedMethods.add(eachField::get.javaMethod)
            }
        }

        for (eachMethod in collectedMethods) {
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
        val model = MethodModel(operation, method)

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
