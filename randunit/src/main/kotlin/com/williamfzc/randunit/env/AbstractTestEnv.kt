/**
 * Copyright 2021 williamfzc
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.williamfzc.randunit.env

import com.williamfzc.randunit.env.sandbox.Sandbox
import com.williamfzc.randunit.env.strategy.AbstractStrategy
import com.williamfzc.randunit.models.MockModel
import com.williamfzc.randunit.models.StatementModel

// env was designed for running inside something like TestCase/TestSuite
// which can be used by different runner
// it manages:
// - statement model (s)
// - about how to gen runnable statement from statement model
abstract class AbstractTestEnv @JvmOverloads constructor(var envConfig: EnvConfig = EnvConfig()) {
    var mockModel = MockModel(envConfig.mockConfig)
    private val strategy: AbstractStrategy by lazy {
        envConfig.strategy.kotlin.objectInstance ?: envConfig.strategy.newInstance()
    }

    open fun prepareEnv() {}
    open fun beforeRun() {}
    open fun beforeEachRun(statementModel: StatementModel) {}
    abstract fun runStatement(statement: Statement)
    open fun afterEachRun(statementModel: StatementModel) {}
    open fun afterRun() {}

    private val statementModels = mutableListOf<StatementModel>()

    fun add(statementModel: StatementModel) = statementModels.add(statementModel)

    fun addAll(smList: Iterable<StatementModel>) = smList.forEach { add(it) }

    fun removeAll() = statementModels.clear()

    fun start() {
        prepareEnv()
        beforeRun()
        // loop will not drop anything
        // because maybe it will be reused after that
        for (each in statementModels) {
            beforeEachRun(each)
            runStatementInSandbox(each)
            afterEachRun(each)
        }
        afterRun()
    }

    fun runStatementInSandbox(statementModel: StatementModel) {
        // sandbox should always be safe
        val sandbox = Sandbox(envConfig.sandboxConfig)
        strategy.genStatements(statementModel, mockModel).forEach { each ->
            sandbox.runSafely(each, ::runStatement)?.let { err ->
                // env make the decision
                throw err
            }
        }
    }
}
