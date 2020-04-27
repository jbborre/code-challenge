package com.code.challenge.jms;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Processor {
    @Input("candidate-update")
    SubscribableChannel candidateUpdate();

    @Output("transation-log")
    MessageChannel transactionLog();

    @Output
    MessageChannel anotherOutput();
}
