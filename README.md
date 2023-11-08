# Simple ApolloConfig Client

## Usage

```kotlin
data class SampleAppProperties(
    val timeout: Long
)

fun main() {
    val apolloClientConfig = ApolloClientConfig().apply {
        metaServiceUrl = "http://127.0.0.1:18080"
        // configServicesUrl = listOf("http://127.0.0.1:18080")
    }

    val apolloClient = ApolloClient.fromConfig(apolloClientConfig)
    
    val rawProperties = apolloClient.loadConfig("sampleApp", "default", "application")
    println(rawProperties)

    repeat(10) {
        val properties = apolloClient.loadDefaultAppConfig<SampleAppProperties>("sampleApp")
        println(properties)
    }
}
```