package io.nullptr.config

import io.nullptr.config.dto.ApolloApplicationConfig
import io.nullptr.util.Requests

class ApolloConfigService(private val baseUrl: String) {

    /**
     * Fetch the latest application config
     */
    fun fetchConfig(appId: String, clusterName: String, namespaceName: String): ApolloApplicationConfig {
        val url = "${baseUrl}/configs/${appId}/${clusterName}/${namespaceName}"
        return Requests.getObject<ApolloApplicationConfig>(url)
    }
}