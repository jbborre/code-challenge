package com.code.challenge.jms;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.annotation.KafkaListener;

/*
    Consumer for kafka.  Logs the incoming message and publishes to a transaction-log topic
 */
public class CandidateConsumer {

//    public CandidateConsumer(){
//        StreamsBuilder builder = new StreamsBuilder();
////        builder.stream("candidate-update");
//
//        KStream<String, Long> wordCounts = builder.stream(
//                "candidate-update", /* input topic */
//                Consumed.with(
//                        Serdes.String(), /* key serde */
//                        Serdes.Long()   /* value serde */
//                )
//        );
//    }


//    @KafkaListener(topics = "myTopic")
//    public void listen(ConsumerRecord<?, ?> cr) throws Exception {
//        logger.info(cr.toString());
//        latch.countDown();
//    }
}
