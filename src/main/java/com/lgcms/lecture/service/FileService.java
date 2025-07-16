package com.lgcms.lecture.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileService {
 //파일 저장 로직 아직 생각 중...
    @Async
    public void saveImage(MultipartFile thumbnail, String lectureId, Long memberId) {
        if(thumbnail == null) return;

    }

    @Async
    public void saveVideo(MultipartFile file, String lectureId, Long memberId) {
        if(file == null) return;
    }
}
