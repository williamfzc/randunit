package com.williamfzc.randunit

import org.junit.Test

import randoop.condition.SpecificationCollection
import randoop.main.GenInputsAbstract
import randoop.main.ThrowClassNameError
import randoop.reflection.DefaultReflectionPredicate
import randoop.reflection.OperationModel
import randoop.reflection.VisibilityPredicate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val model = OperationModel.createModel(
            VisibilityPredicate.PackageVisibilityPredicate(GenInputsAbstract.junit_package_name),
            DefaultReflectionPredicate(),
            GenInputsAbstract.omit_methods,
            setOf<String>(),
            setOf<String>(),
            ThrowClassNameError(),
            GenInputsAbstract.literals_file,
            SpecificationCollection.create(GenInputsAbstract.specifications)
        )
        val operations = model.operations
        println("operation: $operations")
    }
}