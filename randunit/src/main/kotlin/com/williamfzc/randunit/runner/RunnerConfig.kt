package com.williamfzc.randunit.runner

import org.jeasy.random.EasyRandomParameters

data class RunnerConfig(
    val mockParameters: EasyRandomParameters? = null,
    val batchSize: Int = 2,
    val filterType: Set<String> = setOf(),
    val dryRun: Boolean = false,
    val statementLimit: Int = 1000,
    val includePrivateMethod: Boolean = false
)
