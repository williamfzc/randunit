package com.williamfzc.randunit

import com.williamfzc.randunit.models.Statement
import com.williamfzc.randunit.operations.OperationManager
import com.williamfzc.randunit.runner.Runner
import com.williamfzc.randunit.runner.RunnerConfig
import org.junit.Test
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream
import kotlin.collections.ArrayList

class AAA {
    fun a() {}
    fun b(b: String?) {}
    fun c(): String {
        return "aaa"
    }

    fun d(b: BBB?) {}
}

class BBB


class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val opm = OperationManager()
        opm.addClazz(Runner::class.java)
        Runner().runAll(opm)
    }

    @TestFactory
    fun withJunit(): Stream<DynamicTest>? {
        val dynamicTests: MutableList<DynamicTest> = ArrayList()

        class CustomRunner(cfg: RunnerConfig) : Runner(cfg) {
            override fun beforeExec(statement: Statement) {
                super.beforeExec(statement)
                val dynamicTest: DynamicTest = dynamicTest("hahah") {
                    statement.exec()
                }
                dynamicTests.add(dynamicTest)
            }
        }

        val cfg = RunnerConfig(dryRun = true)

        val opm = OperationManager()
        opm.addClazz(Runner::class.java)
        CustomRunner(cfg).runAll(opm)

        return dynamicTests.stream()
    }
}
