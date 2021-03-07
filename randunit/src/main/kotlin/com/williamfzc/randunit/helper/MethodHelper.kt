package com.williamfzc.randunit.helper

import java.lang.reflect.Method
import java.lang.reflect.Modifier

object MethodHelper {
    fun isMethodStatic(method: Method): Boolean {
        return Modifier.isStatic(method.modifiers)
    }

    fun forceMethodAccessible(method: Method) {
        method.isAccessible = true
    }

    fun isBuiltinMethod(method: Method): Boolean {
        for (eachPrefix in TypeHelper.builtinTypeFilter)
            if (method.toGenericString().startsWith(eachPrefix))
                return true
        return false
    }
}