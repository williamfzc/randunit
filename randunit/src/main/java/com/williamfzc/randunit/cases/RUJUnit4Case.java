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
package com.williamfzc.randunit.cases;

import com.williamfzc.randunit.RandUnit;
import com.williamfzc.randunit.env.AbstractTestEnv;
import com.williamfzc.randunit.env.NormalTestEnv;
import com.williamfzc.randunit.models.StatementModel;
import com.williamfzc.randunit.scanner.ScannerConfig;

import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

abstract public class RUJUnit4Case {
    protected final StatementModel sm;

    protected static final AbstractTestEnv testEnv = new NormalTestEnv();
    protected static ScannerConfig scannerConfig = new ScannerConfig();
    protected static Set<Class<?>> targetClasses = new HashSet<>();

    public RUJUnit4Case(StatementModel sm) {
        this.sm = sm;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<StatementModel> data() {
        return RandUnit.INSTANCE.collectStatements(targetClasses, scannerConfig);
    }

    @Test
    public void runStatements() {
        testEnv.runStatementInSandbox(sm);
    }
}
