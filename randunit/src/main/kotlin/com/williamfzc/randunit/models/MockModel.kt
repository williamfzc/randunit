/**
 * Copyright 2021 williamfzc
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.williamfzc.randunit.models

import com.williamfzc.randunit.mock.*
import java.lang.Exception
import java.util.logging.Logger

open class MockModel(mockConfig: MockConfig) {
    companion object {
        private val logger = Logger.getLogger("MockModel")
    }

    private val mockerList = mutableListOf<AbstractMocker>()
    init {
        mockerList.add(MockitoMocker(mockConfig))
        mockerList.add(EasyRandomMocker(mockConfig))
        if (mockConfig.ktFirst)
            mockerList.add(0, MockkMocker(mockConfig))
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

object DefaultMocker : MockModel(MockConfig())
