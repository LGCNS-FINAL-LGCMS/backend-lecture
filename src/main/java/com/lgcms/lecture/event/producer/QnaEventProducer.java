package com.lgcms.lecture.event.producer;

import com.lgcms.lecture.common.kafka.dto.KafkaEvent;
import com.lgcms.lecture.common.kafka.dto.QnaAnswered;
import com.lgcms.lecture.common.kafka.dto.QnaCreated;
import io.micrometer.tracing.Tracer;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class QnaEventProducer {

    @Value("${spring.application.name}")
    private String applicationName;

    private final KafkaTemplate kafkaTemplate;
    private Tracer tracer;

    public void QnaCreated(QnaCreated qnaCreated){

        KafkaEvent kafkaEvent = KafkaEvent.builder()
                .eventId(applicationName + UUID.randomUUID().toString())
                .eventTime(LocalDateTime.now().toString())
                .eventType("QNA_CREATED")
                .data(qnaCreated)
                .build();
        ProducerRecord<String, String> record = new ProducerRecord<>("NOTIFICATION", kafkaEvent.toString());

//        W3CTraceContextPropagator.getInstance()
//            .inject(Context.current(), record.headers(), (headers, key, value) -> {
//                headers.add(key, value.getBytes(StandardCharsets.UTF_8));
//            });
//
//        W3CBaggagePropagator.getInstance()
//                .inject(Context.current(), record.headers(), (headers, key, value) -> {
//                    headers.add(key, value.getBytes(StandardCharsets.UTF_8));
//                });

//        System.out.println("=== Final Kafka Record Headers ===");
//        record.headers().forEach(header -> {
//            System.out.println("Header: " + header.key() + " = " + new String(header.value(), StandardCharsets.UTF_8));
//        });
//        kafkaTemplate.send(record);
        kafkaTemplate.send("NOTIFICATION",kafkaEvent);
    }
    public void QnaAnswered(QnaAnswered qnaAnswered){

        KafkaEvent kafkaEvent = KafkaEvent.builder()
                .eventId(applicationName + UUID.randomUUID().toString())
                .eventTime(LocalDateTime.now().toString())
                .eventType("QNA_ANSWERED")
                .data(qnaAnswered)
                .build();

        kafkaTemplate.send("NOTIFICATION",kafkaEvent);
    }
}
