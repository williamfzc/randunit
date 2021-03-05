package com.williamfzc.randunit

import com.williamfzc.randunit.operations.OperationManager
import com.williamfzc.randunit.runner.Runner
import org.junit.Test


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
        opm.addClazz(AAA::class.java)
        Runner().run(opm)
    }
}