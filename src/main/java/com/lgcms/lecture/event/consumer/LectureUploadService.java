package com.lgcms.lecture.event.consumer;

import com.lgcms.lecture.common.kafka.dto.KafkaEvent;
import com.lgcms.lecture.common.kafka.dto.LectureUploadDto;
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

    @KafkaListener(topics = "UPLOAD-01", containerFactory = "defaultFactory")
    public void LectureUploadConsume(KafkaEvent event){
        lectureService.updateThumbnailAndTextbook((LectureUploadDto) event.getData());
    }

    @KafkaListener(topics= "UPLOAD-02", containerFactory = "defaultFactory")
    public void TestConsume(KafkaEvent<LectureUploadDto> event){
        LectureUploadDto lectureUploadDto = event.getData();
        System.out.println(lectureUploadDto.getLectureId());
    }
}
