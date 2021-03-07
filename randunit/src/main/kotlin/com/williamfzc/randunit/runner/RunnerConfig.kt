package com.williamfzc.randunit.runner

import org.jeasy.random.EasyRandomParameters

data class RunnerConfig(
    val mockParameters: EasyRandomParameters? = null,
    val batchSize: Int = 5,
    val filterType: Set<String> = setOf(),
    val dryRun: Boolean = false,
    val statementLimit: Int = 1000
)
