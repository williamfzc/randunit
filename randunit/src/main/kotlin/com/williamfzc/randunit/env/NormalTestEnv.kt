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
import java.lang.reflect.Method
import java.util.logging.Logger

class NormalTestEnv @JvmOverloads constructor(envConfig: EnvConfig = EnvConfig()) :
    AbstractTestEnv(envConfig) {
    companion object {
        private val logger = Logger.getGlobal()
    }

    // this method only will be running inside sandbox
    // so it need no try-catch
    override fun runStatement(statement: Statement) {
        logger.info("running: ${statement.model.getDesc()}")

        val returnValueOfInvoke = synchronized(statement) {
            try {
                statement.invoke()
            } finally {
                statement.model.callerOperation.tearDown(statement.caller)
            }
        }

        returnValueOfInvoke?.let { v ->
            verifyReturnValue(v, statement.method)
        }
    }

    private fun verifyReturnValue(v: Any, method: Method) {
        val retType = v.javaClass
        val shouldBe = method.returnType.javaClass
        if (shouldBe.isAssignableFrom(retType))
            throw RUTypeException("return type $retType != $shouldBe")
    }
}
