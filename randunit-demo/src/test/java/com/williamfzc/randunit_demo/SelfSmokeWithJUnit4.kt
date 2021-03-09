package com.williamfzc.randunit_demo

import com.williamfzc.randunit.RandUnitAndroid
import com.williamfzc.randunit.env.SimpleTestEnv
import com.williamfzc.randunit.models.Statement
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config


@Config(sdk = [28])
@RunWith(ParameterizedRobolectricTestRunner::class)
class SelfSmokeWithJUnit4(val statement: Statement) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun data(): Collection<Statement> {
            return RandUnitAndroid.collectStatements(setOf(MainActivity::class.java))
        }
    }

    @Test
    fun run() {
        val env = SimpleTestEnv()
        env.add(statement)
        env.start()
    }
}