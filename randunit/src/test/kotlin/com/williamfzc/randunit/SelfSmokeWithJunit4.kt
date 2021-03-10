package com.williamfzc.randunit

import com.williamfzc.randunit.env.EnvConfig
import com.williamfzc.randunit.env.NormalTestEnv
import com.williamfzc.randunit.mock.MockkMocker
import com.williamfzc.randunit.models.StatementModel
import com.williamfzc.randunit.scanner.Scanner
import com.williamfzc.randunit.scanner.ScannerConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SelfSmokeWIthJUnit4(val statementModel: StatementModel) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<StatementModel> {
            val scannerConfig = ScannerConfig(includePrivateMethod = true)
            return RandUnit.collectStatements(
                setOf(
                    RandUnit::class.java,
                    NormalTestEnv::class.java,
                    Scanner::class.java,
                    MockkMocker::class.java,
                    RandUnitBase::class.java,
                    EnvConfig::class.java
                ),
                scannerConfig
            )
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
