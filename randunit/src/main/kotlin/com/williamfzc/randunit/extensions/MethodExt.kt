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

import java.lang.reflect.Method
import java.lang.reflect.Modifier

fun Method.isStatic(): Boolean = Modifier.isStatic(this.modifiers)

fun Method.isBuiltin(): Boolean {
    for (eachPrefix in BUILTIN_TYPE_PREFIX_FILTER) {
        if (this.declaringClass.name.contains(eachPrefix))
            return true
    }
    return false
}

fun Method.isPrivateOrProtected(): Boolean = this::class.java.isPrivateOrProtected()

fun Method.isNative(): Boolean = Modifier.isNative(this.modifiers)
