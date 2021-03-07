package com.williamfzc.randunit.models

import io.mockk.mockkClass
import io.mockk.mockkObject
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.ObjectCreationException
import java.lang.Exception
import kotlin.reflect.KClass

class MockModel(easyRandomParameters: EasyRandomParameters? = null) {
    private val usedEasyRandomParameters =
        easyRandomParameters ?: genDefaultEasyRandomParameters()
    private val easyRandom = EasyRandom(usedEasyRandomParameters)

    private fun genDefaultEasyRandomParameters(): EasyRandomParameters {
        return EasyRandomParameters()
            .scanClasspathForConcreteTypes(true)
    }

    fun <T : Any> mock(t: Class<T>): Any? {
        return try {
            mockWithEasyRandom(t)
        } catch (e: ObjectCreationException) {
            // try mockk (kt
            try {
                mockWithMockk(t.kotlin)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun <T> mockWithEasyRandom(t: Class<T>): T {
        return easyRandom.nextObject(t)
    }

    fun <T : Any> mockWithMockk(t: KClass<T>): Any {
        t.objectInstance?.let {
            return mockkObject(it)
        } ?: run {
            return mockkClass(t)
        }
    }
}
