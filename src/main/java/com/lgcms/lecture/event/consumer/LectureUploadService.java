package com.lgcms.lecture.event.consumer;

import com.lgcms.lecture.common.kafka.dto.EncodingSuccess;
import com.lgcms.lecture.common.kafka.dto.KafkaEvent;
import com.lgcms.lecture.common.kafka.dto.LectureUploadDto;
import com.lgcms.lecture.common.kafka.util.KafkaEventFactory;
import com.lgcms.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureUploadService {

    private final LectureService lectureService;
    private final KafkaEventFactory kafkaEventFactory;

    @KafkaListener(topics = "LECTURE_UPLOAD", containerFactory = "defaultFactory")
    public void LectureUploadConsume(KafkaEvent<?> event){
        LectureUploadDto lectureUploadDto = kafkaEventFactory.convert(event, LectureUploadDto.class);
        lectureService.updateThumbnailAndTextbook(lectureUploadDto);
     }

    @KafkaListener(topics = "ENCODING_SUCCESS", containerFactory = "defaultFactory")
    public void EncodingSuccess(KafkaEvent<?> event){
        EncodingSuccess encodingSuccess = kafkaEventFactory.convert(event, EncodingSuccess.class);
        lectureService.updateTotalPlaytime(encodingSuccess);
    }
}
