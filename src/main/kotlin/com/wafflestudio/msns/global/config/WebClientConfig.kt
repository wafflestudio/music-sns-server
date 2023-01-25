package com.wafflestudio.msns.global.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import io.netty.resolver.DefaultAddressResolverGroup
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Configuration
class WebClientConfig {
    @Bean
    fun playlistClient(): WebClient =
        WebClient
            .builder()
            .baseUrl("https://feelin-api-dev.wafflestudio.com/api/v1")
            .clientConnector(
                ReactorClientHttpConnector(
                    HttpClient
                        .create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                        .responseTimeout(Duration.ofSeconds(1))
                        .doOnConnected { conn ->
                            conn
                                .addHandlerLast(ReadTimeoutHandler(5))
                                .addHandlerLast(WriteTimeoutHandler(5))
                        }
                        .resolver(DefaultAddressResolverGroup.INSTANCE)
                )
            )
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()

    @Bean
    fun slackClient(): WebClient =
        WebClient
            .builder()
            .baseUrl("https://hooks.slack.com")
            .clientConnector(
                ReactorClientHttpConnector(
                    HttpClient
                        .create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                        .responseTimeout(Duration.ofSeconds(1))
                        .doOnConnected { conn ->
                            conn
                                .addHandlerLast(ReadTimeoutHandler(5))
                                .addHandlerLast(WriteTimeoutHandler(5))
                        }
                        .resolver(DefaultAddressResolverGroup.INSTANCE)
                )
            )
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
}
