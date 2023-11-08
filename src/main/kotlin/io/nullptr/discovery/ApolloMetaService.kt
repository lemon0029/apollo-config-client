package io.nullptr.discovery

import io.nullptr.discovery.dto.ServiceInstance
import io.nullptr.util.Requests

class ApolloMetaService(private val baseUrl: String) {
    companion object {
        const val CONFIG_SERVICE_NAME = "config"
        const val ADMIN_SERVICE_NAME = "admin"
    }

    fun lookupConfigServiceInstances() = lookupServiceInstances(CONFIG_SERVICE_NAME)

    fun lookupAdminServiceInstances() = lookupServiceInstances(ADMIN_SERVICE_NAME)

    private fun lookupServiceInstances(serviceName: String): List<ServiceInstance> {
        val url = "${baseUrl}/services/${serviceName}"

        return Requests.getObject<List<ServiceInstance>>(url)
    }
}