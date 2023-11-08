package io.nullptr.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

object Jsons {

    val mapper = jacksonObjectMapper()

    inline fun <reified T : Any> readValue(text: String) = mapper.readValue<T>(text)
}