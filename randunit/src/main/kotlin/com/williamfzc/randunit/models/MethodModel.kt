package com.williamfzc.randunit.models

import com.williamfzc.randunit.extensions.isValidType
import com.williamfzc.randunit.operations.AbstractOperation
import java.lang.reflect.Method

// desc this method
class MethodModel(
    private val callerOperation: AbstractOperation,
    private val method: Method,
    private val mockModel: MockModel
) {
    private val parametersTypes = method.parameterTypes

    fun getRelativeTypes(): Set<Class<*>> {
        val typeSet = parametersTypes.toMutableSet()
        val returnType = method.returnType
        if (returnType.isValidType())
            typeSet.add(returnType)
        return typeSet
    }

    fun generateStatement(): Statement? {
        // verify
        for (eachType in parametersTypes) {

            // todo: non-null check and attack
//            if (!eachType.isAnnotationPresent(NotNull::class.java)) {
//                args.add(null)
//                continue
//            }

            // easy-random can not mock java.lang.Class
            // which returns a `null` always
            if (eachType.canonicalName == "java.lang.Class")
                return null
        }
        return Statement(method, callerOperation, parametersTypes.toList(), mockModel)
    }
}
