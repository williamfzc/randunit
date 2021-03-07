package com.williamfzc.randunit.runner

import org.jeasy.random.EasyRandomParameters

data class RunnerConfig(
    var mockParameters: EasyRandomParameters? = null,
    var batchSize: Int = 2,
    var filterType: Set<String> = setOf(),
    var dryRun: Boolean = false,
    var statementLimit: Int = 1000,
    var includePrivateMethod: Boolean = false
)
