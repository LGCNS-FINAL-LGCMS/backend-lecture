package com.lgcms.lecture.event.consumer;

import com.lgcms.lecture.common.kafka.dto.EncodingSuccess;
import com.lgcms.lecture.common.kafka.dto.KafkaEvent;
import com.lgcms.lecture.common.kafka.util.KafkaEventFactory;
import com.lgcms.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EncodingEventConsumer {

    private final KafkaEventFactory kafkaEventFactory;
    private final LectureService lectureService;

    @KafkaListener(topics = "ENCODING_SUCCESS_LECTURE", containerFactory = "defaultFactory")
    public void EncodingSuccess(KafkaEvent<?> event){
        EncodingSuccess encodingSuccess = kafkaEventFactory.convert(event, EncodingSuccess.class);
        lectureService.updateTotalPlaytime(encodingSuccess);
    }
}
