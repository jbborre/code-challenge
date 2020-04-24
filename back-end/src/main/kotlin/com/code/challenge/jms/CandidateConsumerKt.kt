package com.code.challenge.jms

import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.cloud.stream.messaging.Sink
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

/*
    Consumer for kafka.  Logs the incoming message and publishes to a transaction-log topic
 */

private val LOGGER = KotlinLogging.logger {}

@Service("candidateConsumer")
class CandidateConsumerKt {

    @KafkaListener(topics = ["candidate-update"])
    @Throws(Exception::class)
    fun listen(cr: ConsumerRecord<*, *>) {
        LOGGER.info(cr.toString())
    }

//    @StreamListener(Sink.INPUT)
//    @StreamListener("candidate-update")
//    fun handle(message: CandidateModel) {
//        LOGGER.info("Received: ${message.toString()}")
//    }
}