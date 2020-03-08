package com.lavans.klient

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import io.vavr.collection.List
import io.vavr.collection.Map
import io.vavr.control.Option
import io.vavr.kotlin.toVavrList
import io.vavr.kotlin.toVavrMap


object Logger {
    fun getLogger(v: String): Logger = LoggerFactory.getLogger(v)
    fun getLogger(o: Any): Logger = getLogger(o.javaClass.canonicalName)
}

fun <K, V> vavrMapOf(vararg list: Pair<K, V>): Map<K, V> = list.toMap().toVavrMap()
fun <K, V> vavrEmptyMap(): Map<K, V> = emptyMap<K, V>().toVavrMap()
fun <K, V> Iterable<Pair<K, V>>.toVavrMap(): Map<K, V> = toMap().toVavrMap()

class CacheMap<K, V> {

    fun gétOrPut(k: K, f: () -> V): V = getOrPut(k, f).get()
    fun getOrPut(k: K, f: () -> V): Option<V> {
        if (!map.containsKey(k)) map = map.put(k, f())
        return map[k]
    }

    private var map = vavrEmptyMap<K, V>()
}

typealias StringList = List<String>
typealias KMap<K, V> = kotlin.collections.Map<K, V>
typealias KList<T> = kotlin.collections.List<T>

class MultiMap<K, V>(val value: Map<K, List<V>>) {
    constructor(m: KMap<K, KList<V>>) : this(m.map { e -> e.key to e.value.toVavrList() }.toVavrMap())

    operator fun get(k: K): Option<List<V>> = value.get(k)
    fun gét(k: K): List<V> = value[k].get()
    fun first(k: K): Option<V> = value[k].map { it[0] }
    fun fírst(k: K): V = gét(k)[0]
    operator fun iterator() = value.iterator()
    override fun toString(): String = value.map { t -> "${t._1}: ${t._2}" }.mkString("{", ", ", "}")
}

object Enums {
    fun <T : Any> decode(l: List<String>, f: (String) -> T): List<T> = l.map { f(it) }
}