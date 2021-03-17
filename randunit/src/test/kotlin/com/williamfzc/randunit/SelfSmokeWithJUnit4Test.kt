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
package com.williamfzc.randunit

import com.williamfzc.randunit.env.EnvConfig
import com.williamfzc.randunit.env.NormalTestEnv
import com.williamfzc.randunit.env.rules.AbstractRule
import com.williamfzc.randunit.env.sandbox.SandboxConfig
import com.williamfzc.randunit.mock.MockConfig
import com.williamfzc.randunit.models.StatementModel
import com.williamfzc.randunit.scanner.ScannerConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.lang.NullPointerException

@RunWith(Parameterized::class)
class SelfSmokeWithJUnit4Test(private val statementModel: StatementModel) {
    companion object {
        private val testEnv = NormalTestEnv()
        private const val packageName = "com.williamfzc.randunit"

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<StatementModel> {
            // env setup
            val envConfig = EnvConfig()
            // mock first
            envConfig.mockConfig = MockConfig(ktFirst = true)
            // sandbox setup
            val sandboxConfig = SandboxConfig()

            // custom rules
            class CustomRule : AbstractRule() {
                override fun judge(statementModel: StatementModel, e: Throwable): Boolean {
                    return when (e) {
                        is IllegalStateException -> true
                        is UnsupportedOperationException -> true
                        is InternalError -> true
                        is NullPointerException -> {
                            e.stackTrace[0].className.contains("java.io.File")
                        }
                        else -> false
                    }
                }
            }
            sandboxConfig.rules.add(CustomRule())
            envConfig.sandboxConfig = sandboxConfig

            testEnv.envConfig = envConfig

            // setup ok, start scanning
            val scannerConfig = ScannerConfig()
            scannerConfig.includeFilter.add(packageName)

            return RandUnit.collectStatementsWithPackage(packageName, scannerConfig)
        }
    }

    @Test
    fun runStatements() {
        testEnv.runWithSandbox(statementModel)
    }
}
