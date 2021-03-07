package com.williamfzc.randunit.helper

object TypeHelper {
    var builtinTypeFilter = setOf("java.", "android.")

    fun isBuiltinType(t: Class<*>): Boolean = hasTypePrefix(t, builtinTypeFilter)

    fun isNullType(t: Class<*>): Boolean {
        return t == Void::class.java
    }

    fun hasTypePrefix(t: Class<*>, prefix: String): Boolean {
        return t.canonicalName.startsWith(prefix)
    }

    fun hasTypePrefix(t: Class<*>, prefixes: Set<String>): Boolean {
        for (each in prefixes)
            if (hasTypePrefix(t, each))
                return true
        return false
    }

    fun isValidType(t: Class<*>): Boolean {
        return !isBuiltinType(t).and(!isNullType(t))
    }
}
