package com.williamfzc.randunit.operations

import java.util.*

open class OperationManager {
    private var operations: Queue<AbstractOperation> = LinkedList<AbstractOperation>()

    fun add(op: AbstractOperation) {
        operations.add(op)
    }

    fun poll(): AbstractOperation? {
        return operations.poll()
    }

    open fun addClazz(newClazz: Class<*>) = add(NormalOperation.of(newClazz))
}