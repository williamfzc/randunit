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
package com.williamfzc.randunit.env.strategy

import com.williamfzc.randunit.env.Statement
import com.williamfzc.randunit.extensions.isStatic
import com.williamfzc.randunit.models.MockModel
import com.williamfzc.randunit.models.StatementModel
import java.lang.Exception
import java.util.logging.Logger

fun verifyFunc(s: Statement): String? {
    s.executedResult?.let {
        val retType = it.javaClass
        val shouldBe = s.method.returnType.javaClass
        if (shouldBe.isAssignableFrom(retType))
            return "return type $retType != $shouldBe"
    }

    // can not verify
    return null
}

object NormalStrategy : AbstractStrategy() {
    private val logger = Logger.getGlobal()

    override fun genStatements(
        statementModel: StatementModel,
        mockModel: MockModel
    ): Iterable<Statement> {
        // at least it will not cause any crashes because of mock
        var caller = generateCaller(statementModel)
        caller ?: run {
            // failed and mock not allowed
            if (!statementModel.callerOperation.canMock()) {
                logger.warning("type ${statementModel.callerOperation} can not mock, skipped")
                return setOf()
            }
            // use the mock one
            val op = statementModel.callerOperation
            logger.warning("generate caller ${op.type} failed, use the mock one")
            caller = mockModel.mock(op.type)
        }

        if (!verifyCallerInst(caller))
            return setOf()
        // caller is ready

        val parameters = generateParameters(statementModel)
        if (parameters.size != statementModel.parametersTypes.size) {
            logger.warning("parameters mock failed, skipped")
            return setOf()
        }

        // by default, gen only one statement
        return setOf(
            Statement(
                statementModel.method,
                caller!!,
                parameters,
                statementModel,
                ::verifyFunc
            )
        )
    }

    private fun generateCaller(statementModel: StatementModel): Any? {
        val callOperation = statementModel.callerOperation
        try {
            // https://stackoverflow.com/questions/2474017/using-reflection-to-change-static-final-file-separatorchar-for-unit-testing/2474242#2474242
            val method = statementModel.method
            if (method.isStatic()) {
                logger.info("method $method is static, use `Class` as caller")
                return callOperation.type
            }
            logger.info("method $method is not static, use `instance` as caller")
            return callOperation.getInstanceWithCache()
        } catch (e: Exception) {
            // failed to create a real caller
            // try to recreate
            callOperation.cleanInstanceCache()
            return try {
                callOperation.getInstanceWithCache()
            } catch (e: Exception) {
                // give up
                e.printStackTrace()
                null
            }
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
}
