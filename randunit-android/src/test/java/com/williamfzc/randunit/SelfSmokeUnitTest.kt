package com.williamfzc.randunit

import com.williamfzc.randunit.operations.*
import com.williamfzc.randunit.scanner.Scanner
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class SelfSmokeUnitTest {
    private val clzSet = setOf(
        Scanner::class.java,
        AndroidOperationManager::class.java,
        ActivityOperation::class.java,
        ServiceOperation::class.java,
        ContentProviderOperation::class.java,
        OtherAndroidOperation::class.java,
        NormalOperation::class.java
    )

    @Test
    fun scanOnly() {
        val opm = AndroidOperationManager()

        for (each in clzSet) {
            opm.addClazz(each)
        }

        Scanner().scanAll(opm)
    }

    @TestFactory
    fun scanItself(): Iterable<DynamicTest> {
        return RandUnitAndroid.runWithTestFactory(clzSet)
    }
}
