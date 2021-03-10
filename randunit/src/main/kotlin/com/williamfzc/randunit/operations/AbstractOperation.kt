package com.williamfzc.randunit.operations

abstract class AbstractOperation {
    open var type: Class<*> = Any::class.java
    abstract fun getInstance(): Any

    fun getId(): String {
        return type.canonicalName
    }
}
