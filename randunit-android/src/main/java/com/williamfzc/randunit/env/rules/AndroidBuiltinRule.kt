package com.williamfzc.randunit.env.rules

import com.williamfzc.randunit.env.Statement
import com.williamfzc.randunit.extensions.isRobolectricError

class AndroidBuiltinRule : BuiltinRule() {
    override fun judge(statement: Statement, e: Throwable): Boolean {
        if (e.isRobolectricError())
            return e !is ClassNotFoundException
        return super.judge(statement, e)
    }
}
