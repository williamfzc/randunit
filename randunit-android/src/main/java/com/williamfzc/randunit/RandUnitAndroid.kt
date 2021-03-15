package com.williamfzc.randunit

import android.app.Activity
import android.app.Service
import android.content.ContentProvider
import androidx.fragment.app.Fragment
import com.williamfzc.randunit.models.StatementModel
import com.williamfzc.randunit.operations.AndroidOperationManager
import com.williamfzc.randunit.operations.OperationManager
import com.williamfzc.randunit.scanner.ScannerConfig
import org.reflections.Reflections

object RandUnitAndroid : RandUnitBase() {
    override fun getOperationManager(): OperationManager {
        return AndroidOperationManager()
    }

    fun collectStatementsWithAutoSearch(
        targetClasses: Set<Class<*>>,
        cfg: ScannerConfig? = null,
        packageName: String
    ): List<StatementModel> {
        val extra = mutableSetOf<Class<*>>()
        val reflections = Reflections(packageName)

        for (
            eachBaseType in setOf(
                Activity::class.java,
                Service::class.java,
                ContentProvider::class.java,
                Fragment::class.java
            )
        )
        // should not cause any errors
            kotlin.runCatching {
                reflections.getSubTypesOf(eachBaseType).forEach { extra.add(it) }
            }
        return collectStatements(extra.plus(targetClasses), cfg)
    }
}
