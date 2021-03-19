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
package com.williamfzc.randunit.extensions

fun Throwable.getFirstTrace(): StackTraceElement? = this.stackTrace.firstOrNull()
fun Throwable.firstTraceContains(s: String): Boolean =
    this.getFirstTrace()?.toString()?.contains(s, ignoreCase = true) ?: false

fun Throwable.msgContains(s: String): Boolean =
    this.message?.contains(s) ?: false

fun Throwable.isRobolectricError(): Boolean =
    this.firstTraceContains("robolectric")
