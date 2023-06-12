package kr.hqservice.fish

import kr.hqservice.fish.command.FishAdminCommand
import kr.hqservice.fish.listener.FishInventoryListener
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
        val fishFile = CustomConfig(this, "config.yml")
        fishRepository = FishRepository(fishFile)
        fishRepository.load()

        server.pluginManager.registerEvents(FishInventoryListener(), this)
        server.pluginManager.registerEvents(FishListener(fishRepository), this)

        getCommand("낚시관리")?.setExecutor(FishAdminCommand(fishRepository))
    }

    override fun onDisable() {

    }
}