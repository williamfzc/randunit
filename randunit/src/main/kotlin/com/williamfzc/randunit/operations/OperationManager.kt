package com.williamfzc.randunit.operations

import java.util.*

class OperationManager {
    private var operations = LinkedList<AbstractOperation>()

    fun append(op: AbstractOperation) {
        operations.add(op)
    }

    fun popFirst(): AbstractOperation? {
        return operations.poll()
    }

    fun addClazz(newClazz: Class<*>) = append(NormalOperation.of(newClazz))
}