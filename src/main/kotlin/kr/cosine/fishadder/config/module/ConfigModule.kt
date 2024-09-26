package kr.cosine.fishadder.config.module

import kr.cosine.fishadder.config.FishConfig
import kr.hqservice.framework.bukkit.core.component.module.Module
import kr.hqservice.framework.bukkit.core.component.module.Setup
import kr.hqservice.framework.bukkit.core.component.module.Teardown

@Module
class ConfigModule(
    private val fishConfig: FishConfig
) {

    @Setup
    fun setup() {
        fishConfig.load()
    }

    @Teardown
    fun teardown() {
        fishConfig.save()
    }
}