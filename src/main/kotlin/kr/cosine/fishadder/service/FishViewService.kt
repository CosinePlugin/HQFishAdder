package kr.cosine.fishadder.service

import kr.cosine.fishadder.registry.FishItemStackRegistry
import kr.cosine.fishadder.view.FishSettingView
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.global.core.component.Service
import org.bukkit.entity.Player

@Service
class FishViewService(
    private val plugin: HQBukkitPlugin,
    private val fishItemStackRegistry: FishItemStackRegistry
) {

    fun openFishSettingView(player: Player) {
        FishSettingView(plugin, fishItemStackRegistry).open(player)
    }
}