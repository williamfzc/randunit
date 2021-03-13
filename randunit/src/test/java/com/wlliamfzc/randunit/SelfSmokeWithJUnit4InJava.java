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

package com.wlliamfzc.randunit;

import com.williamfzc.randunit.RandUnit;
import com.williamfzc.randunit.env.AbstractTestEnv;
import com.williamfzc.randunit.env.NormalTestEnv;
import com.williamfzc.randunit.models.StatementModel;
import com.williamfzc.randunit.scanner.ScannerConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RunWith(Parameterized.class)
public class SelfSmokeWithJUnit4InJava {
    private final StatementModel sm;

    public SelfSmokeWithJUnit4InJava(StatementModel s) {
        sm = s;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<StatementModel> data() {
        Set<String> includeFilter = new HashSet<>();
        includeFilter.add("com.williamfzc.randunit");
        Set<String> empty = new HashSet<>();

        ScannerConfig scannerConfig = new ScannerConfig(
                includeFilter,
                empty,
                empty,
                20,
                false,
                true);

        Set<Class<?>> clzSet = new HashSet<>();
        clzSet.add(RandUnit.class);
        return RandUnit.INSTANCE.collectStatements(clzSet, scannerConfig);
    }

    @Test
    public void run() {
        AbstractTestEnv env = new NormalTestEnv();
        env.add(sm);
        env.start();
    }
}
