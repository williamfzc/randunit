package com.williamfzc.randunit;

import com.williamfzc.randunit.env.AbstractTestEnv;
import com.williamfzc.randunit.env.EnvConfig;
import com.williamfzc.randunit.env.NormalTestEnv;
import com.williamfzc.randunit.models.StatementModel;
import com.williamfzc.randunit.scanner.ScannerConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Config(sdk = {28})
@RunWith(ParameterizedRobolectricTestRunner.class)
public class SelfSmokeWithJUnit4InJava {
    private final StatementModel sm;
    private static final AbstractTestEnv testEnv = new NormalTestEnv();

    public SelfSmokeWithJUnit4InJava(StatementModel s) {
        sm = s;
    }

    @ParameterizedRobolectricTestRunner.Parameters
    public static Collection<StatementModel> data() {
        Set<String> includeFilter = new HashSet<>();
        includeFilter.add("com.williamfzc.randunit");

        Set<String> excludeMethodFilter = new HashSet<>();
        excludeMethodFilter.add("toJson");
        Set<String> empty = new HashSet<>();

        ScannerConfig scannerConfig = new ScannerConfig(includeFilter, empty, excludeMethodFilter, 500, false, true);

        Set<Class<?>> clzSet = new HashSet<>();
        clzSet.add(RandUnitAndroid.class);
        return RandUnitAndroid.INSTANCE.collectStatements(clzSet, scannerConfig);
    }

    @Test
    public void run() {
        testEnv.add(sm);
        testEnv.start();
        testEnv.removeAll();
    }
}
