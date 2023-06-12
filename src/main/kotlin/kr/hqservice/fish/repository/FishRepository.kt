package kr.hqservice.fish.repository

import kr.hqservice.fish.extension.async
import kr.hqservice.fish.extension.percent
import kr.hqservice.fish.util.CustomConfig
import org.bukkit.inventory.ItemStack

class FishRepository(private val file: CustomConfig) {

    private val config = file.getConfig()

    private val fishes = mutableMapOf<ItemStack, Double>()

    fun load() {
        if (!config.contains("fishes")) return
        config.getConfigurationSection("fishes")?.let { fishSection ->
            fishSection.getKeys(false).forEach { count ->
                val percent = fishSection.getDouble("$count.percent")
                val item = fishSection.getItemStack("$count.item") ?: return@forEach
                fishes[item] = percent
            }
        }
    }

    fun save() {
        val fishes = fishes.toList()
        config.set("fishes", null)
        async {
            var count = 0
            fishes.forEach { (item, percent) ->
                config.set("fishes.$count.percent", percent)
                config.set("fishes.$count.item", item)
                count++
            }
            file.saveConfig()
        }
    }

    fun reload() {
        file.reloadConfig()
        fishes.clear()
        load()
    }

    fun isEmpty() = fishes.isEmpty()

    fun contains(item: ItemStack) = fishes.contains(item)

    fun getFish() = fishes.percent()

    fun setFish(item: ItemStack, percent: Double) {
        fishes[item] = percent
        save()
    }

    fun removeFish(item: ItemStack) {
        fishes.remove(item)
        save()
    }

    fun getFishes(): Map<ItemStack, Double> = fishes
}