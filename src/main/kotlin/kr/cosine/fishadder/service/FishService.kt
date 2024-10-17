package kr.cosine.fishadder.service

import kr.cosine.fishadder.data.FishItemStack
import kr.cosine.fishadder.registry.FishItemStackRegistry
import kr.hqservice.framework.global.core.component.Service
import kotlin.math.ln
import kotlin.random.Random

@Service
class FishService(
    private val fishItemStackRegistry: FishItemStackRegistry
) {
    private val random: Random = Random

    fun findFishItemStackByChance(): FishItemStack? {
        return fishItemStackRegistry.fishItemStacks.minByOrNull {
            -ln(random.nextDouble()) / it.getChance()
        }
    }
}