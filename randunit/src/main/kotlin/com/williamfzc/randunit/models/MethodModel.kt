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
package com.williamfzc.randunit.models

import com.williamfzc.randunit.extensions.isValidType
import com.williamfzc.randunit.operations.AbstractOperation
import java.lang.reflect.Method

// desc this method
class MethodModel(
    private val callerOperation: AbstractOperation,
    private val method: Method
) {
    private val parametersTypes = method.parameterTypes

    fun getRelativeTypes(): Set<Class<*>> {
        val typeSet = parametersTypes.toMutableSet()
        val returnType = method.returnType
        if (returnType.isValidType())
            typeSet.add(returnType)
        return typeSet
    }

    fun generateStatement(): StatementModel? {
        // verify
        for (eachType in parametersTypes) {

            // todo: non-null check and attack
//            if (!eachType.isAnnotationPresent(NotNull::class.java)) {
//                args.add(null)
//                continue
//            }

            // easy-random can not mock java.lang.Class
            // which returns a `null` always
            if (eachType.canonicalName == "java.lang.Class")
                return null
        }
        return StatementModel(method, callerOperation, parametersTypes.toList())
    }
}
