package kr.hqservice.fish.inventory

import kr.hqservice.fish.extension.setDisplayName
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

internal object InventoryUtils {

    val air = ItemStack(Material.AIR)

    val beforePageButton = ItemStack(Material.ARROW).setDisplayName("§c이전 페이지로 이동")

    val nextPageButton = ItemStack(Material.ARROW).setDisplayName("§a다음 페이지로 이동")

    fun Inventory.setItem(range: IntRange, item: ItemStack) {
        range.forEach { setItem(it, item) }
    }

    fun Inventory.setItem(item: ItemStack, vararg slot: Int) {
        slot.forEach { setItem(it, item) }
    }

    fun Player.playButtonSound(volume: Float = 1f, pitch: Float = 1f) {
        playSound(location, Sound.UI_BUTTON_CLICK, volume, pitch)
    }
}