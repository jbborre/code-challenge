package com.code.challenge

import com.code.challenge.jms.CandidateModel
import com.code.challenge.jms.Event
import com.google.gson.Gson
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.context.junit4.SpringRunner
import java.util.*


private val LOGGER = KotlinLogging.logger {}

@RunWith(SpringRunner::class)
@SpringBootTest()
class TestKafka(
        @Qualifier("candidateTemplate")
        val candidateTemplate: KafkaTemplate<String, String>
) {

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
        assertTrue(true)
    }
}