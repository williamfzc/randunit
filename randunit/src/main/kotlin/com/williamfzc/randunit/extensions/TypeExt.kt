/**
 * Copyright 2021 williamfzc
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.williamfzc.randunit.extensions

import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

val BUILTIN_TYPE_PREFIX_FILTER = setOf("java.lang.Object", "kotlin.")

fun Class<*>.hasTypePrefix(prefix: String): Boolean {
    return this.name.startsWith(prefix)
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
}

// notice that:
// modifier can be `private` or `public` or `final` ..., not `private final`
// https://stackoverflow.com/questions/3301635/change-private-static-final-field-using-java-reflection
fun Class<*>.getTypeModifiersWithoutFinal(): Int = this.modifiers and Modifier.FINAL.inv()

fun Class<*>.isPrivate(): Boolean = Modifier.isPrivate(this.getTypeModifiersWithoutFinal())
fun Class<*>.isProtected(): Boolean = Modifier.isProtected(this.getTypeModifiersWithoutFinal())
fun Class<*>.isPrivateOrProtected(): Boolean = this.isPrivate().or(this.isProtected())
fun Class<*>.getDeclaredMethodsSafely(): Array<out Method> {
    return try {
        this.declaredMethods
    } catch (e: Throwable) {
        when (e) {
            is VerifyError -> arrayOf()
            is ClassNotFoundException -> arrayOf()
            is NoClassDefFoundError -> arrayOf()
            else -> throw e
        }
    }
}

fun Class<*>.getMethodsSafely(): Array<out Method> {
    return try {
        this.methods
    } catch (e: Throwable) {
        when (e) {
            is VerifyError -> arrayOf()
            is ClassNotFoundException -> arrayOf()
            is NoClassDefFoundError -> arrayOf()
            else -> throw e
        }
    }
}

fun Class<*>.getDeclaredClassesSafely(): Array<out Class<*>> {
    return try {
        this.declaredClasses
    } catch (e: Throwable) {
        when (e) {
            is VerifyError -> arrayOf()
            is ClassNotFoundException -> arrayOf()
            is NoClassDefFoundError -> arrayOf()
            else -> throw e
        }
    }
}

fun Class<*>.getDeclaredFieldsSafely(): Array<out Field> {
    return try {
        this.declaredFields
    } catch (e: Throwable) {
        when (e) {
            is VerifyError -> arrayOf()
            is ClassNotFoundException -> arrayOf()
            is NoClassDefFoundError -> arrayOf()
            else -> throw e
        }
    }
}
