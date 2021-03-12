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
import com.williamfzc.randunit.RandUnit
import com.williamfzc.randunit.env.EnvConfig
import com.williamfzc.randunit.env.NormalTestEnv
import com.williamfzc.randunit.mock.MockConfig
import com.williamfzc.randunit.models.StatementModel
import com.williamfzc.randunit.scanner.ScannerConfig
import com.williamfzc.randunit_demo.HereIsAClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SmokeTest(val statementModel: StatementModel) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<StatementModel> {
            val scannerConfig = ScannerConfig(
                includeFilter = setOf("com.williamfzc.randunit_demo"),
                recursively = true,
                includePrivateMethod = true
            )
            return RandUnit.collectStatements(
                setOf(
                    HereIsAClass::class.java,
                ),
                scannerConfig
            )
        }
    }

    @Test
    fun run() {
        val mockConfig = MockConfig(ktFirst = true)
        val envConfig =
            EnvConfig(mockConfig, ignoreExceptions = setOf(IllegalStateException::class.java))
        val env = NormalTestEnv(envConfig)
        env.add(statementModel)
        env.start()
    }
}
