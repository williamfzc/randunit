package com.williamfzc.randunit.helper

import org.jeasy.random.EasyRandom

object TypeHelper {
    private val easyRandom = EasyRandom()
    var typeFilter = mutableSetOf("java.", "android.")

    fun <T> mock(t: Class<T>): T {
        return easyRandom.nextObject(t)
    }

    fun isBuiltinType(t: Class<*>): Boolean {
        for (each in typeFilter)
            if (t.canonicalName.startsWith(each))
                return true
        return false
    }

    fun isNullType(t: Class<*>): Boolean {
        return t == Void::class.java
    }

    fun isValidType(t: Class<*>): Boolean {
        return !isBuiltinType(t).and(!isNullType(t))
    }
}