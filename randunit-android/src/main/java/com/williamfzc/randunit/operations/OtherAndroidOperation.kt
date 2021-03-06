package com.williamfzc.randunit.operations

class OtherAndroidOperation : AbstractAndroidOperation() {
    override fun getInstance(): Any {
        return type.newInstance()
    }
}
