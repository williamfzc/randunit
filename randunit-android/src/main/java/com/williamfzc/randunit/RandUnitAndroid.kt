package com.williamfzc.randunit

import com.williamfzc.randunit.models.StatementModel
import com.williamfzc.randunit.operations.AndroidOperationManager
import com.williamfzc.randunit.operations.OperationManager
import com.williamfzc.randunit.scanner.ScannerConfig

object RandUnitAndroid : RandUnitBase() {
    override fun getOperationManager(): OperationManager {
        return AndroidOperationManager()
    }

    // why we need a cache:
    // Robolectric uses a different class loader when running the tests, so the parameters objects
    // created by the test runner are not compatible with the parameters required by the test.
    // Instead, we compute the parameters within the test's class loader.
    private var stCache: Collection<StatementModel> = setOf()

    fun collectStatementsWithCache(
        targetClasses: Iterable<Class<*>>,
        cfg: ScannerConfig? = null
    ): Collection<StatementModel> {
        if (stCache.isEmpty())
            stCache = collectStatements(targetClasses, cfg)
        return stCache
    }
}
