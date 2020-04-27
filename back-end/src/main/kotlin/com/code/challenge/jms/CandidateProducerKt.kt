package com.code.challenge.jms

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

private val LOGGER = KotlinLogging.logger {}
private const val TOPIC_NAME = "candidate-update"

@Service("candidateProducer")
@RestController()
class CandidateProducerKt(
        @Autowired
        @Qualifier("candidateTemplate")
        val candidateTemplate: KafkaTemplate<String, CandidateModel>,
        @Autowired
        val processor: Processor
) {

    fun sendMessage(msg: CandidateModel) {
        LOGGER.info("calling send message")
        candidateTemplate.send(TOPIC_NAME, UUID.randomUUID().toString(), msg)
        LOGGER.info("published candidate message")
    }

    @PostMapping("/candidate")
    open fun postCandidateMessage(
            @RequestBody
            candidateModel: CandidateModel
    ){
        LOGGER.info("received candidate message to put on queue {}", candidateModel)
//        sendMessage(candidateModel)
        processor.candidateUpdate().send(MessageBuilder.withPayload(Gson().toJson(candidateModel)).build())
    }
}