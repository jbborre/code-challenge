package com.code.challenge

import com.code.challenge.jms.CandidateModel
import com.code.challenge.jms.Event
import com.google.gson.Gson
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import org.springframework.kafka.listener.MessageListener
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


private val LOGGER = KotlinLogging.logger {}

@SpringBootTest
class TestKafka(
        @Qualifier("candidateTemplate")
        val candidateTemplate: KafkaTemplate<String, String>
) {

    private fun createProducer(): Producer<Long?, String?>? {
        val props = Properties()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:29092"
        props[ProducerConfig.CLIENT_ID_CONFIG] = "KafkaExampleProducer"
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = LongSerializer::class.java.getName()
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.getName()
        return KafkaProducer(props)
    }

    @Test
    fun `publish candidates`() {
        candidateTemplate.send("candidate-update", UUID.randomUUID().toString(),
                Gson().toJson(CandidateModel(
                        event = Event.CREATE,
                        recordId = 1,
                        name = "Joe Programmer"
                )))
        candidateTemplate.send("candidate-update", UUID.randomUUID().toString(),
                Gson().toJson(CandidateModel(
                        event = Event.UPDATE,
                        recordId = 1,
                        name = "Java Programmer"
                )))
        candidateTemplate.send("candidate-update", UUID.randomUUID().toString(),
                Gson().toJson(CandidateModel(
                        event = Event.DELETE,
                        recordId = 1
                )))
    }

    //    @Test
    fun `test kafa publish`() {

        val producer = createProducer()

        val time = System.currentTimeMillis()

        try {

            val record = ProducerRecord("candidate-update", 0L,
                    "Hello Mom $0")
            val metadata = producer!!.send(record).get()
            val elapsedTime = System.currentTimeMillis() - time
            System.out.printf("""
    sent record(key=%s value=%s) meta(partition=%d, offset=%d) time=%d
    
    """.trimIndent(),
                    record.key(), record.value(), metadata.partition(),
                    metadata.offset(), elapsedTime)
        } finally {
            producer!!.flush()
            producer.close()
        }
    }

    private fun createContainer(
            containerProps: ContainerProperties): KafkaMessageListenerContainer<Int, String> {
        val props: Map<String, Any> = consumerProps()
        val cf = DefaultKafkaConsumerFactory<Int, String>(props)
        return KafkaMessageListenerContainer(cf, containerProps)
    }


    private fun createTemplate(): KafkaTemplate<Int, String> {
        val senderProps = senderProps()
        val pf: ProducerFactory<Int, String> = DefaultKafkaProducerFactory(senderProps)
        return KafkaTemplate(pf)
    }

    private fun consumerProps(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:29092"
        props[ConsumerConfig.GROUP_ID_CONFIG] = "code-challenge"
        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = true
        props[ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG] = "100"
        props[ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG] = "15000"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = IntegerDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return props
    }

    private fun senderProps(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:29092"
        props[ProducerConfig.RETRIES_CONFIG] = 0
        props[ProducerConfig.BATCH_SIZE_CONFIG] = 16384
        props[ProducerConfig.LINGER_MS_CONFIG] = 1
        props[ProducerConfig.BUFFER_MEMORY_CONFIG] = 33554432
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = IntegerSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        return props
    }

    //    @Test
    fun `test kafka using spring`() {
        LOGGER.info("Start auto")
        val containerProps = ContainerProperties("candidate-upate")
        val latch = CountDownLatch(4)
        containerProps.messageListener = MessageListener<Int?, String?> { message ->
            LOGGER.info("received: $message")
            latch.countDown()
        }
        val container: KafkaMessageListenerContainer<Int, String> = createContainer(containerProps)
        container.setBeanName("testAuto")
        container.start()
        Thread.sleep(1000) // wait a bit for the container to start

        val template: KafkaTemplate<Int, String> = createTemplate()
        template.defaultTopic = "candidate-update"
        template.send("candidate-update", "hi")
//        template.sendDefault(0, "foo")
//        template.sendDefault(2, "bar")
//        template.sendDefault(0, "baz")
//        template.sendDefault(2, "qux")
        template.flush()
        assertTrue(latch.await(60, TimeUnit.SECONDS))
        container.stop()
        LOGGER.info("Stop auto")
    }
}