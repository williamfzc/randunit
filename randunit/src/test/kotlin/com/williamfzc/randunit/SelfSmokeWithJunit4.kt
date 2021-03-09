package com.williamfzc.randunit

import com.williamfzc.randunit.env.NormalTestEnv
import com.williamfzc.randunit.models.StatementModel
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SelfSmokeWIthJUnit4(val statementModel: StatementModel) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<StatementModel> {
            return RandUnit.collectStatements(setOf(RandUnit::class.java))
        }
    }

    @Test
    fun run() {
        NormalTestEnv().run(statementModel)
    }
}
