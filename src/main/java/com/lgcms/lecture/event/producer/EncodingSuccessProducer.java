package com.lgcms.lecture.event.producer;


import com.lgcms.lecture.common.kafka.dto.EncodingStatus;
import com.lgcms.lecture.common.kafka.dto.EncodingSuccess;
import com.lgcms.lecture.common.kafka.dto.KafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class EncodingSuccessProducer {

    @Value("${spring.application.name}")
    private String applicationName;

    private final KafkaTemplate kafkaTemplate;

    public void EncodingStatusNotification(EncodingStatus encodingStatus){
        KafkaEvent kafkaEvent = KafkaEvent.builder()
                .eventId(applicationName + "_" + UUID.randomUUID().toString())
                .eventType("ENCODING_STATUS")
                .eventTime(LocalDateTime.now().toString())
                .data(encodingStatus)
                .build();

        kafkaTemplate.send("NOTIFICATION",kafkaEvent);
    }

}
