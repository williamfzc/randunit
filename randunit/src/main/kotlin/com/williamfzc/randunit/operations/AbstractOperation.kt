package com.williamfzc.randunit.operations

abstract class AbstractOperation() {
    lateinit var type: Class<*>

    abstract fun getInstance(): Any
}