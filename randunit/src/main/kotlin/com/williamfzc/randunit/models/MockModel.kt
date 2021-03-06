package com.williamfzc.randunit.models

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters

class MockModel(easyRandomParameters: EasyRandomParameters? = null) {
    private val easyRandom =
        if (null == easyRandomParameters) EasyRandom()
        else EasyRandom(easyRandomParameters)

    fun <T> mock(t: Class<T>): T {
        return easyRandom.nextObject(t)
    }
}
