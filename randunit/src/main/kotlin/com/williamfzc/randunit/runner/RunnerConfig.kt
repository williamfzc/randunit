package com.williamfzc.randunit.runner

import com.williamfzc.randunit.mock.MockConfig

data class RunnerConfig(
    var mockConfig: MockConfig = MockConfig(),
    var batchSize: Int = 2,
    var filterType: Set<String> = setOf(),
    var dryRun: Boolean = false,
    var statementLimit: Int = 10000,
    var includePrivateMethod: Boolean = false
)
