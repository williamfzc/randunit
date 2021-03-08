package com.williamfzc.randunit.mock

abstract class AbstractMocker(val cfg: MockConfig) {
    abstract fun mock(t: Class<*>): Any
}
