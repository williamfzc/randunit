package com.williamfzc.randunit

import com.williamfzc.randunit.env.SimpleTestEnv
import com.williamfzc.randunit.models.Statement
import com.williamfzc.randunit.operations.OperationManager
import com.williamfzc.randunit.scanner.Scanner
import com.williamfzc.randunit.scanner.ScannerConfig
import io.mockk.MockKException
import org.junit.jupiter.api.DynamicTest
import java.lang.Exception
import java.util.logging.Logger
import java.util.stream.Stream

abstract class RandUnitBase {
    private val logger = Logger.getLogger("RandUnit")
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
        logger.info("scan finished, statements count: ${ret.size}")
        return ret
    }

    fun runWithTestFactory(
        targetClasses: Set<Class<*>>,
        cfg: ScannerConfig? = null
    ): Stream<DynamicTest> {
        val dynamicTests: MutableList<DynamicTest> = ArrayList()

        collectStatements(targetClasses, cfg).forEach {
            // each statement will be executed at least twice
            // make it a config soon
            val env = SimpleTestEnv()
            for (each in 1..2) {
                val dynamicTest: DynamicTest = DynamicTest.dynamicTest(it.getName()) {
                    try {
                        env.run(it)
                    } catch (e: Exception) {
                        if ((e.cause !is MockKException).and(e.cause !is UninitializedPropertyAccessException))
                            throw e
                    }
                }
                dynamicTests.add(dynamicTest)
            }
        }

        return dynamicTests.stream()
    }
}
