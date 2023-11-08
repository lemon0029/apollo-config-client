package io.nullptr.config.dto

data class ApolloApplicationConfig(
    val appId: String,
    val cluster: String,
    val namespaceName: String,
    val configurations: Map<String, Any>,
    val releaseKey: String
)