package kr.hqservice.fish.listener

import kr.hqservice.fish.repository.FishRepository
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent

class FishListener(
    private val fishRepository: FishRepository
) : Listener {

    @EventHandler
    fun onFishing(event: PlayerFishEvent) {
        if (event.state != PlayerFishEvent.State.CAUGHT_FISH) return

        val caught = event.caught
        if (caught !is Item) return

        if (fishRepository.isEmpty()) return

        caught.itemStack = fishRepository.getFish()
    }
}