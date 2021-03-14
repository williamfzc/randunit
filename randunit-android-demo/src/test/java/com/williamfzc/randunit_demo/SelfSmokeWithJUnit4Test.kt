package com.williamfzc.randunit_demo

import com.williamfzc.randunit.RandUnitAndroid
import com.williamfzc.randunit.env.EnvConfig
import com.williamfzc.randunit.env.NormalTestEnv
import com.williamfzc.randunit.models.StatementModel
import com.williamfzc.randunit.scanner.ScannerConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config


@Config(sdk = [29])
@RunWith(ParameterizedRobolectricTestRunner::class)
class SelfSmokeWithJUnit4Test(private val statementModel: StatementModel) {
    companion object {
        private val envConfig = EnvConfig(ignoreExceptions = setOf(IllegalStateException::class.java))
        val env = NormalTestEnv(envConfig)

        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun data(): Collection<StatementModel> {
            val scannerConfig = ScannerConfig(
                includeFilter = setOf("com.williamfzc.randunit_demo"),
                excludeFilter = setOf("org.jeasy"),
                recursively = true,
                includePrivateMethod = true
            )

            return RandUnitAndroid.collectStatements(setOf(MainActivity::class.java), scannerConfig)
        }
    }

    @Test
    fun run() {
        env.add(statementModel)
        env.start()
        env.removeAll()
    }
}