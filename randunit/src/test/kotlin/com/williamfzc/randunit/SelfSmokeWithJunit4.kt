package com.williamfzc.randunit

import com.williamfzc.randunit.env.SimpleTestEnv
import com.williamfzc.randunit.models.Statement
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SelfSmokeWIthJUnit4(val statement: Statement) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Statement> {
            return RandUnit.collectStatements(setOf(RandUnit::class.java))
        }
    }

    @Test
    fun run() {
        SimpleTestEnv().run(statement)
    }
}
