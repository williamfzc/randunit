package com.williamfzc.randunit.helper

import java.lang.reflect.Modifier

object TypeHelper {
    var builtinTypeFilter = setOf("java.", "android.", "kotlin.")

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

    fun isAbstract(t: Class<*>): Boolean {
        return Modifier.isAbstract(t.modifiers)
    }

    fun isValidType(t: Class<*>): Boolean {
        return (!isBuiltinType(t))
            .and(!isNullType(t))
            .and(!t.isPrimitive)
            .and(!t.isInterface)
            .and(!isAbstract(t))
    }
}
