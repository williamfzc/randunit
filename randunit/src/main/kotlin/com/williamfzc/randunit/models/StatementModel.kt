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

import com.williamfzc.randunit.operations.AbstractOperation
import java.lang.reflect.Method

data class StatementModel(
    // a `method call` may looks like:
    // methodA(caller, param1, param2)
    val method: Method,
    val callerOperation: AbstractOperation,
    val parametersTypes: List<Class<*>>
) {
    fun getName(): String {
        return "statement_${method}_with_$parametersTypes"
    }

    fun getDesc(): String {
        return "[caller ${callerOperation.type}] invoking [method $method] with [params $parametersTypes]"
    }
}
