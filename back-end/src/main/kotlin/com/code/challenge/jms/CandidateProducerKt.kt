package com.code.challenge.jms

import com.google.gson.Gson
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
        val candidateTemplate: KafkaTemplate<String, String>,
        @Autowired
        val processor: Processor
) {

    fun sendMessage(msg: CandidateModel) {
        LOGGER.info("calling send message")
        candidateTemplate.send(TOPIC_NAME, UUID.randomUUID().toString(), Gson().toJson(msg))
        LOGGER.info("published candidate message")
    }

    @PostMapping("/candidate")
    fun postCandidateMessage(
            @RequestBody
            candidateModel: CandidateModel
    ){
        LOGGER.info("received candidate message to put on queue {}", candidateModel)
//        sendMessage(candidateModel)
        processor.candidateUpdate().send(MessageBuilder.withPayload(Gson().toJson(candidateModel)).build())
    }

    @PostMapping("/candidate/sample-data")
    fun postSampleData(){
        LOGGER.info("publishing sample data to queue")
        processor.candidateUpdate().send(MessageBuilder.withPayload(Gson().toJson(CandidateModel(
                event = Event.CREATE,
                recordId = 1,
                name = "Joe Programmer"
        ))).build())
        processor.candidateUpdate().send(MessageBuilder.withPayload(Gson().toJson(CandidateModel(
                event = Event.UPDATE,
                recordId = 1,
                name = "Java Programmer"
        ))).build())
        processor.candidateUpdate().send(MessageBuilder.withPayload(Gson().toJson(CandidateModel(
                event = Event.DELETE,
                recordId = 1
        ))).build())
    }
}