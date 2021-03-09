package com.williamfzc.randunit

import com.williamfzc.randunit.operations.OperationManager
import com.williamfzc.randunit.scanner.Scanner
import com.williamfzc.randunit.testres.AAA
import com.williamfzc.randunit.testres.CCC
import org.junit.Test
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.util.stream.Stream

class SelfSmokeUnitTest {
    @Test
    fun scanSimple() {
        val opm = OperationManager()
        opm.addClazz(CCC::class.java)
        opm.addClazz(AAA::class.java)
        Scanner().scanAll(opm)
    }

    @TestFactory
    fun scanAndRun(): Stream<DynamicTest>? {
        val clsSet = setOf(CCC::class.java, AAA::class.java)
        return RandUnit.runWithTestFactory(clsSet)
    }

    @TestFactory
    fun scanItself(): Stream<DynamicTest>? {
        val clsSet = setOf(Scanner::class.java)
        return RandUnit.runWithTestFactory(clsSet)
    }
}
