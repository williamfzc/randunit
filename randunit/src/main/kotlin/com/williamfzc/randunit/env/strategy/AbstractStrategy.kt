package com.williamfzc.randunit.env.strategy

import com.williamfzc.randunit.env.Statement
import com.williamfzc.randunit.models.DefaultMocker
import com.williamfzc.randunit.models.MockModel
import com.williamfzc.randunit.models.StatementModel

abstract class AbstractStrategy {
    var mockModel: MockModel = DefaultMocker
    abstract fun genStatements(statementModel: StatementModel, mockModel: MockModel): Iterable<Statement>
}
