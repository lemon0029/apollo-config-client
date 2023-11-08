package io.nullptr.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object Jsons {

    val mapper = jacksonObjectMapper()

    inline fun <reified T : Any> readValue(text: String) = mapper.readValue<T>(text)

    inline fun <reified T : Any> convertValue(from: Any): T = mapper.convertValue(from, T::class.java)
}