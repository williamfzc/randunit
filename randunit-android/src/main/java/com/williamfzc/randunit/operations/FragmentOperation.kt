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

package com.williamfzc.randunit.operations

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.Lifecycle

class FragmentOperation : AbstractAndroidOperation() {
    override fun getInstance(): Any {
        val scenario = FragmentScenario.launchInContainer<Fragment>(type as Class<Fragment>)
        scenario.moveToState(Lifecycle.State.CREATED)
        var fragment: Fragment? = null
        scenario.onFragment {
            fragment = it
        }
        return fragment!!
    }
}
