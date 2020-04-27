package com.code.challenge.jms

import com.google.gson.Gson
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.Output
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.cloud.stream.messaging.Sink
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.*

/*
    Consumer for kafka.  Logs the incoming message and publishes to a transaction-log topic
 */

private val LOGGER = KotlinLogging.logger {}

@Service
@EnableBinding(Processor::class)
class CandidateConsumerKt(
        @Autowired
        @Qualifier("auditTemplate")
        val auditTemplate: KafkaTemplate<String, String>
) {

// A simple way to consume from kafka topic but does not use streams!
//    @KafkaListener(topics = ["candidate-update"])
//    @Throws(Exception::class)
//    fun listen(cr: ConsumerRecord<UUID, CandidateModel>) {
//        LOGGER.info("received the following message: {}", cr.toString())
//        auditTemplate.send(
//                "transaction-log",
//                UUID.randomUUID().toString(),
//                Audit(recordId = cr.value().recordId, event = Event.UPDATE, status = Status.SUCCESS))
//        LOGGER.info("published to transaction log topic")
//    }

    @StreamListener(value = "candidate-update")
    @Output("transaction-log")
    fun handle(message: String): String {
        LOGGER.info("Received: ${message}")
        val candidate = Gson().fromJson(message, CandidateModel::class.java)
        return Audit(recordId = candidate.recordId, event = Event.UPDATE, status = Status.SUCCESS).toString()
    }
}