package com.williamfzc.randunit.env

import com.williamfzc.randunit.mock.MockConfig

data class EnvConfig(
    var mockConfig: MockConfig = MockConfig()
)
