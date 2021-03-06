package com.williamfzc.randunit_demo

import com.williamfzc.randunit.operations.AndroidOperationManager
import com.williamfzc.randunit.operations.OperationManager
import com.williamfzc.randunit.runner.Runner
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


class AAA {
    fun a() {}
    fun b(b: String?) {}
    fun c(): String {
        return "aaa"
    }

    fun d(b: BBB?) {}
}

class BBB

@Config(sdk=[28])
@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val opm = AndroidOperationManager()
        opm.addClazz(MainActivity::class.java)
        Runner().run(opm)
    }
}