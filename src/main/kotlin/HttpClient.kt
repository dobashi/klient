package com.exwzd.dia.common.http.client

import com.exwzd.dia.common.Logger
import java.io.PrintStream
import java.net.HttpURLConnection
import java.util.zip.GZIPInputStream
import javax.servlet.http.Cookie

/**
 * HttpClient.get("https://www.google.com").asString()
 *
 * val req = HttpRequest(URL("https://www.google.com"), RequestBody("{ key1: "value1"}))
 * val res = HttpClient().post(req)
 * res.content
 * res.asString()
 */
class HttpClient() {
    private val log = Logger.getLogger(this)
//    private val cookie: Cookie

    fun get(request: HttpRequest): HttpResponse = request(Method.GET, request)

    fun post(request: HttpRequest): HttpResponse = request(Method.POST, request)
    fun put(request: HttpRequest): HttpResponse = request(Method.PUT, request)
    fun delete(request: HttpRequest): HttpResponse = request(Method.DELETE, request)

    private fun request(method: Method, request: HttpRequest): HttpResponse {
        log.debug("Request\n$method ${request.url}\n${request.headers}")
        val con = request.getConnection()
        connect(con, method, request)
        if (method.hasOutput()) {
            log.debug("has output")
            PrintStream(con.outputStream).use { output ->
                log.info(request.body.get().value)
                output.print(request.body.get().value)
                output.flush()
            }
        }
        val res = stream(con).use { input ->
            HttpResponse(con.responseCode, con.responseMessage, con.headerFields, input.readAllBytes())
        }
        log.debug("Response $res")
        return res
    }

    private fun connect(con: HttpURLConnection, method: Method, request: HttpRequest) {
        con.requestMethod = method.name
        request.headers.forEach { e -> con.setRequestProperty(e._1, e._2) }
        con.doOutput = method.hasOutput()
        con.connectTimeout = timeout
        con.readTimeout = timeout
        con.connect()
    }

    private fun stream(con: HttpURLConnection) = if (isGzipped(con)) GZIPInputStream(con.inputStream) else con.inputStream
    private fun isGzipped(con: HttpURLConnection) = con.headerFields["Content-Encoding"]?.contains("gzip") ?: false

    companion object {
        var timeout: Int = 2000 // milliseconds
        fun get(s: String): String = HttpClient().get(HttpRequest(s)).asString()
    }
}


