package com.williamfzc.randunit.runner

import com.williamfzc.randunit.operations.AbstractOperation
import com.williamfzc.randunit.operations.OperationManager
import org.jeasy.random.EasyRandom
import org.jeasy.random.ObjectCreationException
import java.lang.Exception
import java.lang.reflect.Modifier
import java.util.logging.Logger

class Runner {
    companion object {
        private val easyRandom = EasyRandom()
        private val logger = Logger.getLogger("Runner")
    }


    fun run(operation: AbstractOperation, operationManager: OperationManager) {
        methodLoop@ for (eachMethod in operation.type.declaredMethods) {
            if (!eachMethod.isAccessible)
                eachMethod.isAccessible = true

            val caller =
                if (Modifier.isStatic(eachMethod.modifiers))
                    operation.type
                else
                    operation.getInstance()

            // args
            val argTypes = eachMethod.parameterTypes
            val args = mutableListOf<Any>()
            for (eachType in argTypes) {
                try {
                    args.add(easyRandom.nextObject(eachType))
                } catch (e: ObjectCreationException) {
                    e.printStackTrace()
                    continue@methodLoop
                }

                // not built-in type
                if (!eachType.canonicalName.startsWith("java.") && !eachType.canonicalName.startsWith("android."))
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