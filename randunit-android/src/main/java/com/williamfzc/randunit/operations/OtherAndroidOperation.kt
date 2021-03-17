package com.williamfzc.randunit.operations

import com.williamfzc.randunit.models.DefaultMocker

class OtherAndroidOperation(t: Class<*>) : AbstractAndroidOperation(t) {
    override fun getInstance(): Any {
        return DefaultMocker.mock(type)!!
    }
}
