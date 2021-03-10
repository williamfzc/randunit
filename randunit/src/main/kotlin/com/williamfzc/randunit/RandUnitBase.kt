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

import com.williamfzc.randunit.env.NormalTestEnv
import com.williamfzc.randunit.models.StatementModel
import com.williamfzc.randunit.operations.OperationManager
import com.williamfzc.randunit.scanner.Scanner
import com.williamfzc.randunit.scanner.ScannerConfig
import io.mockk.MockKException
import org.junit.jupiter.api.DynamicTest
import java.lang.Exception
import java.util.logging.Logger

abstract class RandUnitBase {
    companion object {
        private val logger = Logger.getLogger("RandUnit")
    }

    abstract fun getOperationManager(): OperationManager

    fun collectStatements(
        targetClasses: Set<Class<*>>,
        cfg: ScannerConfig? = null
    ): List<StatementModel> {
        val ret = mutableListOf<StatementModel>()

        class CustomScanner(cfg: ScannerConfig) : Scanner(cfg) {
            override fun handle(statementModel: StatementModel) {
                ret.add(statementModel)
            }
        }

        val finalCfg = cfg ?: ScannerConfig()
        val opm = getOperationManager()
        for (eachClazz in targetClasses)
            opm.addClazz(eachClazz)
        CustomScanner(finalCfg).scanAll(opm)
        logger.info("scan finished, statements count: ${ret.size}")
        return ret
    }

    fun runWithTestFactory(
        targetClasses: Set<Class<*>>,
        cfg: ScannerConfig? = null
    ): Iterable<DynamicTest> {
        val dynamicTests: MutableList<DynamicTest> = ArrayList()

        collectStatements(targetClasses, cfg).forEach {
            val dynamicTest: DynamicTest = DynamicTest.dynamicTest(it.getName()) {
                try {
                    val env = NormalTestEnv()
                    env.add(it)
                    env.start()
                } catch (e: Exception) {
                    if ((e.cause !is MockKException).and(e.cause !is UninitializedPropertyAccessException))
                        throw e
                }
            }
            dynamicTests.add(dynamicTest)
        }

        return dynamicTests
    }
}
