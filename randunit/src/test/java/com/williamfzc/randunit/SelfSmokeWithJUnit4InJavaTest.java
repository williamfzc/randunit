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

package com.williamfzc.randunit;

import com.williamfzc.randunit.env.AbstractTestEnv;
import com.williamfzc.randunit.env.NormalTestEnv;
import com.williamfzc.randunit.env.Statement;
import com.williamfzc.randunit.env.rules.AbstractRule;
import com.williamfzc.randunit.models.StatementModel;
import com.williamfzc.randunit.scanner.ScannerConfig;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RunWith(Parameterized.class)
public class SelfSmokeWithJUnit4InJavaTest {
    private final StatementModel sm;
    private static final AbstractTestEnv testEnv = new NormalTestEnv();

    public SelfSmokeWithJUnit4InJavaTest(StatementModel s) {
        sm = s;
    }

    static class CustomRule extends AbstractRule {
        @Override
        public boolean judge(@NotNull Statement statement, @NotNull Throwable e) {
            return (e instanceof IllegalArgumentException) ||
                    (e instanceof UnsupportedOperationException) ||
                    (e instanceof InternalError) ||
                    (e instanceof NullPointerException);
        }
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<StatementModel> data() {
        Set<String> includeFilter = new HashSet<>();
        includeFilter.add("com.williamfzc.randunit");
        Set<String> empty = new HashSet<>();
        Set<String> excludeMethodFilter = new HashSet<>();
        excludeMethodFilter.add("toJson");

        ScannerConfig scannerConfig = new ScannerConfig(
                includeFilter,
                empty,
                excludeMethodFilter);

        Set<Class<?>> clzSet = new HashSet<>();
        clzSet.add(RandUnit.class);
        testEnv.getEnvConfig().getSandboxConfig().getRules().add(new CustomRule());
        return RandUnit.INSTANCE.collectStatements(clzSet, scannerConfig);
    }

    @Test
    public void run() {
        testEnv.runStatementInSandbox(sm);
    }
}
