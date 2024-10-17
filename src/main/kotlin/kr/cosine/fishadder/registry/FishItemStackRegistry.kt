package kr.cosine.fishadder.registry

import kr.cosine.fishadder.data.FishItemStack
import kr.hqservice.framework.global.core.component.Bean

@Bean
class FishItemStackRegistry {
    private val _fishItemStacks = mutableListOf<FishItemStack>()
    val fishItemStacks: List<FishItemStack> get() = _fishItemStacks

    var isChanged = false

    fun setFishItemStacks(fishItemStacks: List<FishItemStack>) {
        _fishItemStacks.clear()
        _fishItemStacks.addAll(fishItemStacks)
    }

    fun addFishItemStack(fishItemStack: FishItemStack) {
        _fishItemStacks.add(fishItemStack)
        isChanged = true
    }

    fun removeFishItemStack(fishItemStack: FishItemStack) {
        _fishItemStacks.remove(fishItemStack)
        isChanged = true
    }
}