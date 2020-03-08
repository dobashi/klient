package com.lavans.klient

import io.vavr.control.Option
import io.vavr.kotlin.none
import io.vavr.kotlin.option
import io.vavr.kotlin.some
import java.nio.charset.Charset

data class Mime(val type: ContentType, val charset: Option<Charset>) {
    companion object {
        fun of(x: String): Mime {
            val list = x.split(";").map { it.trim() }
            val type = ContentType.óf(list[0])
            val charset = if (hasCharset(list)) some(charset(list[1])) else none()
            return Mime(type, charset)
        }

        private fun hasCharset(list: List<String>) = list.size > 1 && list[1].startsWith("charset=")
        private fun charset(s: String) = Charset.forName(s.split("=")[1].trim())
    }
}

enum class ContentType(val value: String) {
    TextPlain("text/plain"),
    TextHtml("text/html"),
    ApplicationJson("application/json");

    companion object {
        fun of(x: String) = values().find { it.value == x }.option()
        fun óf(x: String) = of(x).get()
    }
}
