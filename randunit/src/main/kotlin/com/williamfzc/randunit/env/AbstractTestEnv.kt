package com.williamfzc.randunit.env

import com.williamfzc.randunit.models.MockModel
import com.williamfzc.randunit.models.StatementModel

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
    open fun beforeEachRun(statementModel: StatementModel) {}
    abstract fun run(statementModel: StatementModel)
    open fun afterEachRun(statementModel: StatementModel) {}
    open fun afterRun() {}

    private val statementModels = mutableListOf<StatementModel>()

    fun add(statementModel: StatementModel) = statementModels.add(statementModel)

    fun start() {
        prepareEnv()
        beforeRun()
        for (each in statementModels) {
            beforeEachRun(each)
            run(each)
            afterEachRun(each)
        }
        afterRun()
    }
}
