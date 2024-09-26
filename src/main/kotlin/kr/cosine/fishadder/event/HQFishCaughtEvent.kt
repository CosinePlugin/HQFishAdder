package kr.cosine.fishadder.event

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack

class HQFishCaughtEvent(
    who: Player,
    val fish: ItemStack,
    val fishDisplayName: String,
    val chance: Double
) : PlayerEvent(who) {

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }

    override fun getHandlers(): HandlerList {
        return getHandlerList()
    }
}