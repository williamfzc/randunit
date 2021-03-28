/*
 * Copyright 2021 williamfzc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.williamfzc.randunit.env.sandbox

import com.williamfzc.randunit.env.Statement
import com.williamfzc.randunit.env.rules.BuiltinRule
import java.lang.reflect.InvocationTargetException
import java.util.logging.Logger

open class Sandbox(open var cfg: SandboxConfig) {
    companion object {
        private val logger = Logger.getLogger("Sandbox")
    }

    var defaultRules = setOf(BuiltinRule())

    fun runSafely(statement: Statement, realRun: (Statement) -> Unit): Throwable? {
        try {
            realRun(statement)
        } catch (e: Throwable) {
            logger.warning("error happened inside sandbox: $e")
            val realException =
                if ((e is InvocationTargetException).and(null != e.cause))
                // it can be an Error type
                    e.cause as Throwable
                else
                    e

            // declared exceptions?
            statement.method.exceptionTypes.let { declaredExceptions ->
                if (declaredExceptions.contains(e::class.java)) {
                    logger.info("error happened but it's declared in $declaredExceptions")
                    return null
                }
            }

            // no rules match
            if (!checkRules(statement, realException))
                return realException
        }
        // nothing happened if match
        return null
    }

    private fun checkRules(statement: Statement, e: Throwable): Boolean {
        for (each in cfg.rules.plus(defaultRules))
            if (each.judge(statement, e)) {
                logger.info("$e happened but allowed by rule: $each")
                return true
            }
        return false
    }
}
