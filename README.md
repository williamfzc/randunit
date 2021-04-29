# randunit

[![ci on jdk8,11](https://github.com/williamfzc/randunit/workflows/Android%20CI/badge.svg)](https://github.com/williamfzc/randunit)
[![codecov](https://codecov.io/gh/williamfzc/randunit/branch/main/graph/badge.svg?token=FNCFFQFVP8)](https://codecov.io/gh/williamfzc/randunit)
[![](https://jitpack.io/v/williamfzc/randunit.svg)](https://jitpack.io/#williamfzc/randunit)

> Build Android/JVM applications with confidence and less effort.

[English verison](README_en.md)

## 这是什么？

RandUnit 取义自 `Random UnitTest`，他会：

- 根据提供的包名或入口类，搜索所有相关的待测试类与方法
- 根据搜索结果，为每个方法生成一系列 statements 用于测试
- 像常规单测流程一般，在 junit 上运行这些 statements ，得到测试结果

而这一切只需要一次简单的复制粘贴：

```kotlin
import com.williamfzc.randunit.env.NormalTestEnv
import com.williamfzc.randunit.models.StatementModel
import com.williamfzc.randunit.scanner.ScannerConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MinExampleTest(private val statementModel: StatementModel) {
    companion object {
        private val testEnv = NormalTestEnv()
        private const val packageName = "com.your.package"
        private val cases by lazy {
            val scannerConfig = ScannerConfig()
            scannerConfig.includeFilter.add(packageName)

            RandUnit.collectStatementsWithPackage(packageName, scannerConfig)
        }

        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<StatementModel> {
            return cases
        }
    }

    @Test
    fun runStatements() {
        testEnv.runStatementInSandbox(statementModel)
    }
}
```

由于它是合法的junit用例，所以你可以在ide里直接运行它。直接 run with coverage 的话：

![ide](./docs/pics/ide.jpg)

你就可以无痛得到一份自动化冒烟测试用例了。

## 为什么使用 randunit?

### 支持 常规JVM应用 与 android

randunit supports JDK version >= 8. 得益于 [robolectric](https://github.com/robolectric/robolectric) 的支持, randunit 能够很好地被应用到 android 项目中。不过上面的模板需要有微调：

```kotlin
import com.williamfzc.randunit.RandUnitAndroid
import com.williamfzc.randunit.env.NormalTestEnv
import com.williamfzc.randunit.models.StatementModel
import com.williamfzc.randunit.scanner.ScannerConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [28])
@RunWith(ParameterizedRobolectricTestRunner::class)
class MinExampleTest(private val statementModel: StatementModel) {
    companion object {
        private val testEnv = NormalTestEnv()
        private const val packageName = "com.your.package"
        private val cases by lazy {
            val scannerConfig = ScannerConfig()
            scannerConfig.includeFilter.add(packageName)

            RandUnitAndroid.collectStatementsWithPackage(packageName, scannerConfig)
        }

        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun data(): Collection<StatementModel> {
            return cases
        }
    }

    @Test
    fun runStatements() {
        testEnv.runStatementInSandbox(statementModel)
    }
}
```

可以查看：

- [normal java demo](./randunit-demo)
- [android demo](./randunit-android-demo)
- [google android sample](https://github.com/williamfzc/uamp/commit/af36299bd4f2ce10eba39ec44914d56776a378f9)

### 良好 junit4/5 适配

原生支持了 junit4/5 ，这也使得我们能够直接利用他们原有的各种特性（包括报告、插件等），也可以很好地与 IDE、CI环境 进行协同。

### 发现问题

做这个东西除了覆盖率，当然预期是他能够发现真正的问题。

```kotlin
override fun getCastOptions(context: Context?): CastOptions? {

    // oh, you import a non-existed class here!
    // it should cause a ClassNotFoundException
    Class.forName("import unknown class here!")

    ...
}
```

该类型问题在编译期并不能被发现，问题越晚暴露 == 修复成本越高。randunit可以无痛发现该类型问题：

```text
WARNING: error happened inside sandbox: java.lang.reflect.InvocationTargetException

java.lang.ClassNotFoundException: import unknown class here!

	at org.robolectric.internal.bytecode.SandboxClassLoader.getByteCode(SandboxClassLoader.java:158)
```

当然，不局限于该类型问题。它能够侦测所有会抛出异常的情况，并呈现在测试报告里。

### 扩展与演进

目前，randunit 的执行还在使用较为原始的策略：直接使用随机的mock参数，invoke。所以很显然它并不能很好覆盖所有的分支，可以看到他的方法、类覆盖率虽然还可以，但是行覆盖率一般般。

所以在最初期，randunit 的定位就是一个框架（不同于 randoop 与 evosuite），算法只是它的其中一部分，还需要考虑兼容与适配运行环境来提供良好的扩展性。在此基础上，后期将这些算法迁移到这上面之后，这些算法将可以无痛运行在不同环境上了。更多展开详见下文。

## installation

> 最新版本请以顶部徽章显示的版本为准。

randunit released on [jitpack.io/#williamfzc/randunit](https://jitpack.io/#williamfzc/randunit).

### gradle

maven repo:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

常规 java/kotlin 项目：

```
dependencies {
    testImplementation "com.github.williamfzc.randunit:randunit:0.1.3"
}
```

android 项目：

```
dependencies {
    testImplementation "com.github.williamfzc.randunit:randunit-android:0.1.3"
}
```

### maven

maven repo：

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

首先需要添加 kotlin 依赖（如有请忽略）：

```xml
<dependency>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-stdlib-jdk8</artifactId>
    <version>${kotlin.version}</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-test</artifactId>
    <version>${kotlin.version}</version>
    <scope>test</scope>
</dependency>
```

常规 java/kotlin 项目：

```
<dependency>
    <groupId>com.github.williamfzc.randunit</groupId>
    <artifactId>randunit</artifactId>
    <version>0.1.3</version>
</dependency>
```

android 项目（微调 artifactId 即可）：

```
<dependency>
    <groupId>com.github.williamfzc.randunit</groupId>
    <artifactId>randunit-android</artifactId>
    <version>0.1.3</version>
</dependency>
```

## 效果如何？

randunit 自身的单元测试是由它自己完成的。（注：badge中的覆盖率并不准确，你可以在 IDE 中试试 ：）

或者，你可以尝试一下上面的三个demo。

> NOTICE: 目前 randunit 还处在持续迭代的阶段，对于不准确的情况还请多谅解。对于这个项目的预期请参见下文。

## 原型与演进方向

在单元测试领域，有两个优秀的学院派先行者：

- [randoop](https://github.com/randoop/randoop)
- [evosuite](https://github.com/EvoSuite/evosuite)

他们都在生成算法与策略上有非常成熟的经验，感兴趣的同学可以到他们主页中查阅论文，后者比前者要更为先进一些。

但从另一个方面来说，这两个项目几乎将大部分的精力投入在算法上（例如 evosuite 的目标是尽可能提高分支覆盖率），这也使得他们在落地与使用上存在一定的壁垒与门槛：

- 严格来说他们是用例生成器，生成后还要处理执行的问题，兼容其他平台会比较困难
- 大部分场景他们推荐使用命令行工具，这也使得与其他工具的联动能力并不是很好

randunit 的定位是一个框架，由三个部分组成:

- scanner: 基于用户提供的入口类或包，搜索所有待测的类与方法，形成一系列 statement model
- generator: 根据前者生成的 statement model，结合策略生成一系列的 statements
- runner(env): 适配并提供运行环境，用于真正运行 statements，得到测试结果等

基于这一结构：

- 算法工程师可以专心在 generator 的设计上，而不需要关心其他例如运行环境之类的事情
- 框架工程师可以专心在其他事情上，而无需纠结算法问题

虽然目前的 generator 距离先进水平还有很大差距。

## 什么时候推荐使用 randunit

- 在之前很长一段时间的观察里，无论项目大小，依旧有大量开发中的项目处在没有任何单元测试的状态中。随着 devops 的流行，持续化的自动化测试几乎已经成为整个敏捷流程中最为关键的一环。
- 而在这种情况下其实很多业务认知到了这一阶段的重要性，但不知道从何下手
- 这个项目的出发点就是，用尽可能少的成本将单元测试跑起来，至少将这里的空缺填补起来

### 推荐使用的情况

你的项目目前还处于裸奔的状态，或单元测试做得不好的状态

### 不推荐使用的情况

你想要一个彻底发现问题并能完整覆盖整个项目的工具

## FAQ

### 如何忽略无效错误？

随机性意味着可能会出现随机问题。因为动态注入与反射之类机制的存在，randunit目前并不能甄别抛出的问题就是一个真实的bug：例如框架发现一些参数没有初始化导致NPE，其实只是因为这些参数是由动态注入来初始化的；但屏蔽也不可以，因为在其他场景下这确实是个问题。

所以我们决定不在框架内进行武断判定（当然其实已经屏蔽了一些框架本身引起的问题），而是将判定交由用户自己决定。你可以通过添加 rule 来决定一个被抛出的异常是否为意料内的：

```
// custom rules
class CustomRule : AbstractRule() {
    override fun judge(statement: Statement, e: Throwable): Boolean {

        // return true if you do not think that is a bug
        return when (e) {
            is IllegalStateException -> true
            else -> false
        }
    }
}

// and add it to your env
testEnv.envConfig.sandboxConfig.rules.add(CustomRule())
```

这里的判定可以非常灵活，举个例子，我们不可能忽略所有的 NPE 问题，但我们可以选择性地忽略某个模块或某个类。例如，你希望屏蔽某个特定模块（例如com.abc.def）引发的异常：

```kotlin
if (isHappenedInPackage(e, "com.abc.def"))
    return true
```

或者可以自定义进行任意形态的判定。更多可参见 AbstractRule 中的方法。

### 一些类发现不了

按包名搜索的实现来自 [reflections](https://github.com/ronmamo/reflections) ，确实可能出现搜索不彻底的情况。可以在 cases 生成过程中自行添加特定的类：

```kotlin
val statements = RandUnit.collectStatements(
    setOf(
        SomeClass::class.java, 
        SomeOtherClass::class.java
    )
)
 
// 再将其加入即可           
RandUnitAndroid.collectStatementsWithPackage(packageName, scannerConfig).plus(statements)
```

这些搜索都是递归的，所以只需要添加几个最关键的即可。另外，randunit的方法遍历机制也能够潜移默化地推动开发者写出更加适合单测的代码。

## 参与该项目 & 二次开发

请参见：[issue 2](https://github.com/williamfzc/randunit/issues/2)

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
