package kr.cosine.fishadder.config

import kr.cosine.fishadder.data.FishItemStack
import kr.cosine.fishadder.registry.FishItemStackRegistry
import kr.hqservice.framework.bukkit.core.extension.toByteArray
import kr.hqservice.framework.bukkit.core.extension.toItemArray
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.File

@Bean
class FishConfig(
    plugin: Plugin,
    private val fishItemStackRegistry: FishItemStackRegistry
) {

    private val file = File(plugin.dataFolder, "fish.yml")
    private val config = YamlConfiguration.loadConfiguration(file)

    fun load() {
        if (!file.exists()) return
        val fishItemStacks = config.getString("fish")
            ?.run(Base64Coder::decodeLines)
            ?.toItemArray()
            ?.map(::FishItemStack)
            ?: return
        fishItemStackRegistry.setFishItemStacks(fishItemStacks)
    }

    fun save() {
        if (fishItemStackRegistry.isChanged) {
            val compressedItemStack = fishItemStackRegistry.fishItemStacks
                .map(FishItemStack::toItemStack)
                .toTypedArray<ItemStack?>()
                .toByteArray()
                .run(Base64Coder::encodeLines)
            config.set("fish", compressedItemStack)
            config.save(file)
            fishItemStackRegistry.isChanged = false
        }
    }
}