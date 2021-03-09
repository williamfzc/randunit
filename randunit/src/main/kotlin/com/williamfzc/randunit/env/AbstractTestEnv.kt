package com.williamfzc.randunit.env

import com.williamfzc.randunit.models.MockModel
import com.williamfzc.randunit.models.Statement

// todo: current statement == statement model
// env was designed for running inside something like TestCase/TestSuite
// which can be used by different runner
// it manages:
// - statement model (s)
// - about how to gen runnable statement from statement model
abstract class AbstractTestEnv(envConfig: EnvConfig = EnvConfig()) {
    val mockModel = MockModel(envConfig.mockConfig)

    open fun prepareEnv() {}
    open fun beforeRun() {}
    open fun beforeEachRun(statement: Statement) {}
    abstract fun run(statement: Statement)
    open fun afterEachRun(statement: Statement) {}
    open fun afterRun() {}

    private val statements = mutableListOf<Statement>()

    fun add(statement: Statement) = statements.add(statement)

    fun start() {
        prepareEnv()
        beforeRun()
        for (each in statements) {
            beforeEachRun(each)
            run(each)
            afterEachRun(each)
        }
        afterRun()
    }
}
