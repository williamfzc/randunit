package com.williamfzc.randunit.operations

import com.williamfzc.randunit.models.DefaultMocker

class OtherAndroidOperation : AbstractAndroidOperation() {
    override fun getInstance(): Any {
        return DefaultMocker.mock(type)!!
    }
}
