package com.williamfzc.randunit.models

import com.williamfzc.randunit.mock.AbstractMocker
import com.williamfzc.randunit.mock.EasyRandomMocker
import com.williamfzc.randunit.mock.MockConfig
import com.williamfzc.randunit.mock.MockkMocker
import java.lang.Exception
import java.util.logging.Logger

class MockModel(mockConfig: MockConfig) {
    companion object {
        private val logger = Logger.getLogger("MockModel")
    }

    private val mockerList = mutableListOf<AbstractMocker>()
    init {
        mockerList.add(EasyRandomMocker(mockConfig))
        mockerList.add(MockkMocker(mockConfig))
    }

    fun <T : Any> mock(t: Class<T>): Any? {
        for (eachMocker in mockerList) {
            try {
                val m = eachMocker.mock(t)
                logger.info("mock type $t finished by $eachMocker")
                return m
            } catch (e: Exception) {
                logger.warning("mock type $t failed: $e")
                continue
            }
        }
        return null
    }
}
