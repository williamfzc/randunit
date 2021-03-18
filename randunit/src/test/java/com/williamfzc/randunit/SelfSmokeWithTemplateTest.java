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
package com.williamfzc.randunit;

import com.williamfzc.randunit.cases.RUJUnit4Case;
import com.williamfzc.randunit.env.Statement;
import com.williamfzc.randunit.env.rules.AbstractRule;
import com.williamfzc.randunit.mock.MockkMocker;
import com.williamfzc.randunit.models.StatementModel;

import org.jetbrains.annotations.NotNull;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.HashSet;

@RunWith(Parameterized.class)
public class SelfSmokeWithTemplateTest extends RUJUnit4Case {
    public SelfSmokeWithTemplateTest(StatementModel sm) {
        super(sm);
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
        targetClasses.add(RandUnit.class);
        targetClasses.add(MockkMocker.class);
        scannerConfig.setIncludeFilter(new HashSet<String>() {{
            add("com.williamfzc.randunit");
        }});
        // causes by java
        scannerConfig.setExcludeMethodFilter(new HashSet<String>() {{
            add("toJson");
        }});
        testEnv.getEnvConfig().getSandboxConfig().getRules().add(new CustomRule());
        return RandUnit.INSTANCE.collectStatementsWithPackage("com.williamfzc.randunit", scannerConfig);
    }
}
