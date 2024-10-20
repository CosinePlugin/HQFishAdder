package kr.cosine.fishadder.listener

import kr.cosine.fishadder.event.HQFishCaughtEvent
import kr.cosine.fishadder.registry.ChatObserverRegistry
import kr.cosine.fishadder.service.FishService
import kr.hqservice.framework.bukkit.core.listener.HandleOrder
import kr.hqservice.framework.bukkit.core.listener.Listener
import kr.hqservice.framework.bukkit.core.listener.Subscribe
import kr.hqservice.framework.nms.extension.getDisplayName
import org.bukkit.entity.Item
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.PluginManager

@Listener
class FishListener(
    private val pluginManager: PluginManager,
    private val chatObserverRegistry: ChatObserverRegistry,
    private val fishService: FishService
) {
    @Subscribe
    fun onPlayerFish(event: PlayerFishEvent) {
        if (event.state != PlayerFishEvent.State.CAUGHT_FISH) return

        val caught = event.caught
        if (caught !is Item) return

        val fishItemStack = fishService.findFishItemStackByChance() ?: return

        val player = event.player
        val fish = fishItemStack.toOriginalItemStack()
        val fishDisplayName = fish.getDisplayName()
        val chance = fishItemStack.getChance()
        caught.itemStack = fish

        val hqFishCaughtEvent = HQFishCaughtEvent(player, fish, fishDisplayName, chance)
        pluginManager.callEvent(hqFishCaughtEvent)
    }

    @Subscribe(handleOrder = HandleOrder.FIRST, ignoreCancelled = true)
    fun onPlayerAsyncChat(event: AsyncPlayerChatEvent) {
        chatObserverRegistry.observe(event)
    }

    @Subscribe
    fun onPlayerQuit(event: PlayerQuitEvent) {
        chatObserverRegistry.removeChatObserver(event.player.uniqueId)
    }
}