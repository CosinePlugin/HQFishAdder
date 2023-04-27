package kr.hqservice.fish.listener

import kr.hqservice.fish.inventory.FishInventoryHolder
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class FishInventoryListener : Listener {

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        event.inventory.holder?.let {
            if (it is FishInventoryHolder) {
                it.onInventoryClick(event)
            }
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        event.inventory.holder?.let {
            if (it is FishInventoryHolder) {
                it.onInventoryClose(event)
            }
        }
    }
}