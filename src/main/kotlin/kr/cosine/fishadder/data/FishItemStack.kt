package kr.cosine.fishadder.data

import kr.hqservice.framework.nms.extension.getNmsItemStack
import kr.hqservice.framework.nms.extension.nms
import org.bukkit.inventory.ItemStack

class FishItemStack(
    private val itemStack: ItemStack
) {

    private val tag get() = itemStack.getNmsItemStack().getTag()

    private var chance = tag.getDouble(CHANCE_KEY)

    fun getChance(): Double {
        return chance
    }

    fun setChance(chance: Double) {
        itemStack.nms {
            tag {
                setDouble(CHANCE_KEY, chance)
                this@FishItemStack.chance = chance
            }
        }
    }

    fun toItemStack(): ItemStack {
        return itemStack.clone()
    }

    fun toOriginalItemStack(): ItemStack {
        return toItemStack().nms {
            tag {
                remove(CHANCE_KEY)
            }
        }
    }

    companion object {
        private const val CHANCE_KEY = "HQFishAdderChance"

        fun of(itemStack: ItemStack): FishItemStack {
            return FishItemStack(
                itemStack.nms {
                    tag {
                        setDouble(CHANCE_KEY, 100.0)
                    }
                }
            )
        }
    }
}