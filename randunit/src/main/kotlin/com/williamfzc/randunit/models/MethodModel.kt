package com.williamfzc.randunit.models

import com.williamfzc.randunit.extensions.isStatic
import com.williamfzc.randunit.extensions.isValidType
import com.williamfzc.randunit.operations.AbstractOperation
import java.lang.Exception
import java.lang.reflect.Method
import java.util.logging.Logger

// desc this method
class MethodModel(
    private val callerOperation: AbstractOperation,
    private val method: Method,
    private val mockModel: MockModel
) {
    private val parametersTypes = method.parameterTypes

    companion object {
        private val logger = Logger.getLogger("MethodModel")
    }

    private fun generateCaller(): Any? {
        // todo: caller only (maybe) be accessed inside test runner
        // so this method should be called inside TestCase
        // parameters too!!
        try {
            if (method.isStatic())
                return callerOperation.type
            return callerOperation.getInstance()
        } catch (e: Exception) {
            // failed to create a real caller
            // use the mock one
            logger.warning("generate caller $callerOperation failed, use the mock one")
            e.printStackTrace()
            return mockModel.mock(callerOperation.type)
        }
    }

    fun getRelativeTypes(): Set<Class<*>> {
        val typeSet = parametersTypes.toMutableSet()
        val returnType = method.returnType
        if (returnType.isValidType())
            typeSet.add(returnType)
        return typeSet
    }

    fun generateStatement(): Statement? {
        val args = mutableListOf<Any?>()
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

            args.add(mockModel.mock(eachType))
        }
        // gen caller
        generateCaller()?.let { caller ->
            return Statement(method, caller, args.toList())
        }
        return null
    }
}
