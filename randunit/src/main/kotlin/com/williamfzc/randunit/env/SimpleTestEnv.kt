package com.williamfzc.randunit.env

import com.williamfzc.randunit.models.Statement

class SimpleTestEnv : AbstractTestEnv() {
    override fun run(statement: Statement) {
        statement.exec()
    }
}