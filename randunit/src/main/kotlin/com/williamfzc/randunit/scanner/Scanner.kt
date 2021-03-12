/**
 * Copyright 2021 williamfzc
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.williamfzc.randunit.scanner

import com.williamfzc.randunit.extensions.*
import com.williamfzc.randunit.models.MethodModel
import com.williamfzc.randunit.operations.AbstractOperation
import com.williamfzc.randunit.operations.OperationManager
import org.reflections.Reflections
import java.lang.reflect.Method
import java.util.*
import java.util.logging.Logger
import kotlin.reflect.jvm.javaMethod

open class Scanner(private val cfg: ScannerConfig = ScannerConfig()) :
    ScannerHookLayer {
    companion object {
        private val logger = Logger.getLogger("Scanner")
    }

    var statementCount = 0
    var opHistory = mutableSetOf<String>()

    private fun verifyClass(t: Class<*>): Boolean {
        val ret = t.isValidType()
            .and(!t.hasTypePrefix(cfg.excludeFilter))

        // avoid NoClassDef
        try {
            t.canonicalName
        } catch (e: NoClassDefFoundError) {
            return false
        }

        // ignore abstract classes
        if (t.isInterface || t.isAbstract())
            return false

        // include filter?
        if (cfg.includeFilter.isNotEmpty())
            return ret.and(t.hasTypePrefix(cfg.includeFilter))
        return ret
    }

    private fun verifyOperation(operation: AbstractOperation): Boolean {
        operation.type.let {
            return verifyClass(it).and(!opHistory.contains(operation.id()))
        }
    }

    private fun verifyMethod(method: Method): Boolean {
        if (method.isBuiltin())
            return false

        // exclude protected and private methods
        method.isAccessible = true
        if (method.isPrivateOrProtected()) {
            if (!cfg.includePrivateMethod)
                return false
        }
        return true
    }

    private fun scanWithLock(operation: AbstractOperation, operationManager: OperationManager) {
        val opRawType = operation.type
        if (!verifyOperation(operation)) {
            logger.info("operation $opRawType is invalid, skipped")
            return
        }

        logger.info("start scanning op: ${opRawType.canonicalName}")
        val collectedMethods = mutableListOf(*opRawType.getDeclaredMethodsSafely())

        // todo: count of classes from loader can be a large number ...
        if (cfg.recursively)
            for (eachClz in getClassesFromLoader(opRawType.classLoader))
                if (verifyClass(eachClz))
                    operationManager.addClazz(eachClz)

        // parent types
        operationManager.addClazz(opRawType.superclass)

        // search its subtypes if abstract
        if (opRawType.isInterface || opRawType.isAbstract())
            for (eachClz in getSubTypes(opRawType))
                operationManager.addClazz(eachClz)

        // inner classes should be considered
        for (eachInnerClass in opRawType.getDeclaredClassesSafely())
            operationManager.addClazz(eachInnerClass)

        // fields
        for (eachField in opRawType.getDeclaredFieldsSafely()) {
            operationManager.addClazz(eachField.type)

            // access (get and set
            if (eachField.isAccessible) {
                eachField::set.javaMethod?.let { collectedMethods.add(it) }
                eachField::get.javaMethod?.let { collectedMethods.add(it) }
            }
        }

        for (eachMethod in collectedMethods) {
            if (!verifyMethod(eachMethod)) {
                logger.info("verify method ${eachMethod.name} false, skipped")
                continue
            }
            beforeMethod(eachMethod, operation, operationManager)
            scanMethod(eachMethod, operation, operationManager)
            afterMethod(eachMethod, operation, operationManager)
        }
        logger.info("op $operation end")
        opHistory.add(operation.id())
    }

    private fun scan(operation: AbstractOperation, operationManager: OperationManager) {
        synchronized(operation.type) {
            scanWithLock(operation, operationManager)
        }
    }

    // NOTICE: this method can not be used in android
    // but actually our unit tests will only run on JVM (robolectric)
    // https://stackoverflow.com/questions/2681459/how-can-i-list-all-classes-loaded-in-a-specific-class-loader
    private fun getClassesFromLoader(loader: ClassLoader): Iterable<Class<*>> {
        synchronized(loader) {
            return try {
                val f = ClassLoader::class.java.getDeclaredField("classes")
                f.isAccessible = true
                // return value is a vector ... with lots of potential thread issues
                val vector = f.get(Thread.currentThread().contextClassLoader) as Vector<Class<*>>
                val s = mutableSetOf<Class<*>>()
                vector.forEach {
                    s.add(it)
                }
                s
            } catch (e: Exception) {
                logger.warning("failed to get classes from class loader: $loader because of $e")
                setOf()
            }
        }
    }

    private fun getSubTypes(interfaceOrAbstract: Class<*>): Iterable<Class<*>> {
        return Reflections().getSubTypesOf(
            interfaceOrAbstract::class.java
        )
    }

    private fun scanMethod(
        method: Method,
        operation: AbstractOperation,
        operationManager: OperationManager
    ) {
        val model = MethodModel(operation, method)

        // append some rel types
        for (eachRelType in model.getRelativeTypes())
            operationManager.addClazz(eachRelType)

        val stat = model.generateStatement()
        statementCount++
        stat?.run {
            try {
                handle(this)
            } catch (e: Exception) {
                logger.warning("unknown error happened: $e")
                e.printStackTrace()
            }
        }
    }

    fun scanAll(operationManager: OperationManager) {
        var op = operationManager.poll()
        while (null != op) {
            beforeOperation(op, operationManager)
            scan(op, operationManager)
            afterOperation(op, operationManager)

            // get the next one
            op = operationManager.poll()

            if (statementCount >= cfg.statementLimit) {
                logger.info("statement count reach limit: ${cfg.statementLimit}")
                break
            }
        }

        logger.info(
            """
            scan finished
            
            - relative classes: $opHistory
            - statement count: $statementCount
            """.trimIndent()
        )
    }
}
