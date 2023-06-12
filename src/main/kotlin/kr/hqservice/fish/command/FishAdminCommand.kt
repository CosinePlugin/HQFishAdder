package kr.hqservice.fish.command

import kr.hqservice.fish.HQFishAdder.Companion.prefix
import kr.hqservice.fish.extension.amount
import kr.hqservice.fish.extension.isDouble
import kr.hqservice.fish.extension.sendMessages
import kr.hqservice.fish.inventory.FishListInventory
import kr.hqservice.fish.repository.FishRepository
import kr.ms.core.util.ItemStackNameUtil.getItemName
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class FishAdminCommand(
    private val fishRepository: FishRepository
) : CommandExecutor, TabCompleter {

    companion object {
        private val commandTabList = listOf("설정", "목록", "리로드")
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): List<String> {
        if (args.size <= 1) {
            return commandTabList
        }
        return emptyList()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("$prefix 콘솔에서 사용할 수 없는 명령어입니다.")
            return true
        }
        val player: Player = sender
        if (args.isEmpty()) {
            printHelp(player)
            return true
        }
        checker(player, args)
        return true
    }

    private fun printHelp(player: Player) {
        player.sendMessages(
            "$prefix 낚시 관리 명령어 도움말",
            "",
            "$prefix /낚시관리 설정 [확률] : 손에 들고 있는 물고기의 확률을 설정합니다.",
            "$prefix /낚시관리 목록 : 물고기의 목록을 확인합니다.",
            "$prefix /낚시관리 리로드 : config.yml을 리로드합니다."
        )
    }

    private fun checker(player: Player, args: Array<out String>) {
        when (args[0]) {
            "설정" -> setFish(player, args)

            "목록" -> showFishList(player)

            "리로드" -> reload(player)
        }
    }

    private fun setFish(player: Player, args: Array<out String>) {
        val item = player.inventory.itemInMainHand.clone().amount()
        if (item.type == Material.AIR) {
            player.sendMessage("$prefix 손에 아이템을 들어주세요.")
            return
        }
        if (fishRepository.contains(item)) {
            player.sendMessage("$prefix 이미 등록되어 있는 아이템입니다.")
            return
        }
        if (args.size == 1) {
            player.sendMessage("$prefix 확률을 입력해주세요.")
            return
        }
        val percentText = args[1]
        if (!percentText.isDouble()) {
            player.sendMessage("$prefix 숫자만 입력 가능합니다.")
            return
        }
        val percent = percentText.toDouble()
        fishRepository.setFish(item, percent)
        player.sendMessage("$prefix ${getItemName(item)}의 확률을 $percent%로 설정하였습니다.")
    }

    private fun showFishList(player: Player) {
        FishListInventory(fishRepository).openInventory(player)
    }

    private fun reload(player: Player) {
        fishRepository.reload()
        player.sendMessage("$prefix config.yml이 리로드되었습니다.")
    }
}