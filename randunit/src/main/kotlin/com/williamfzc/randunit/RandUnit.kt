package com.williamfzc.randunit

import com.williamfzc.randunit.operations.OperationManager

object RandUnit : RandUnitBase() {
    override fun getOperationManager(): OperationManager {
        return OperationManager()
    }
}