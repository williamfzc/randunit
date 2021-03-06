package com.williamfzc.randunit.models

import com.williamfzc.randunit.helper.MethodHelper
import com.williamfzc.randunit.helper.TypeHelper
import com.williamfzc.randunit.operations.AbstractOperation
import org.jeasy.random.ObjectCreationException
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

    private fun generateCaller(): Any {
        // todo: singleton?
        if (MethodHelper.isMethodStatic(method))
            return callerOperation.type
        return callerOperation.getInstance()
    }

    fun getRelativeTypes(): Set<Class<*>> {
        val typeSet = parametersTypes.toMutableSet()
        val returnType = method.returnType
        if (TypeHelper.isValidType(returnType))
            typeSet.add(returnType)
        return typeSet
    }

    fun generateStatement(): Statement? {
        val args = mutableListOf<Any?>()
        for (eachType in parametersTypes) {
            if (TypeHelper.isNullType(eachType)) {
                args.add(null)
                continue
            }
            try {
                args.add(mockModel.mock(eachType))
            } catch (e: ObjectCreationException) {
                logger.warning("object mock failed: $eachType")
                return null
            }
        }
        // gen caller
        val caller = try {
            generateCaller()
        } catch (e: Exception) {
            logger.warning("gen caller failed: $e")
            return null
        }

        return Statement(method, caller, args.toList())
    }
}
