package com.code.challenge

import com.code.challenge.jms.CandidateModel
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.processor.Processor
import org.apache.kafka.streams.processor.ProcessorContext
import org.apache.kafka.streams.processor.ProcessorSupplier
import org.apache.kafka.streams.state.KeyValueStore
import org.apache.kafka.streams.state.StoreBuilder
import org.apache.kafka.streams.state.Stores
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.*
import org.springframework.web.client.RestTemplate
import java.util.*
import java.util.function.Consumer


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
    fun candidateProducerFactory(): ProducerFactory<String, String> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun auditProducerFactory(): ProducerFactory<String, String> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean("candidateTemplate")
    fun candidateTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(candidateProducerFactory())
    }

    @Bean("auditTemplate")
    fun auditTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(auditProducerFactory())
    }

    @Bean
    fun getRestTemplate(): RestTemplate{
        return RestTemplate()
    }

    @Bean
    fun myStore(): StoreBuilder<*>? {
        return Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore("transaction-log-store"), Serdes.Long(),
                Serdes.String())
    }

    var state1: KeyValueStore<Long, String>? = null


//    @Bean
//    fun process(): Consumer<KStream<Any, String>> {
//        return Consumer { input: KStream<Any, String> ->
//            input.process(ProcessorSupplier<Any, String> {
//                object : Processor<Any?, String?> {
//                    override fun init(context: ProcessorContext) {
//                        state1 = context.getStateStore("transaction-log-store") as KeyValueStore<Long, String>
//                    }
//
//                    override fun process(key: Any?, value: String?) {
//                        // processing code
//                    }
//
//                    override fun close() {
//                        if (state1 != null) {
//                            state1?.close()
//                        }
//                    }
//                }
//            }, "transaction-log-store")
//        }
//    }

}