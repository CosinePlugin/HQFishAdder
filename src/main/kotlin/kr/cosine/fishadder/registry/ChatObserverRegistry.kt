package kr.cosine.fishadder.registry

import kr.cosine.fishadder.observer.ChatObserver
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.UUID

@Bean
class ChatObserverRegistry {
    private val chatObservers = mutableMapOf<UUID, ChatObserver>()

    fun addChatObserver(subscriber: UUID, chatObserver: ChatObserver) {
        chatObservers[subscriber] = chatObserver
    }

    fun removeChatObserver(subscriber: UUID) {
        chatObservers.remove(subscriber)
    }

    fun observe(event: AsyncPlayerChatEvent) {
        chatObservers.forEach {
            it.value.onChat(event)
        }
    }
}