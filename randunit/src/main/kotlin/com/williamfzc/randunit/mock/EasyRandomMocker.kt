package com.williamfzc.randunit.mock

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters

class EasyRandomMocker(cfg: MockConfig) : AbstractMocker(cfg) {
    private val usedEasyRandomParameters =
        cfg.easyRandomParameters ?: genDefaultEasyRandomParameters()
    private val easyRandom = EasyRandom(usedEasyRandomParameters)

    override fun mock(t: Class<*>): Any {
        return easyRandom.nextObject(t)
    }

    private fun genDefaultEasyRandomParameters(): EasyRandomParameters {
        return EasyRandomParameters()
            .scanClasspathForConcreteTypes(true)
    }
}
