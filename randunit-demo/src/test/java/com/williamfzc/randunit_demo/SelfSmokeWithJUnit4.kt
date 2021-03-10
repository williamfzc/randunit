package com.williamfzc.randunit_demo

import com.williamfzc.randunit.RandUnitAndroid
import com.williamfzc.randunit.env.EnvConfig
import com.williamfzc.randunit.env.NormalTestEnv
import com.williamfzc.randunit.models.StatementModel
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config


@Config(sdk = [28])
@RunWith(ParameterizedRobolectricTestRunner::class)
class SelfSmokeWithJUnit4(private val statementModel: StatementModel) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun data(): Collection<StatementModel> {
            return RandUnitAndroid.collectStatements(setOf(MainActivity::class.java))
        }
    }

    @Test
    fun run() {
        val envConfig = EnvConfig(ignoreExceptions = setOf(IllegalStateException::class.java))
        val env = NormalTestEnv(envConfig)
        env.add(statementModel)
        env.start()
    }
}