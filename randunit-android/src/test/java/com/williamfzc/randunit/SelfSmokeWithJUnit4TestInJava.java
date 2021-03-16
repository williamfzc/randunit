package com.williamfzc.randunit;

import com.williamfzc.randunit.env.AbstractTestEnv;
import com.williamfzc.randunit.env.NormalTestEnv;
import com.williamfzc.randunit.env.rules.AbstractRule;
import com.williamfzc.randunit.models.StatementModel;
import com.williamfzc.randunit.scanner.ScannerConfig;

import org.jetbrains.annotations.NotNull;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Config(sdk = {28})
@RunWith(ParameterizedRobolectricTestRunner.class)
public class SelfSmokeWithJUnit4TestInJava {
    private final StatementModel sm;
    private static final AbstractTestEnv testEnv = new NormalTestEnv();

    static class CustomRule extends AbstractRule {
        @Override
        public boolean judge(@NotNull StatementModel statementModel, @NotNull Throwable e) {
            return (e instanceof IllegalArgumentException) ||
                    (e instanceof UnsupportedOperationException) ||
                    (e instanceof InternalError);
        }
    }

    public SelfSmokeWithJUnit4TestInJava(StatementModel s) {
        sm = s;
    }

    static {
        RandUnitAndroid.INSTANCE.cleanCache();
    }

    @ParameterizedRobolectricTestRunner.Parameters
    public static Collection<StatementModel> data() {
        Set<String> includeFilter = new HashSet<>();
        includeFilter.add("com.williamfzc.randunit");

        testEnv.getEnvConfig().getSandboxConfig().getRules().add(new CustomRule());

        Set<String> excludeMethodFilter = new HashSet<>();
        excludeMethodFilter.add("toJson");
        Set<String> empty = new HashSet<>();

        ScannerConfig scannerConfig = new ScannerConfig(includeFilter, empty, excludeMethodFilter, 500, false, true);

        Set<Class<?>> clzSet = new HashSet<>();
        clzSet.add(RandUnitAndroid.class);
        return RandUnitAndroid.INSTANCE.collectStatementsWithCache(clzSet, scannerConfig);
    }

    @Test
    public void run() {
        testEnv.runWithSandbox(sm);
    }
}
