package com.williamfzc.randunit.scanner

data class ScannerConfig(
    var filterType: Set<String> = setOf(),
    var statementLimit: Int = 1000,
    var includePrivateMethod: Boolean = false
)
