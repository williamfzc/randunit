/*
 * Copyright 2021 williamfzc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.williamfzc.randunit.mock

import android.content.Context
import com.williamfzc.randunit.exceptions.RUMockException
import org.robolectric.RuntimeEnvironment

class AndroidMocker(cfg: MockConfig) : AbstractMocker(cfg) {
    override fun mock(t: Class<*>): Any {
        return when {
            Context::class.java.isAssignableFrom(t) -> RuntimeEnvironment.systemContext
            else -> throw RUMockException("failed to mock with robolectric: $t")
        }
    }
}
