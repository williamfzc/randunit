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
import io.mockk.MockKException
import java.lang.Exception
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.logging.Logger

class NormalTestEnv(private val envConfig: EnvConfig = EnvConfig()) : AbstractTestEnv(envConfig) {
    companion object {
        private val logger = Logger.getLogger("StandardTestEnv")
        private val BUILTIN_IGNORED_EXCEPTIONS = setOf(MockKException::class.java)
    }
    private val ignoredExceptions = BUILTIN_IGNORED_EXCEPTIONS.plus(envConfig.ignoreExceptions)

    private fun generateCaller(statementModel: StatementModel): Any? {
        val callOperation = statementModel.callerOperation
        try {
            if (statementModel.method.isStatic())
                return callOperation.type
            return callOperation.getInstance()
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
            }
        }
        return ret
    }

    override fun run(statementModel: StatementModel) {
        logger.info("running: ${statementModel.getDesc()}")

        val caller = generateCaller(statementModel)
        caller ?: run {
            logger.warning("generate caller failed, skipped")
            return
        }

        val parameters = generateParameters(statementModel)
        if (parameters.size != statementModel.parametersTypes.size) {
            logger.warning("parameters mock failed, skipped")
            return
        }
        runSafely(caller, statementModel.method, parameters)
    }

    private fun isIgnoredException(e: Exception): Boolean {
        for (eachIgnoreException in ignoredExceptions) {
            if (e::class.java == eachIgnoreException)
                return true
        }
        return false
    }

    private fun runSafely(caller: Any, method: Method, parameters: List<Any?>) {
        val returnValueOfInvoke: Any? = try {
            method.invoke(caller, *parameters.toTypedArray())
        } catch (e: Exception) {
            val realException =
                if ((e is InvocationTargetException).and(null != e.cause))
                    e.cause as Exception
                else
                    e
            if (!isIgnoredException(realException))
                throw realException

            // else, ignore
            logger.info("error $realException happened but allowed")
            null
        }

        returnValueOfInvoke?.let { v ->
            val retType = v.javaClass
            val shouldBe = method.returnType.javaClass
            if (shouldBe.isAssignableFrom(retType))
                throw RUTypeException("return type $retType != $shouldBe")
        }
    }
}
