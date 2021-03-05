package com.williamfzc.randunit.runner

import com.williamfzc.randunit.operations.AbstractOperation
import com.williamfzc.randunit.operations.OperationManager
import org.jeasy.random.EasyRandom
import java.lang.reflect.Modifier

class Runner {
    private val easyRandom = EasyRandom()

    fun run(operation: AbstractOperation, operationManager: OperationManager) {
        for (eachMethod in operation.type.declaredMethods) {
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
                args.add(easyRandom.nextObject(eachType))

                // not built-in type
                if (!eachType.canonicalName.startsWith("java"))
                    operationManager.addClazz(eachType)
            }

            // exec
            println("invoking: $eachMethod")
            eachMethod.invoke(caller, *args.toTypedArray())
        }
    }

    fun run(operationManager: OperationManager) {
        var op = operationManager.popFirst()
        while (null != op) {
            run(op, operationManager)
            op = operationManager.popFirst()
        }
    }
}