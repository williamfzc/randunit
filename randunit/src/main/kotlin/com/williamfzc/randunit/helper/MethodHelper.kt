package com.williamfzc.randunit.helper

import java.lang.reflect.Method
import java.lang.reflect.Modifier

object MethodHelper {
    fun isMethodStatic(method: Method): Boolean {
        return Modifier.isStatic(method.modifiers)
    }

    fun isMethodAccessible(method: Method): Boolean {
        return method.isAccessible
    }

    fun forceMethodAccessible(method: Method) {
        method.isAccessible = true
    }
}