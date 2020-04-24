package com.code.challenge

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.*
import org.springframework.web.client.RestTemplate
import java.util.*


@Configuration
class Config {

    @Value(value = "\${kafka.bootstrapAddress}")
    private val bootstrapAddress: String = "localhost:9092"

    @Bean
    fun admin(): KafkaAdmin {
        val configs = mapOf<String, Any>(
            AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress
        )
        return KafkaAdmin(configs);
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<Any, Any> {
        return DefaultKafkaConsumerFactory(consumerConfigs())
    }

    @Bean
    fun consumerConfigs(): Map<String, Any> {
        val props = mutableMapOf<String, Any>()
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafka.getBrokersAsString())
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.GROUP_ID_CONFIG] = "challenge"
        return props
    }

    @Bean
    fun candidateUpdatetopic(): NewTopic {
        return TopicBuilder.name("candidate-update")
                .partitions(1)
                .replicas(1)
                .compact()
                .build()
    }

    @Bean
    fun producerFactory(): ProducerFactory<String?, String?> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String?, String?>? {
        return KafkaTemplate(producerFactory())
    }
    @Bean
    fun getRestTemplate(): RestTemplate{
        return RestTemplate()
    }

}