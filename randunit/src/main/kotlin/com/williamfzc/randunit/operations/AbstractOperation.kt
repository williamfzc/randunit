package com.williamfzc.randunit.operations

abstract class AbstractOperation {
    open lateinit var type: Class<*>
    abstract fun getInstance(): Any

    fun getId(): String {
        return type.canonicalName
    }
}