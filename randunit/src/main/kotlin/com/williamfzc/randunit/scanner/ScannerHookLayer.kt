package com.williamfzc.randunit.scanner

import com.williamfzc.randunit.models.StatementModel
import com.williamfzc.randunit.operations.AbstractOperation
import com.williamfzc.randunit.operations.OperationManager
import java.lang.reflect.Method

interface ScannerHookLayer {
    fun beforeMethod(
        method: Method,
        operation: AbstractOperation,
        operationManager: OperationManager
    ) {
    }

    fun afterMethod(
        method: Method,
        operation: AbstractOperation,
        operationManager: OperationManager
    ) {
    }

    fun beforeOperation(
        operation: AbstractOperation,
        operationManager: OperationManager
    ) {
    }

    fun afterOperation(
        operation: AbstractOperation,
        operationManager: OperationManager
    ) {
    }

    fun handle(statementModel: StatementModel) {
    }
}
