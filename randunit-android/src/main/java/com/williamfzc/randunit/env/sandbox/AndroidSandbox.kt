package com.williamfzc.randunit.env.sandbox

import com.williamfzc.randunit.env.rules.AndroidBuiltinRule

class AndroidSandbox(override var cfg: SandboxConfig) : Sandbox(cfg) {
    init {
        defaultRules = setOf(AndroidBuiltinRule())
    }
}
