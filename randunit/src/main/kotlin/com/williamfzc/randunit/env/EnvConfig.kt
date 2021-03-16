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
package com.williamfzc.randunit.env

import com.williamfzc.randunit.env.sandbox.SandboxConfig
import com.williamfzc.randunit.mock.MockConfig
import java.lang.Exception

data class EnvConfig @JvmOverloads constructor(
    var mockConfig: MockConfig = MockConfig(),
    var ignoreExceptions: MutableSet<Class<out Exception>> = mutableSetOf(),
    var reuseCaller: Boolean = false,
    var sandboxConfig: SandboxConfig = SandboxConfig()
)
