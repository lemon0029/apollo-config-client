package io.nullptr

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.nullptr.config.ApolloConfigService
import io.nullptr.discovery.ApolloMetaService
import io.nullptr.util.Jsons
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class ApolloClient {

    companion object {
        const val DEFAULT_CLUSTER_NAME = "default"
        const val DEFAULT_NAMESPACE_NAME = "application"

        private val EMPTY_PROPERTIES = Properties()

        fun fromConfig(config: ApolloClientConfig): ApolloClient {
            if (config.metaServiceUrl != null) {
                return ApolloClient().apply {
                    metaService = ApolloMetaService(config.metaServiceUrl!!)
                    refreshConfigServices()
                }
            } else if (config.configServicesUrl != null) {
                return ApolloClient().apply {
                    configServices = config.configServicesUrl!!.map {
                        ApolloConfigService(it)
                    }
                }
            }

            throw IllegalArgumentException("Both the meta-service URL and config-services URL cannot be empty.")
        }
    }

    lateinit var metaService: ApolloMetaService
    lateinit var configServices: List<ApolloConfigService>

    val defaultAppConfigLocalCache = ConcurrentHashMap<String, Any>()

    inline fun <reified T : Any> loadDefaultAppConfig(appId: String): T {
        if (defaultAppConfigLocalCache[appId] != null) return defaultAppConfigLocalCache[appId] as T

        val properties = loadConfig(appId, DEFAULT_CLUSTER_NAME, DEFAULT_NAMESPACE_NAME)
        return Jsons.convertValue<T>(properties).also { defaultAppConfigLocalCache[appId] = it }
    }

    fun loadConfig(appId: String, clusterName: String, namespaceName: String): Properties {
        if (!::configServices.isInitialized) return EMPTY_PROPERTIES

        val apolloConfigService = configServices.randomOrNull() ?: return EMPTY_PROPERTIES
        val apolloApplicationConfig = apolloConfigService.fetchConfig(appId, clusterName, namespaceName)
        return jacksonObjectMapper().convertValue(apolloApplicationConfig.configurations, Properties::class.java)
    }

    fun refresh() {
        refreshConfigServices()
        defaultAppConfigLocalCache.clear()
    }

    fun refreshConfigServices() {
        if (!::metaService.isInitialized) return

        val configServiceInstances = metaService.lookupConfigServiceInstances()
        configServices = configServiceInstances.map { ApolloConfigService(baseUrl = it.homepageUrl) }
    }
}