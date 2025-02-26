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
import com.williamfzc.randunit.env.Statement
import com.williamfzc.randunit.env.rules.AbstractRule
import com.williamfzc.randunit.env.rules.SuppressAllRule
import com.williamfzc.randunit.exceptions.RUException
import com.williamfzc.randunit.exceptions.RUTypeException
import com.williamfzc.randunit.mock.MockConfig
import com.williamfzc.randunit.mock.MockkMocker
import com.williamfzc.randunit.operations.OperationManager
import com.williamfzc.randunit.scanner.Scanner
import com.williamfzc.randunit.scanner.ScannerConfig
import com.williamfzc.randunit.testres.AAA
import com.williamfzc.randunit.testres.CCC
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.lang.NullPointerException

class SelfSmokeWithJUnit5Test {
    @Test
    fun scanSimple() {
        val opm = OperationManager()
        opm.addClazz(CCC::class.java)
        opm.addClazz(AAA::class.java)

        val scannerConfig = ScannerConfig()
        scannerConfig.includeFilter.add("com.williamfzc.randunit")
        Scanner(scannerConfig).scanAll(opm)
    }

    @Test
    fun scanAndCollect() {
        val scannerConfig = ScannerConfig()
        scannerConfig.includeFilter.add("com.williamfzc.randunit")

        val clzSet = setOf(
            RandUnit::class.java,
            RandUnitBase::class.java,
            NormalTestEnv::class.java,
            Scanner::class.java,
            MockkMocker::class.java,
            EnvConfig::class.java,
            RUTypeException::class.java,
            RUException::class.java,
            SuppressAllRule::class.java,
            MockkMocker::class.java
        )
        val operations = RandUnit.collectOperations(clzSet, scannerConfig)
        val statements = RandUnit.collectStatements(clzSet, scannerConfig)
        Assert.assertTrue(operations.isNotEmpty())
        Assert.assertTrue(statements.isNotEmpty())
    }

    @TestFactory
    fun scanItself(): Iterable<DynamicTest> {
        class CustomRule : AbstractRule() {
            override fun judge(statement: Statement, e: Throwable): Boolean {
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

        val scannerConfig = ScannerConfig()
        scannerConfig.includeFilter.add("com.williamfzc.randunit")

        val mockConfig = MockConfig(ktFirst = true)
        val envConfig = EnvConfig(mockConfig)
        envConfig.sandboxConfig.rules.add(CustomRule())

        val clsSet = setOf(Scanner::class.java)
        return RandUnit.runWithTestFactory(clsSet, scannerConfig, envConfig = envConfig)
    }
}
