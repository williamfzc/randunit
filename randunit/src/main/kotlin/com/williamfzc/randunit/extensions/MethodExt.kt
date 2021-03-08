package com.williamfzc.randunit.extensions

import java.lang.reflect.Method
import java.lang.reflect.Modifier

fun Method.isStatic(): Boolean = Modifier.isStatic(this.modifiers)

fun Method.isBuiltin(): Boolean {
    for (eachPrefix in BUILTIN_TYPE_PREFIX_FILTER)
        if (this.toGenericString().startsWith(eachPrefix))
            return true
    return false
}

fun Method.isPrivateOrProtected(): Boolean = this::class.java.isPrivateOrProtected()
