package com.williamfzc.randunit

import com.williamfzc.randunit.models.Statement
import com.williamfzc.randunit.operations.OperationManager
import com.williamfzc.randunit.scanner.Scanner
import com.williamfzc.randunit.scanner.ScannerConfig
import io.mockk.MockKException
import org.junit.jupiter.api.DynamicTest
import java.lang.Exception
import java.util.stream.Stream

abstract class RandUnitBase {
    abstract fun getOperationManager(): OperationManager

    fun collectStatements(
        targetClasses: Set<Class<*>>,
        cfg: ScannerConfig? = null
    ): List<Statement> {
        val ret = mutableListOf<Statement>()

        class CustomScanner(cfg: ScannerConfig) : Scanner(cfg) {
            override fun handle(statement: Statement) {
                ret.add(statement)
            }
        }

        val finalCfg = cfg ?: ScannerConfig()
        val opm = getOperationManager()
        for (eachClazz in targetClasses)
            opm.addClazz(eachClazz)
        CustomScanner(finalCfg).scanAll(opm)
        return ret
    }

    fun runWithTestFactory(
        targetClasses: Set<Class<*>>,
        cfg: ScannerConfig? = null
    ): Stream<DynamicTest> {
        val dynamicTests: MutableList<DynamicTest> = ArrayList()

        collectStatements(targetClasses, cfg).forEach {
            val dynamicTest: DynamicTest = DynamicTest.dynamicTest(it.getName()) {
                try {
                    it.exec()
                } catch (e: Exception) {
                    if ((e.cause !is MockKException).and(e.cause !is UninitializedPropertyAccessException))
                        throw e
                }
            }
            dynamicTests.add(dynamicTest)
        }

        return dynamicTests.stream()
    }
}
