package com.williamfzc.randunit.scanner

import com.williamfzc.randunit.mock.MockConfig

data class ScannerConfig(
    var mockConfig: MockConfig = MockConfig(),
    var filterType: Set<String> = setOf(),
    var statementLimit: Int = 10000,
    var includePrivateMethod: Boolean = false
)
