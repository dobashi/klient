package com.lavans.klient

import io.vavr.Tuple2
import io.vavr.collection.List
import io.vavr.collection.Map
import io.vavr.control.Option
import io.vavr.kotlin.none
import io.vavr.kotlin.some
import java.net.HttpURLConnection
import java.net.URL

enum class Method {
    GET, POST, PUT, DELETE, HEAD, OPTIONS;

    companion object {
        private val outputs = List.of<Method>(
            POST,
            PUT
        )
    }

    fun hasOutput(): Boolean = outputs.contains(this)
}

data class Headers(val value: Map<String, String>) {
    //    constructor(x: io.vavr.collection.Map<String, String>):this(x.toJavaMap())
    fun forEach(f: (Tuple2<String, String>) -> Unit): Unit = value.iterator().forEach { f(it) }

    fun add(k: String, v: String) =
        Headers(value.put(k, v))
    override fun toString(): String = value.map { e -> "  ${e._1}: ${e._2}" }.mkString("\n")
}

data class RequestQuery(val value: ParameterMap) {
    fun toParam() = value.toParam()
}

data class RequestBody(val value: String) {
    constructor(params: ParameterMap) : this(params.toParam())
}

typealias ParameterMap = Map<String, String>

//fun ParameterMap.toParam() = if (isEmpty) "" else entries.fold("", { acc, e -> "$acc&${e.key}=${e.value}" }).substring(1)
fun ParameterMap.toParam() = if (isEmpty) "" else fold("", { acc, e -> "$acc&${e._1}=${e._2}" }).substring(1)

data class HttpRequest(val url: URL, val headers: Headers = defaultHeaders, val body: Option<RequestBody> = none()) {
    constructor(url: String, body: RequestBody) : this(URL(url), body = some(body))
    constructor(url: String, query: RequestQuery = RequestQuery(
        vavrEmptyMap()
    )
    ) : this(URL("$url?${query.toParam()}"))

    fun getConnection(): HttpURLConnection = url.openConnection() as HttpURLConnection
    fun addHeader(k: String, v: String) = this.copy(headers = headers.add(k, v))

    companion object {
        val defaultHeaders = Headers(
            vavrMapOf(
                "Content-Type" to "application/json"
            )
        )
    }
}

