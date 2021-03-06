package com.williamfzc.randunit.operations

class AndroidOperationManager: OperationManager() {
    override fun addClazz(newClazz: Class<*>) {
        add(AbstractAndroidOperation.of(newClazz))
    }
}