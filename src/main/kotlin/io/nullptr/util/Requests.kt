package io.nullptr.util

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers

object Requests {

    val client: HttpClient = HttpClient.newHttpClient()

    inline fun <reified T> getObject(url: String): T {
        val request = HttpRequest.newBuilder(URI(url)).build()
        val response = client.send(request, BodyHandlers.ofString())
        response.assertOk()

        return Jsons.readValue(response.body())
    }

    fun HttpResponse<*>.assertOk() {
        if (statusCode() / 10 == 20 && body() != null) return

        throw IllegalStateException("Unexpected http response, status code: ${statusCode()}, body: ${body()}")
    }
}