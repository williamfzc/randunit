package com.williamfzc.randunit.helper

object TypeHelper {
    var builtinTypeFilter = setOf("java.", "android.")

    fun isBuiltinType(t: Class<*>): Boolean {
        for (each in builtinTypeFilter)
            if (t.canonicalName.startsWith(each))
                return true
        return false
    }

    fun isNullType(t: Class<*>): Boolean {
        return t == Void::class.java
    }

    fun isValidType(t: Class<*>): Boolean {
        return !isBuiltinType(t).and(!isNullType(t))
    }
}
