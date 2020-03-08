package com.lavans.klient

import io.vavr.Tuple2
import io.vavr.collection.List
import java.nio.charset.Charset

data class HttpStatus(val code: Int, val message: String)
class HttpResponse(val status: HttpStatus, val headers: MultiMap<String, String>, val contents: ByteArray) {
    constructor(code: Int, message: String, headers: KMap<String, KList<String>>, contents: ByteArray)
        : this(HttpStatus(code, message), MultiMap(headers), contents)

    fun asString() = String(contents, charset())
    override fun toString(): String {
        return "\n${headerString()}"
    }

    private fun charset(): Charset =
        headers["Content-Type"].flatMap { list ->
            list.find { value ->
                value.contains("charset=")
            }.flatMap { Mime.of(it).charset }
        }.getOrElse { Charsets.UTF_8 }

    private fun headerString() = headers.value.map { e -> str(e) }.mkString("\n")
    private fun str(t: Tuple2<String, List<String>>): String {
        val k = if (t._1 == null) "" else "  ${t._1}: "
        val v = t._2.mkString(",")
        return k + v
    }
}

