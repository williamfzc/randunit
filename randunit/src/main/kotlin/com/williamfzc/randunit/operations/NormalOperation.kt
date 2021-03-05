package com.williamfzc.randunit.operations

class NormalOperation : AbstractOperation() {
    override fun getInstance(): Any {
        return type.newInstance()
    }

    companion object {
        fun of(t: Class<*>): NormalOperation {
            val op = NormalOperation()
            op.type = t
            return op
        }
    }
}