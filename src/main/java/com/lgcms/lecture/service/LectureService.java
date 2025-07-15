package com.lgcms.lecture.service;

import com.lgcms.lecture.domain.Lecture;
import com.lgcms.lecture.domain.type.LectureStatus;
import com.lgcms.lecture.domain.type.VideoStatus;
import com.lgcms.lecture.dto.request.LectureRequestDto;
import com.lgcms.lecture.dto.request.LectureStatusDto;
import com.lgcms.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureService {

    private final LectureRepository lectureRepository;
    private final FileService fileService;

    @Transactional  //메타정보 저장 --> file service 에 썸네일 동영상 전달 후 처리
    public String saveLecture(LectureRequestDto lectureRequestDto) {
        String lectureId = UUID.randomUUID()+lectureRequestDto.getTitle();
        lectureRepository.save(Lecture.builder()
                        .lectureStatus(LectureStatus.SUBMITTED)
                        .videoStatus(VideoStatus.ENCODING)
                        .id(lectureId)
                        .category(lectureRequestDto.getCategory())
                        .price(lectureRequestDto.getPrice())
                        .title(lectureRequestDto.getTitle())
                        .level(lectureRequestDto.getLevel())
                        .information(lectureRequestDto.getInformation())
                        .createdAt(LocalDateTime.now())
                .build());
        return lectureId;
    }

    public void getLectureList() {
        List<Lecture> lectureList = lectureRepository.findAll();

    }

    @Transactional
    public String modifyLectureStatus(LectureStatusDto lectureStatusDto){
        Lecture lecture = lectureRepository.findById(lectureStatusDto.getLectureId()).orElseThrow();
        lecture.modifyLectureStatus(LectureStatus.valueOf(lectureStatusDto.getLectureStatus()));
        return lectureStatusDto.getLectureStatus();
    }
}
