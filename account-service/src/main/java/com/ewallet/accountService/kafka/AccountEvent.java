package com.ewallet.accountService.kafka;

import com.ewallet.accountService.event.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountEvent {
    @Autowired
    private NewTopic newTopic;

    @Autowired
    private KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public void sendMessage(TransactionEvent event) {
        log.info("Account event sending message ------>>>>>>>>");

        // create message
        Message<TransactionEvent> message = MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, newTopic.name())
                .build();

        // send message
        kafkaTemplate.send(message);
    }

}
