package kr.hqservice.fish

import kr.hqservice.fish.bststs.Metrics
import kr.hqservice.fish.command.FishAdminCommand
import kr.hqservice.fish.listener.FishListener
import kr.hqservice.fish.repository.FishRepository
import kr.hqservice.fish.util.CustomConfig
import org.bukkit.plugin.java.JavaPlugin

class HQFishAdder : JavaPlugin() {

    companion object {
        const val prefix = "§b[ HQFishAdder ]§f"
        lateinit var plugin: HQFishAdder
            private set
    }

    lateinit var fishRepository: FishRepository
        private set

    override fun onLoad() {
        plugin = this
    }

    override fun onEnable() {
        Metrics(this, 18264)

        val fishFile = CustomConfig(this, "fishes.yml")
        fishRepository = FishRepository(fishFile)
        fishRepository.load()

        server.pluginManager.registerEvents(FishListener(this), this)

        getCommand("낚시관리").executor = FishAdminCommand(this)
    }

    override fun onDisable() {

    }
}