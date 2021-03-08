package com.williamfzc.randunit.mock

import io.mockk.mockkClass
import io.mockk.mockkObject

class MockkMocker(cfg: MockConfig) : AbstractMocker(cfg) {
    override fun mock(t: Class<*>): Any {
        val kotlinT = t.kotlin
        kotlinT.objectInstance?.let {
            return mockkObject(it)
        } ?: run {
            return mockkClass(kotlinT)
        }
    }
}