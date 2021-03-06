package com.williamfzc.randunit.runner

import com.williamfzc.randunit.models.Statement
import com.williamfzc.randunit.operations.AbstractOperation
import com.williamfzc.randunit.operations.OperationManager
import java.lang.reflect.Method

interface RunnerHookLayer {
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

    fun beforeExec(
        statement: Statement
    ) {
    }

    fun afterExec(
        statement: Statement
    ) {
    }
}
