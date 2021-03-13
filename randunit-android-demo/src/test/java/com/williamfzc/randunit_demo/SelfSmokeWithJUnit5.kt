package com.williamfzc.randunit_demo

import com.williamfzc.randunit.operations.AndroidOperationManager
import com.williamfzc.randunit.scanner.Scanner
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@Config(sdk = [29])
@RunWith(RobolectricTestRunner::class)
class SelfSmokeWithJUnit5 {
    @Test
    fun scanSimple() {
        val opm = AndroidOperationManager()
        opm.addClazz(MainActivity::class.java)
        Scanner().scanAll(opm)
    }
}