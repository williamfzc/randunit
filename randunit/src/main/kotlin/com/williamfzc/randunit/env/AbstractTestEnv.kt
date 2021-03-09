package com.williamfzc.randunit.env

import com.williamfzc.randunit.models.Statement

// todo: current statement == statement model
// env was designed for running inside something like TestCase/TestSuite
// which can be used by different runner
// it manages:
// - statement model (s)
// - about how to gen runnable statement from statement model
abstract class AbstractTestEnv {
    abstract fun run(statement: Statement)
}
