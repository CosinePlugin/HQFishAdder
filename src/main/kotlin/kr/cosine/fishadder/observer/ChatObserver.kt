package kr.cosine.fishadder.observer

import org.bukkit.event.player.AsyncPlayerChatEvent

interface ChatObserver {
    fun onChat(event: AsyncPlayerChatEvent)
}