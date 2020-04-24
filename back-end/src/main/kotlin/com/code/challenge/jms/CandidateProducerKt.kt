package com.code.challenge.jms

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

private val LOGGER = KotlinLogging.logger {}
private const val TOPIC_NAME = "candidate-update"

@Service("candidateProducer")
class CandidateProducerKt(
        @Autowired
        val kafkaTemplate: KafkaTemplate<String, String>
) {

    fun sendMessage(msg: CandidateModel) {
        LOGGER.info("calling send message")
        kafkaTemplate.send(TOPIC_NAME, msg.toString())
        LOGGER.info("published candidate message")
    }

    @PostMapping("/candidate")
    fun postCandidateMessage(
            @RequestBody
            candidateModel: CandidateModel
    ){
        LOGGER.info("received candidate message to put on queue {}", candidateModel)
        sendMessage(candidateModel)
    }
}