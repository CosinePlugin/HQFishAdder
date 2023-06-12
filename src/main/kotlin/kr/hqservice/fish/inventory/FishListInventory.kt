package kr.hqservice.fish.inventory

import kr.hqservice.fish.HQFishAdder.Companion.prefix
import kr.hqservice.fish.extension.later
import kr.hqservice.fish.extension.setLore
import kr.hqservice.fish.repository.FishRepository
import kr.ms.core.util.ItemStackNameUtil
import kr.ms.core.util.ItemStackNameUtil.getItemName
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

class FishListInventory(
    private val fishRepository: FishRepository
) : FishInventoryHolder("낚시 : 물고기 목록", 6, true) {

    private val items = fishRepository.getFishes()

    override fun init(inventory: Inventory) {
        var slot = 0
        items.forEach { (item, percent) ->
            val fish = item.clone().setLore("§7확률: $percent%", "", "§c[ 쉬프트 좌클릭 시 해당 물고기를 제거합니다 ]")
            inventory.setItem(slot, fish)
            slot++
        }
    }

    override fun onClick(event: InventoryClickEvent) {
        if (event.clickedInventory == null) return

        val slot = event.slot
        if (slot > 53 || slot >= items.size) return

        val player = event.whoClicked as Player
        val item = items.keys.toList()[slot]

        player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1f, 1f)

        if (event.click == ClickType.SHIFT_LEFT) {
            fishRepository.removeFish(item)
            player.sendMessage("$prefix ${getItemName(item)}(이)가 목록에서 제거되었습니다.")
            later { FishListInventory(fishRepository).openInventory(player) }
        }
    }
}