package kr.cosine.fishadder.command

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.cosine.fishadder.config.FishConfig
import kr.cosine.fishadder.service.FishViewService
import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(label = "물고기관리", isOp = true)
class FishAdminCommand(
    private val fishConfig: FishConfig,
    private val fishViewService: FishViewService
) {
    @CommandExecutor("설정", "물고기 설정 화면을 오픈합니다.", priority = 1)
    fun openFishView(player: Player) {
        fishViewService.openFishSettingView(player)
    }

    @CommandExecutor("저장", "변경된 사항을 수동으로 저장합니다.", priority = 2)
    suspend fun save(sender: CommandSender) {
        withContext(Dispatchers.IO) {
            fishConfig.save()
            sender.sendMessage("§a변경된 사항을 수동으로 저장하였습니다.")
        }
    }
}