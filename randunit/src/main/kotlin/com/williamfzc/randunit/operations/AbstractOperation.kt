package com.williamfzc.randunit.operations

import com.williamfzc.randunit.helper.TypeHelper

abstract class AbstractOperation {
    open lateinit var type: Class<*>
    abstract fun getInstance(): Any
    fun isValid(): Boolean {
        return TypeHelper.isValidType(type)
    }
}
