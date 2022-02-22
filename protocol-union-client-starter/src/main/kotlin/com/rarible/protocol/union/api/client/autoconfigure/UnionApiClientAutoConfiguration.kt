package com.rarible.protocol.union.api.client.autoconfigure

import com.rarible.core.application.ApplicationEnvironmentInfo
import com.rarible.protocol.union.api.client.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean

const val UNION_WEB_CLIENT_CUSTOMIZER = "UNION_WEB_CLIENT_CUSTOMIZER"

class UnionApiClientAutoConfiguration(
    private val applicationEnvironmentInfo: ApplicationEnvironmentInfo
) {

    @Autowired(required = false)
    @Qualifier(UNION_WEB_CLIENT_CUSTOMIZER)
    private var webClientCustomizer: WebClientCustomizer = NoopWebClientCustomizer()

    @Bean
    @ConditionalOnMissingBean(UnionApiServiceUriProvider::class)
    fun unionApiServiceUriProvider(
        @Value("\${rarible.core.client.k8s:false}") k8s: Boolean
    ): UnionApiServiceUriProvider {
        return if (k8s)
            K8sUnionApiServiceUriProvider()
        else
            SwarmUnionApiServiceUriProvider(applicationEnvironmentInfo.name)
    }

    @Bean
    @ConditionalOnMissingBean(UnionApiClientFactory::class)
    fun unionApiClientFactory(unionApiServiceUriProvider: UnionApiServiceUriProvider): UnionApiClientFactory {
        val customizer = CompositeWebClientCustomizer(listOf(DefaultUnionWebClientCustomizer(), webClientCustomizer))
        return UnionApiClientFactory(unionApiServiceUriProvider, customizer)
    }
}