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

import com.williamfzc.randunit.models.StatementModel
import java.util.logging.Logger

class Sandbox(cfg: SandboxConfig) {
    private val rules = cfg.rules

    companion object {
        private val logger = Logger.getLogger("Sandbox")
    }

    fun runSafely(statementModel: StatementModel, realRun: (StatementModel) -> Unit) {
        try {
            realRun(statementModel)
        } catch (e: Throwable) {
            if (!checkRules(statementModel, e))
                throw e
        }
    }

    private fun checkRules(statementModel: StatementModel, e: Throwable): Boolean {
        for (each in rules)
            if (each.judge(statementModel, e)) {
                logger.info("$e happened but allowed by rule: $each")
            }
        return false
    }
}
