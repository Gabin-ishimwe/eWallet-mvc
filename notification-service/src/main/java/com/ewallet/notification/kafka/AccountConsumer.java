package com.ewallet.notification.kafka;

import com.ewallet.notification.event.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountConsumer {

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id")
    public void consume(TransactionEvent transactionEvent){
        // log the event
        log.info("Account event received in the notification service" + transactionEvent.toString());

        // save the event data in the DB
    }
}
