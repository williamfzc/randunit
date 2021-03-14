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
import com.williamfzc.randunit.exceptions.RUException
import com.williamfzc.randunit.exceptions.RUTypeException
import com.williamfzc.randunit.mock.MockConfig
import com.williamfzc.randunit.mock.MockkMocker
import com.williamfzc.randunit.models.StatementModel
import com.williamfzc.randunit.scanner.Scanner
import com.williamfzc.randunit.scanner.ScannerConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SelfSmokeWithJUnit4Test(private val statementModel: StatementModel) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<StatementModel> {
            val scannerConfig = ScannerConfig(
                includeFilter = setOf("com.williamfzc.randunit"),
                recursively = true,
                includePrivateMethod = true
            )
            return RandUnit.collectStatementsWithCache(
                setOf(
                    RandUnit::class.java,
                    RandUnitBase::class.java,
                    NormalTestEnv::class.java,
                    Scanner::class.java,
                    MockkMocker::class.java,
                    EnvConfig::class.java,
                    RUTypeException::class.java,
                    RUException::class.java,
                ),
                scannerConfig
            )
        }
    }

    @Test
    fun run() {
        val mockConfig = MockConfig(ktFirst = true)
        val envConfig = EnvConfig(mockConfig, ignoreExceptions = setOf(IllegalStateException::class.java))
        val env = NormalTestEnv(envConfig)
        env.add(statementModel)
        env.start()
    }
}