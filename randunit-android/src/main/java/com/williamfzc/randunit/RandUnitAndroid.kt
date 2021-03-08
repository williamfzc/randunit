package com.williamfzc.randunit

import com.williamfzc.randunit.operations.AndroidOperationManager
import com.williamfzc.randunit.operations.OperationManager

object RandUnitAndroid : RandUnitBase() {
    override fun getOperationManager(): OperationManager {
        return AndroidOperationManager()
    }
}
