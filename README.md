# randunit

[![ci on jdk8,11](https://github.com/williamfzc/randunit/workflows/Android%20CI/badge.svg)](https://github.com/williamfzc/randunit)
[![codecov](https://codecov.io/gh/williamfzc/randunit/branch/main/graph/badge.svg?token=FNCFFQFVP8)](https://codecov.io/gh/williamfzc/randunit)
[![](https://jitpack.io/v/williamfzc/randunit.svg)](https://jitpack.io/#williamfzc/randunit)

> Build Android/JVM applications with confidence and less effort.

## what's it?

RandUnit means `Random UnitTest`, which will:

- scan all the classes in your project by package name or class name
- pick, generate serious of statements
- run them with junit 

All you need is a simple copy-paste:

```kotlin
@RunWith(Parameterized::class)
class SelfSmokeWithJUnit4Test(private val statementModel: StatementModel) {
    companion object {
        private val testEnv = NormalTestEnv()
        private const val packageName = "com.your.package"

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<StatementModel> {
            val scannerConfig = ScannerConfig()
            scannerConfig.includeFilter.add(packageName)

            return RandUnit.collectStatementsWithPackage(packageName, scannerConfig)
        }
    }

    @Test
    fun runStatements() {
        testEnv.runWithSandbox(statementModel)
    }
}
```

And you get a ready-to-use smoke test suite:

![ide](./docs/pics/ide.jpg)

## installation

randunit released on [jitpack.io/#williamfzc/randunit](https://jitpack.io/#williamfzc/randunit).

normal java:

```
dependencies {
    implementation 'com.github.williamfzc:randunit:0.1.1'
}
```

android:

```
dependencies {
    implementation 'com.github.williamfzc:randunit-android:0.1.1'
}
```

## why randunit?

### running on JVM, Android, and more

randunit supports JDK version >= 8. And thanks to [robolectric](https://github.com/robolectric/robolectric), randunit works fine on Android.

See [android sample](https://github.com/williamfzc/uamp) for details.

### based on junit4/5

Easily reuse all their extensions. Use it as a normal test case, both IDE and CI.

## effect?

randunit has been used for testing itself. See the screenshot above :)

Also you can see [android sample](https://github.com/williamfzc/uamp).

> NOTICE: randunit is still working in progress.

## prototype and its future

In this area (unittest generator), [randoop](https://github.com/randoop/randoop) and [evosuite](https://github.com/EvoSuite/evosuite) are in the lead. Both of them have provided some great ideas/algorithms for references.

But on the other hand, these two projects focused on algorithm research more (reach higher branch coverage and so on), which cared less on users (no offense, projects are still good).

randunit looks more like a framework. It consists of 3 parts:

- scanner: trying to collect all the data, such as classes, methods.
- generator: using data from scanner, generating series of statements.
- runner(env): actually invoke these statements and show their results.

Based on this structure, 

- algo developers only need to focus on designing `generator`.
- framework developers take the rest.

Current `generator` still has a long way to go.

## should use / should not use

In these days, there are still tons of projects developing without any unittest. `Continuous Test` is nearly the most important part of DevOps workflow. 
At the beginning, this repo was designed for providing smoke tests quickly, without any extra efforts.

### should use

Your project is still running naked yet.

### should not use

You want a strict coverage tool.

## license

Licensed under the Apache License, Version 2.0 (the "License"). See [LICENSE](LICENSE).

```text
Copyright 2021 williamfzc

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
