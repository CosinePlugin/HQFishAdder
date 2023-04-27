package kr.hqservice.fish.listener

import kr.hqservice.fish.HQFishAdder
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent

class FishListener(plugin: HQFishAdder) : Listener {

    private val fishRepository = plugin.fishRepository
    private val fishes = fishRepository.getFishes()

    @EventHandler
    fun onFishing(event: PlayerFishEvent) {
        if (event.state != PlayerFishEvent.State.CAUGHT_FISH) return

        val caught = event.caught
        if (caught !is Item) return

        if (fishes.isEmpty()) return

        caught.itemStack = fishes.random()
    }
}