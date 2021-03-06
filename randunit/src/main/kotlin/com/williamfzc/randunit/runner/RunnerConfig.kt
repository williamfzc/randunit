package com.williamfzc.randunit.runner

import org.jeasy.random.EasyRandomParameters

data class RunnerConfig(
    val mockParameters: EasyRandomParameters? = null,
    val batchSize: Int = 10,
    val filterType: Set<String> = setOf()
)