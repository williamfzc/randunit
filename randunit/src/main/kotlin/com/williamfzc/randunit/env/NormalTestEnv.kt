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
package com.williamfzc.randunit.env

import com.williamfzc.randunit.exceptions.RUTypeException
import com.williamfzc.randunit.extensions.isStatic
import com.williamfzc.randunit.models.StatementModel
import com.williamfzc.randunit.operations.AbstractOperation
import java.lang.Exception
import java.lang.reflect.Method
import java.util.logging.Logger

class NormalTestEnv @JvmOverloads constructor(envConfig: EnvConfig = EnvConfig()) :
    AbstractTestEnv(envConfig) {
    companion object {
        private val logger = Logger.getGlobal()
    }

    private val callerCache = mutableMapOf<String, Any>()

    private fun getCallerInstFromCache(curCallOp: AbstractOperation): Any? {
        return callerCache[curCallOp.id()]
    }

    private fun generateCaller(statementModel: StatementModel): Any? {
        val callOperation = statementModel.callerOperation
        if (envConfig.reuseCaller)
            getCallerInstFromCache(callOperation)?.let { return it }
        try {
            if (statementModel.method.isStatic())
                return callOperation.type
            return callOperation.getInstanceSafely()
        } catch (e: Exception) {
            // failed to create a real caller
            // use the mock one
            logger.warning("generate caller $callOperation failed, use the mock one")
            e.printStackTrace()
            return mockModel.mock(callOperation.type)
        }
    }

    private fun generateParameters(statementModel: StatementModel): List<Any?> {
        val ret = mutableListOf<Any?>()
        for (each in statementModel.parametersTypes) {
            mockModel.mock(each)?.let {
                ret.add(it)
            } ?: run {
                logger.warning("mock $each failed finally")
            }
        }
        return ret
    }

    private fun verifyCallerInst(caller: Any?): Boolean {
        caller ?: run {
            logger.warning("generate caller failed, skipped")
            return false
        }
        // base types (kt issues ...
        if (caller is Unit)
            return false
        return true
    }

    // this method only will be running inside sandbox
    // so it need no try-catch
    override fun run(statementModel: StatementModel) {
        logger.info("running: ${statementModel.getDesc()}")

        // at least it will not cause any crashes because of mock
        val caller = generateCaller(statementModel)
        if (!verifyCallerInst(caller))
            return
        caller as Any

        val parameters = generateParameters(statementModel)
        if (parameters.size != statementModel.parametersTypes.size) {
            logger.warning("parameters mock failed, skipped")
            return
        }

        // ok
        logger.info(
            """
            statement initiated:
            - params: $parameters
            - caller: $caller
            - method: ${statementModel.method}
            """.trimIndent()
        )
        return actualRun(
            caller,
            statementModel.method,
            parameters,
            statementModel.callerOperation
        )
    }

    private fun verifyReturnValue(v: Any, method: Method) {
        val retType = v.javaClass
        val shouldBe = method.returnType.javaClass
        if (shouldBe.isAssignableFrom(retType))
            throw RUTypeException("return type $retType != $shouldBe")
    }

    private fun actualRun(
        caller: Any,
        method: Method,
        parameters: List<Any?>,
        operation: AbstractOperation
    ) {
        val returnValueOfInvoke = synchronized(method) {
            try {
                method.invoke(caller, *parameters.toTypedArray())
            } finally {
                operation.tearDown(caller)
            }
        }

        returnValueOfInvoke?.let { v ->
            // run successful, cache this caller if `reuse` enabled
            if (envConfig.reuseCaller)
                callerCache[operation.id()] = caller

            verifyReturnValue(v, method)
        }
    }
}
