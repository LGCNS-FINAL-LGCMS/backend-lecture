package com.lgcms.lecture.event.producer;

import com.lgcms.lecture.common.kafka.dto.KafkaEvent;
import com.lgcms.lecture.common.kafka.dto.QnaAnswered;
import com.lgcms.lecture.common.kafka.dto.QnaCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class QnaEventProducer {

    @Value("${spring.application.name}")
    private String applicationName;

    private final KafkaTemplate kafkaTemplate;

    public void QnaCreated(QnaCreated qnaCreated) {

        KafkaEvent kafkaEvent = KafkaEvent.builder()
            .eventId(applicationName + UUID.randomUUID().toString())
            .eventTime(LocalDateTime.now().toString())
            .eventType("QNA_CREATED")
            .data(qnaCreated)
            .build();

        kafkaTemplate.send("NOTIFICATION", kafkaEvent);
    }

    public void QnaAnswered(QnaAnswered qnaAnswered) {

        KafkaEvent kafkaEvent = KafkaEvent.builder()
            .eventId(applicationName + UUID.randomUUID().toString())
            .eventTime(LocalDateTime.now().toString())
            .eventType("QNA_ANSWERED")
            .data(qnaAnswered)
            .build();

        kafkaTemplate.send("NOTIFICATION", kafkaEvent);
    }
}
