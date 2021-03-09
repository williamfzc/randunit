package com.williamfzc.randunit_demo

import com.williamfzc.randunit.RandUnitAndroid
import com.williamfzc.randunit.operations.AndroidOperationManager
import com.williamfzc.randunit.scanner.Scanner
import org.junit.Test
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.stream.Stream


@Config(sdk = [28])
@RunWith(RobolectricTestRunner::class)
class SelfSmokeUnitTest {
    @Test
    fun scanSimple() {
        val opm = AndroidOperationManager()
        opm.addClazz(MainActivity::class.java)
        Scanner().scanAll(opm)
    }

    @TestFactory
    fun scanAndTest(): Stream<DynamicTest> {
        val clsSet = setOf(MainActivity::class.java)
        return RandUnitAndroid.runWithTestFactory(clsSet)
    }
}