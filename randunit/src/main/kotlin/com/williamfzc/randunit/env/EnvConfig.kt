package com.williamfzc.randunit.env

import com.williamfzc.randunit.mock.MockConfig
import java.lang.Exception

data class EnvConfig(
    var mockConfig: MockConfig = MockConfig(),
    var ignoreException: Set<Class<out Exception>> = setOf()
)
