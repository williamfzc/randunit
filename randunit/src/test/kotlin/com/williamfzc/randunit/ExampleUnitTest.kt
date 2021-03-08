package com.williamfzc.randunit

import com.williamfzc.randunit.operations.OperationManager
import com.williamfzc.randunit.scanner.Scanner
import org.junit.Test
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream

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
        opm.addClazz(Scanner::class.java)
        Scanner().scanAll(opm)
    }

    @TestFactory
    fun withJunit(): Stream<DynamicTest>? {
        val clsSet = setOf(Scanner::class.java)
        return RandUnit.runWithTestFactory(clsSet)
    }
}
