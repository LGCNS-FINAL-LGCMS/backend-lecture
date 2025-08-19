package com.lgcms.lecture.common.kafka.config;


import com.lgcms.lecture.common.kafka.dto.KafkaEvent;
import com.lgcms.lecture.common.kafka.dto.LectureUploadDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

@Configuration
public class KafkaListenerConfig {

    private final KafkaConfig kafkaConfig;

    public KafkaListenerConfig(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaEvent> defaultFactory() {
        return kafkaConfig.kafkaListenerContainerFactory(KafkaEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LectureUploadDto> anotherValueFactory() {
        return kafkaConfig.kafkaListenerContainerFactory(LectureUploadDto.class);
    }
}