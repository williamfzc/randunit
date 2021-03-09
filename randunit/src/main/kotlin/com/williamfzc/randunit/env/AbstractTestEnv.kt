package com.williamfzc.randunit.env

import com.williamfzc.randunit.models.Statement

abstract class AbstractTestEnv {
    abstract fun run(statement: Statement)
}
