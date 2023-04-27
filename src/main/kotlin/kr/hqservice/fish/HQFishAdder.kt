package kr.hqservice.fish

import kr.hqservice.fish.bststs.Metrics
import org.bukkit.plugin.java.JavaPlugin

class HQFishAdder : JavaPlugin() {

    override fun onLoad() {

    }

    override fun onEnable() {
        Metrics(this, 18264)
    }

    override fun onDisable() {

    }
}