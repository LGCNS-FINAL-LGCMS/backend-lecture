package com.lgcms.lecture.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileService {
    @Async
    public void saveImage(MultipartFile thumbnail, String lectureId) {
    }

    @Async
    public void saveVideo(MultipartFile file, String lectureId) {
    }
}
