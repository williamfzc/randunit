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
import com.williamfzc.randunit.models.StatementModel
import com.williamfzc.randunit.models.toJson
import com.williamfzc.randunit.operations.AbstractOperation
import com.williamfzc.randunit.operations.OperationManager
import com.williamfzc.randunit.scanner.Scanner
import com.williamfzc.randunit.scanner.ScannerConfig
import io.mockk.MockKException
import org.junit.jupiter.api.DynamicTest
import org.reflections.Reflections
import java.util.logging.Logger

abstract class RandUnitBase : RandUnitBaseImpl() {
    companion object {
        private val logger = Logger.getGlobal()
    }

    private var statementCache: List<StatementModel> = listOf()

    fun scanClassesFromPackage(pkgName: String): Iterable<Class<*>> {
        val result = mutableSetOf<Class<*>>()
        val reflections = Reflections(pkgName)
        reflections.store.storeMap.forEach {
            for (each in it.value.asMap()) {
                kotlin.runCatching { result.add(Class.forName(each.key)) }
            }
        }
        return result
    }

    fun scanClassesFromPackages(packages: Iterable<String>): Iterable<Class<*>> {
        val ret = mutableSetOf<Class<*>>()
        packages.forEach { ret.plus(scanClassesFromPackage(it)) }
        return ret
    }

    fun collectStatementsWithCache(
        targetClasses: Iterable<Class<*>>,
        cfg: ScannerConfig? = null
    ): List<StatementModel> {
        if (statementCache.isEmpty())
            statementCache = collectStatements(targetClasses, cfg)
        return statementCache
    }

    fun collectStatementsWithPackage(
        targetPackage: String,
        cfg: ScannerConfig? = null
    ): List<StatementModel> = collectStatements(scanClassesFromPackage(targetPackage), cfg)

    fun runWithTestFactory(
        targetClasses: Iterable<Class<*>>,
        scannerConfig: ScannerConfig? = null,
        envConfig: EnvConfig = EnvConfig()
    ): Iterable<DynamicTest> {
        val dynamicTests: MutableList<DynamicTest> = ArrayList()

        collectStatements(targetClasses, scannerConfig).forEach {
            val dynamicTest: DynamicTest = DynamicTest.dynamicTest(it.getDesc()) {
                try {
                    // todo: looks dynamicTest can not use some objects from outside
                    val env = NormalTestEnv(envConfig)
                    env.add(it)
                    env.start()
                    env.removeAll()
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

abstract class RandUnitBaseImpl {
    companion object {
        private val logger = Logger.getGlobal()
    }

    abstract fun getOperationManager(): OperationManager

    fun collectOperations(
        targetClasses: Set<Class<*>>,
        cfg: ScannerConfig? = null
    ): List<AbstractOperation> {
        val ret = mutableListOf<AbstractOperation>()

        class CustomScanner(cfg: ScannerConfig) : Scanner(cfg) {
            override fun beforeOperation(
                operation: AbstractOperation,
                operationManager: OperationManager
            ) {
                ret.add(operation)
            }
        }

        val finalCfg = cfg ?: ScannerConfig()
        val opm = getOperationManager()
        for (eachClazz in targetClasses)
            opm.addClazz(eachClazz)
        logger.info("start scanning with cfg: $finalCfg")
        CustomScanner(finalCfg).scanAll(opm)
        return ret
    }

    fun collectStatements(
        targetClasses: Iterable<Class<*>>,
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
        logger.info("start scanning with cfg: $finalCfg")
        CustomScanner(finalCfg).scanAll(opm)

        logger.info(ret.toJson())
        return ret
    }
}
