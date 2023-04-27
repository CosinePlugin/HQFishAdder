package kr.hqservice.fish.command

import kr.hqservice.fish.HQFishAdder
import kr.hqservice.fish.extension.later
import kr.hqservice.fish.inventory.FishInventory
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FishAdminCommand(
    private val plugin: HQFishAdder
) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return true
        val player: Player = sender
        later { FishInventory(plugin).openInventory(player) }
        return true
    }
}