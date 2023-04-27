package kr.hqservice.fish.inventory

import kr.hqservice.fish.HQFishAdder
import kr.hqservice.fish.HQFishAdder.Companion.prefix
import kr.hqservice.fish.extension.amount
import kr.hqservice.fish.inventory.InventoryUtils.playButtonSound
import kr.hqservice.fish.inventory.InventoryUtils.setItem
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

class FishInventory(plugin: HQFishAdder) : FishInventoryHolder("낚시 : 물고기 관리", 6, true) {

    private var page = 0

    private val fishRepository = plugin.fishRepository

    private val fishes = fishRepository.getFishes()
    private val chunkedFishes get() = fishes.chunked(45)
    private val nowPageFishes get() = chunkedFishes[page]

    override fun prevInit(inventory: Inventory) {
        inventory.setItem(45, InventoryUtils.beforePageButton)
        inventory.setItem(46..52, InventoryUtils.background)
        inventory.setItem(53, InventoryUtils.nextPageButton)
    }

    override fun init(inventory: Inventory) {
        inventory.setItem(0..44, InventoryUtils.air)
        if (fishes.isEmpty()) return
        nowPageFishes.forEachIndexed { index, item ->
            inventory.setItem(index, item)
        }
    }

    override fun onClick(event: InventoryClickEvent) {
        if (event.clickedInventory == null) return

        val player = event.whoClicked as Player

        event.itemController(player)
        event.pageController(player)
    }

    private fun InventoryClickEvent.itemController(player: Player) {
        // 제거
        val slot = rawSlot
        if (fishes.isNotEmpty() && slot < 45 && slot < nowPageFishes.size) {
            val item = nowPageFishes[slot]

            fishRepository.removeFish(item)
            player.sendMessage("$prefix 물고기가 제거되었습니다.")

            if (page != 0 && page >= chunkedFishes.size) {
                page--
            }
            init(inventory)
        }
        // 등록
        if (slot > 53) {
            val clickedItem = currentItem ?: return
            val item = clickedItem.clone().amount()

            if (fishRepository.contains(item)) {
                player.sendMessage("$prefix 이미 등록되어 있는 물고기입니다.")
                return
            }

            fishRepository.addFish(item)
            player.sendMessage("$prefix 물고기가 등록되었습니다.")

            if (inventory.getItem(44) != null) {
                page++
            }
            init(inventory)
        }
    }

    private fun InventoryClickEvent.pageController(player: Player) {
        if (slot in 45..53) isCancelled = true
        when (slot) {
            45 -> {
                player.playButtonSound()
                if (page == 0) {
                    player.sendMessage("$prefix 이전 페이지가 존재하지 않습니다.")
                    return
                }
                page--
                init(inventory)
            }
            53 -> {
                player.playButtonSound()
                if (page + 1 >= chunkedFishes.size) {
                    player.sendMessage("$prefix 다음 페이지가 존재하지 않습니다.")
                    return
                }
                page++
                init(inventory)
            }
        }
    }

    override fun onClose(event: InventoryCloseEvent) {
        val player = event.player as Player
        if (fishRepository.isChanged) {
            fishRepository.save()
            player.sendMessage("$prefix 변경 내용이 저장되었습니다.")
        }
    }
}