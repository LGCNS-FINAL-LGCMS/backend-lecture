package com.lgcms.lecture.event.consumer;

import com.lgcms.lecture.common.kafka.dto.KafkaEvent;
import com.lgcms.lecture.common.kafka.dto.ProgressUpdate;
import com.lgcms.lecture.common.kafka.util.KafkaEventFactory;
import com.lgcms.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProgressUpdateConsumer {
    private final KafkaEventFactory kafkaEventFactory;
    private final LectureService lectureService;

    @KafkaListener(topics = "PROGRESS_UPDATED")
    public void ProgressUpdated(KafkaEvent<?> event, Acknowledgment ack){
        ack.acknowledge();
        ProgressUpdate progressUpdate = kafkaEventFactory.convert(event, ProgressUpdate.class);
        lectureService.updateLectureProgress(progressUpdate);
    }
}
