package kr.cosine.fishadder.view

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.cosine.fishadder.data.FishItemStack
import kr.cosine.fishadder.registry.FishItemStackRegistry
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.bukkit.core.coroutine.bukkitDelay
import kr.hqservice.framework.bukkit.core.coroutine.extension.BukkitMain
import kr.hqservice.framework.bukkit.core.extension.editMeta
import kr.hqservice.framework.inventory.button.HQButtonBuilder
import kr.hqservice.framework.inventory.container.HQContainer
import kr.hqservice.framework.nms.extension.getDisplayName
import kr.hqservice.framework.nms.extension.virtual
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

class FishSettingView(
    private val plugin: HQBukkitPlugin,
    private val fishItemStackRegistry: FishItemStackRegistry
) : HQContainer(54, "물고기 설정") {

    private var page = 0

    private val fishItemStacks get() = fishItemStackRegistry.fishItemStacks
    private var currentFishItemStacks = emptyList<FishItemStack>()

    override fun initialize(inventory: Inventory) {
        inventory.clear()
        currentFishItemStacks = fishItemStacks.drop(FISH_SIZE * page).take(FISH_SIZE)
        currentFishItemStacks.forEachIndexed { slot, fishItemStack ->
            val itemStack = fishItemStack.toItemStack().editMeta {
                lore = (lore ?: emptyList()) + listOf(
                    "",
                    "§a§l| §f확률: §a${fishItemStack.getChance()}",
                    "",
                    "§a좌클릭 §7▸ §f확률을 설정합니다.",
                    "§c쉬프트+우클릭 §7▸ §f목록에서 삭제합니다."
                )
            }
            inventory.setItem(slot, itemStack)
        }
        drawPageButton()
    }

    private fun drawPageButton() {
        HQButtonBuilder(Material.RED_STAINED_GLASS_PANE).apply {
            setRemovable(true)
            setDisplayName("§c이전 페이지로 이동")
            setClickFunction { event ->
                if (event.getClickType() != ClickType.LEFT) return@setClickFunction
                val player = event.getWhoClicked()
                player.playButtonSound()
                if (page == 0) {
                    player.sendMessage("§c이전 페이지가 존재하지 않습니다.")
                    return@setClickFunction
                }
                page--
                refresh()
            }
        }.build().setSlot(this, BEFORE_PAGE_SLOT)

        HQButtonBuilder(Material.LIME_STAINED_GLASS_PANE).apply {
            setRemovable(true)
            setDisplayName("§a다음 페이지로 이동")
            setClickFunction { event ->
                if (event.getClickType() != ClickType.LEFT) return@setClickFunction
                val player = event.getWhoClicked()
                player.playButtonSound()
                if (fishItemStacks.size <= (page + 1) * FISH_SIZE) {
                    player.sendMessage("§c다음 페이지가 존재하지 않습니다.")
                    return@setClickFunction
                }
                page++
                refresh()
            }
        }.build().setSlot(this, NEXT_PAGE_SLOT)

        HQButtonBuilder(Material.WHITE_STAINED_GLASS_PANE).apply {
            setRemovable(true)
            setDisplayName("§r")
        }.build().setSlot(this, backgroundSlots)
    }

    override fun onClick(event: InventoryClickEvent) {
        if (event.clickedInventory == null) return
        val player = event.whoClicked as Player
        if (event.rawSlot < inventory.size) {
            onTopClick(event, player)
        } else {
            if (event.click != ClickType.LEFT) return
            onBottomClick(event, player)
        }
    }

    private val InventoryClickEvent.fishItemStack get() = currentFishItemStacks.getOrNull(rawSlot)

    private fun onTopClick(event: InventoryClickEvent, player: Player) {
        when (event.click) {
            ClickType.LEFT -> {
                val fishItemStack = event.fishItemStack ?: return
                player.playButtonSound()
                setFishChance(player, fishItemStack)
            }

            ClickType.SHIFT_RIGHT -> {
                val fishItemStack = event.fishItemStack ?: return
                fishItemStackRegistry.removeFishItemStack(fishItemStack)
                if (page > 0 && currentFishItemStacks.size != FISH_SIZE && fishItemStacks.size % FISH_SIZE == 0) {
                    page--
                }
                player.playButtonSound()
                refresh()
            }

            else -> {}
        }
    }

    private fun setFishChance(player: Player, fishItemStack: FishItemStack) {
        player.closeInventory()
        player.sendMessage("§a'취소'를 입력 시 설정이 취소됩니다.")
        delayLaunch {
            player.virtual {
                sign {
                    setConfirmHandler {
                        val chanceText = it.firstOrNull() ?: run {
                            player.sendMessage("§c확률을 입력해주세요.")
                            return@setConfirmHandler false
                        }
                        if (chanceText == "취소") {
                            player.sendMessage("§a설정이 취소되었습니다.")
                            reopen(player)
                            return@setConfirmHandler true
                        }
                        val chance = chanceText.toDoubleOrNull() ?: run {
                            player.sendMessage("§c숫자만 입력할 수 있습니다.")
                            return@setConfirmHandler false
                        }
                        if (chance <= 0.0) {
                            player.sendMessage("§c양수만 입력할 수 있습니다.")
                            return@setConfirmHandler false
                        }
                        fishItemStack.setChance(chance)
                        fishItemStackRegistry.isChanged = true
                        player.sendMessage("§a${fishItemStack.toItemStack().getDisplayName()}의 확률을 ${chance}퍼센트로 설정하였습니다.")
                        reopen(player)
                        return@setConfirmHandler true
                    }
                }
            }
        }
    }

    private fun reopen(player: Player) {
        delayLaunch {
            refresh()
            open(player)
        }
    }

    private fun delayLaunch(block: suspend CoroutineScope.() -> Unit) {
        plugin.launch(Dispatchers.BukkitMain) {
            bukkitDelay(1)
            block()
        }
    }

    private fun onBottomClick(event: InventoryClickEvent, player: Player) {
        val itemStack = event.currentItem?.clone() ?: return
        val fishItemStack = FishItemStack.of(itemStack)
        fishItemStackRegistry.addFishItemStack(fishItemStack)
        if (currentFishItemStacks.size == FISH_SIZE) {
            page++
        }
        player.playButtonSound()
        refresh()
    }

    private fun Player.playButtonSound() {
        playSound(location, Sound.UI_BUTTON_CLICK, 1f, 1f)
    }

    private companion object {
        const val FISH_SIZE = 45

        const val BEFORE_PAGE_SLOT = 45
        const val NEXT_PAGE_SLOT = 53
        val backgroundSlots = 46..52
    }
}