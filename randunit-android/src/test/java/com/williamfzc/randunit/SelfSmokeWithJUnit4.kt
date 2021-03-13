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
package com.williamfzc.randunit

import com.williamfzc.randunit.env.EnvConfig
import com.williamfzc.randunit.env.NormalTestEnv
import com.williamfzc.randunit.models.StatementModel
import com.williamfzc.randunit.operations.*
import com.williamfzc.randunit.scanner.Scanner
import com.williamfzc.randunit.scanner.ScannerConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [28])
@RunWith(ParameterizedRobolectricTestRunner::class)
class SelfSmokeWithJUnit4(private val statementModel: StatementModel) {
    companion object {
        private val envConfig = EnvConfig(ignoreExceptions = setOf(IllegalStateException::class.java))
        private val testEnv = NormalTestEnv(envConfig)

        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun data(): Collection<StatementModel> {
            val scannerConfig = ScannerConfig(
                includeFilter = setOf("com.williamfzc.randunit"),
                excludeFilter = setOf("org.jeasy"),
                excludeMethodFilter = setOf("toJson"),
                recursively = true,
                includePrivateMethod = true
            )
            val clzSet = setOf(
                Scanner::class.java,
                AndroidOperationManager::class.java,
                ActivityOperation::class.java,
                ServiceOperation::class.java,
                ContentProviderOperation::class.java,
                OtherAndroidOperation::class.java,
                NormalOperation::class.java,
                RandUnitAndroid::class.java
            )

            return RandUnitAndroid.collectStatements(
                clzSet,
                scannerConfig
            )
        }
    }

    @Test
    fun run() {
        testEnv.add(statementModel)
        testEnv.start()
        testEnv.removeAll()
    }
}
