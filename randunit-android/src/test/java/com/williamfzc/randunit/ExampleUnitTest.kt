package com.williamfzc.randunit

import com.williamfzc.randunit.operations.*
import com.williamfzc.randunit.runner.Runner
import org.junit.Test

class ExampleUnitTest {
    private val clzSet = setOf(
        Runner::class.java,
        AndroidOperationManager::class.java,
        ActivityOperation::class.java,
        ServiceOperation::class.java,
        ContentProviderOperation::class.java,
        OtherAndroidOperation::class.java,
        NormalOperation::class.java
    )

    @Test
    fun addition_isCorrect() {
        val opm = AndroidOperationManager()

        for (each in clzSet) {
            opm.addClazz(each)
        }

        Runner().runAll(opm)
    }
}
