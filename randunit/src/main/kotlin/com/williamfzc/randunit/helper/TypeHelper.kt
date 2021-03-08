package com.williamfzc.randunit.helper

import java.lang.reflect.Modifier

val BUILTIN_TYPE_PREFIX_FILTER = setOf("java.", "android.", "kotlin.")

fun Class<*>.hasTypePrefix(prefix: String): Boolean {
    return this.canonicalName.startsWith(prefix)
}

fun Class<*>.hasTypePrefix(prefixes: Set<String>): Boolean {
    for (each in prefixes)
        if (this.hasTypePrefix(each))
            return true
    return false
}

fun Class<*>.isBuiltinType(): Boolean = this.hasTypePrefix(BUILTIN_TYPE_PREFIX_FILTER)

fun Class<*>.isNullType(): Boolean = this == Void::class.java

fun Class<*>.isAbstract(): Boolean = Modifier.isAbstract(this.modifiers)

fun Class<*>.isValidType(): Boolean {
    return (!this.isBuiltinType())
        .and(!this.isNullType())
        .and(!this.isPrimitive)
        .and(!this.isInterface)
        .and(!this.isAbstract())
}

// notice that:
// modifier can be `private` or `public` or `final` ..., not `private final`
// https://stackoverflow.com/questions/3301635/change-private-static-final-field-using-java-reflection
fun Class<*>.getTypeModifiersWithoutFinal(): Int = this.modifiers and Modifier.FINAL.inv()

fun Class<*>.isPrivate(): Boolean = Modifier.isPrivate(this.getTypeModifiersWithoutFinal())
fun Class<*>.isProtected(): Boolean = Modifier.isProtected(this.getTypeModifiersWithoutFinal())
fun Class<*>.isPrivateOrProtected(): Boolean = this.isPrivate().or(this.isProtected())
