package kr.hqservice.fish.extension

import kr.hqservice.fish.HQFishAdder.Companion.plugin
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

internal fun sync(block: () -> Unit) {
    Bukkit.getScheduler().runTask(plugin, Runnable(block))
}

internal fun async(block: () -> Unit) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable(block))
}

internal fun later(delay: Int = 1, async: Boolean = false, block: () -> Unit = {}): BukkitTask {
    return if (async) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, Runnable(block), delay.toLong())
    } else {
        Bukkit.getScheduler().runTaskLater(plugin, Runnable(block), delay.toLong())
    }
}