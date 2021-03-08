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

    fun runWithTestFactory(
        targetClasses: Set<Class<*>>,
        cfg: ScannerConfig? = null
    ): Stream<DynamicTest> {
        val dynamicTests: MutableList<DynamicTest> = ArrayList()

        class CustomScanner(cfg: ScannerConfig) : Scanner(cfg) {
            override fun handle(statement: Statement) {
                val dynamicTest: DynamicTest = DynamicTest.dynamicTest(statement.getName()) {
                    try {
                        statement.exec()
                    } catch (e: Exception) {
                        if ((e.cause !is MockKException).and(e.cause !is UninitializedPropertyAccessException))
                            throw e
                    }
                }
                dynamicTests.add(dynamicTest)
            }
        }

        val finalCfg = cfg ?: ScannerConfig()

        val opm = getOperationManager()
        for (eachClazz in targetClasses)
            opm.addClazz(eachClazz)
        CustomScanner(finalCfg).scanAll(opm)

        return dynamicTests.stream()
    }
}
