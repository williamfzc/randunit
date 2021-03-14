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
package com.williamfzc.randunit.operations

import com.williamfzc.randunit.models.DefaultMocker
import java.lang.reflect.Method

object DefaultOperationType

abstract class AbstractOperation {
    // todo: label for identify??
    open var type: Class<*> = DefaultOperationType::class.java

    // will only be called inside env in runtime
    abstract fun getInstance(): Any

    open fun canInvoke(method: Method): Boolean = true
    open fun tearDown(caller: Any) {}

    fun getInstanceSafely(): Any {
        try {
            return getInstance()
        } catch (e: TypeCastException) {
            DefaultMocker.mock(this.type)?.let {
                return it
            }
            return DefaultOperationType
        }
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractOperation) return false

        if (type != other.type) return false

        return true
    }

    // why not use lazy: it causes some errors in self-check
    fun id(): String {
        return type.name
    }
}
