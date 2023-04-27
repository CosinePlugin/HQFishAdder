package kr.hqservice.fish.repository

import kr.hqservice.fish.extension.async
import kr.hqservice.fish.util.CustomConfig
import org.bukkit.inventory.ItemStack

class FishRepository(private val file: CustomConfig) {

    private val config = file.getConfig()

    private val fishes = mutableListOf<ItemStack>()

    var isChanged = false

    @Suppress("unchecked_cast")
    fun load() {
        if (!config.contains("fishes")) return
        fishes.addAll(config.getList("fishes") as MutableList<ItemStack>)
    }

    fun save() {
        async {
            config.set("fishes", fishes)
            file.saveConfig()
            isChanged = false
        }
    }

    fun contains(item: ItemStack): Boolean {
        return fishes.contains(item)
    }

    fun addFish(item: ItemStack) {
        fishes.add(item)
        isChanged = true
    }

    fun removeFish(item: ItemStack) {
        fishes.remove(item)
        isChanged = true
    }

    fun getFishes(): List<ItemStack> {
        return fishes
    }
}