package com.williamfzc.randunit.runner

import com.williamfzc.randunit.helper.MethodHelper
import com.williamfzc.randunit.helper.TypeHelper
import com.williamfzc.randunit.operations.AbstractOperation
import com.williamfzc.randunit.operations.OperationManager
import org.jeasy.random.ObjectCreationException
import java.lang.Exception
import java.util.logging.Logger

class Runner {
    companion object {
        private val logger = Logger.getLogger("Runner")
    }

    private fun run(operation: AbstractOperation, operationManager: OperationManager) {
        methodLoop@ for (eachMethod in operation.type.declaredMethods) {
            MethodHelper.forceMethodAccessible(eachMethod)

            // static or not
            val caller =
                if (MethodHelper.isMethodStatic(eachMethod))
                    operation.type
                else
                    operation.getInstance()

            // mock args
            val argTypes = eachMethod.parameterTypes
            val args = mutableListOf<Any>()
            for (eachType in argTypes) {
                try {
                    args.add(TypeHelper.mock(eachType))
                } catch (e: ObjectCreationException) {
                    e.printStackTrace()
                    continue@methodLoop
                }

                // add types in parameters for recursively query
                if (!TypeHelper.isBuiltinType(eachType))
                    operationManager.addClazz(eachType)
            }

            // exec
            logger.info("invoking: $eachMethod")
            try {
                eachMethod.invoke(caller, *args.toTypedArray())
            } catch (e: Exception) {
                logger.warning("invoke failed: $e")
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