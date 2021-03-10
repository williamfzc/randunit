package com.williamfzc.randunit.env

import com.williamfzc.randunit.mock.MockConfig
import java.lang.Exception

class EnvConfig(
    var mockConfig: MockConfig = MockConfig(),
    var ignoreExceptions: Set<Class<out Exception>> = setOf()
)
