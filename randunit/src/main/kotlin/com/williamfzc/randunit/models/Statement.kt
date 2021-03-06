package com.williamfzc.randunit.models

import java.lang.reflect.Method

class Statement(
    private val method: Method,
    private val caller: Any,
    private val parameters: List<Any>
) {
    // todo: statement is a data
    fun exec() {
        method.invoke(caller, *parameters.toTypedArray())
    }
}