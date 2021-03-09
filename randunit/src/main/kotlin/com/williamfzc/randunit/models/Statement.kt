package com.williamfzc.randunit.models

import com.williamfzc.randunit.operations.AbstractOperation
import java.lang.reflect.Method

data class Statement(
    // a `method call` may looks like:
    // methodA(caller, param1, param2)
    val method: Method,
    val callerOperation: AbstractOperation,
    val parametersTypes: List<Class<*>>
) {
    fun getName(): String {
        return "statement_${method}_with_$parametersTypes"
    }

    fun getDesc(): String {
        return "[caller ${callerOperation.type}] invoking [method $method] with [params $parametersTypes]"
    }
}
