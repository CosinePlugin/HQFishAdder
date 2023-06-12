package kr.hqservice.fish.extension

import org.bukkit.entity.Player

fun Player.sendMessages(vararg messages: String?) {
    messages.filterNotNull().forEach(::sendMessage)
}