package kr.hqservice.fish.extension

import java.lang.IllegalArgumentException
import java.util.AbstractMap
import kotlin.math.ln
import kotlin.random.Random

internal val random: Random by lazy { Random }

internal inline fun <reified T> Map<T, Double>.percent(): T {
    val entry = entries.stream()
    return entry
        .map { e -> AbstractMap.SimpleEntry(e.key, -ln(random.nextDouble()) / e.value) }
        .min(compareBy { it.value } )
        .orElseThrow { IllegalArgumentException() }.key
}

fun String.isDouble(): Boolean {
    return try {
        this.toDouble()
        true
    } catch (e: NumberFormatException) {
        false
    }
}