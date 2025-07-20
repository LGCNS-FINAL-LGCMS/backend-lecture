package com.lgcms.lecture.service;

import com.lgcms.lecture.common.dto.exception.BaseException;
import com.lgcms.lecture.common.dto.exception.LectureError;
import com.lgcms.lecture.domain.Lecture;
import com.lgcms.lecture.domain.LectureEnrollment;
import com.lgcms.lecture.domain.Student;
import com.lgcms.lecture.domain.type.EnrollmentStatus;
import com.lgcms.lecture.domain.type.ImageStatus;
import com.lgcms.lecture.domain.type.LectureStatus;
import com.lgcms.lecture.domain.type.VideoStatus;
import com.lgcms.lecture.dto.request.lecture.LectureModifyDto;
import com.lgcms.lecture.dto.request.lecture.LectureRequestDto;
import com.lgcms.lecture.dto.request.lecture.LectureStatusDto;
import com.lgcms.lecture.dto.response.LectureResponseDto;
import com.lgcms.lecture.repository.LectureEnrollmentRepository;
import com.lgcms.lecture.repository.LectureRepository;
import com.lgcms.lecture.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureService {

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final LectureEnrollmentRepository lectureEnrollmentRepository;

    @Transactional  //메타정보 저장 --> file service 에 썸네일 동영상 전달 후 처리
    public String saveLecture(LectureRequestDto lectureRequestDto, Long memberId) {
        String lectureId = UUID.randomUUID()+lectureRequestDto.getTitle();
        lectureRepository.save(Lecture.builder()
                        .lectureStatus(LectureStatus.SUBMITTED)
                        .imageStatus(ImageStatus.ENCODING)
                        .videoStatus(VideoStatus.ENCODING)
                        .id(lectureId)
                        .memberId(memberId)
                        .category(lectureRequestDto.getCategory())
                        .price(lectureRequestDto.getPrice())
                        .title(lectureRequestDto.getTitle())
                        .level(lectureRequestDto.getLevel())
                        .information(lectureRequestDto.getInformation())
                        .createdAt(LocalDateTime.now())
                .build());
        return lectureId;
    }

    @Transactional
    public List<LectureResponseDto> getLectureList() {
        List<Lecture> lectureList = lectureRepository.findAll();

        return lectureList.stream()
                .map(lecture -> LectureResponseDto.builder()
                                .lectureId(lecture.getId())
                                .title(lecture.getTitle())
                                .price(lecture.getPrice())
                                .thumbnail(lecture.getThumbnail())
                                .build()
                                                )
                .toList();
    }

    @Transactional
    public String modifyLectureStatus(LectureStatusDto lectureStatusDto){
        Lecture lecture = lectureRepository.findById(lectureStatusDto.getLectureId()).orElseThrow(() -> new BaseException(LectureError.LECTURE_NOT_FOUND));
        lecture.modifyLectureStatus(LectureStatus.valueOf(lectureStatusDto.getLectureStatus()));
        return lectureStatusDto.getLectureStatus();
    }

    @Transactional
    public LectureResponseDto getLecture(String lectureId, Long memberId) {
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(() -> new BaseException(LectureError.LECTURE_NOT_FOUND));

        return LectureResponseDto.builder()
                .price(lecture.getPrice())
                .lectureId(lecture.getId())
                .thumbnail(lecture.getThumbnail())
                .title(lecture.getTitle())
                .build();
    }

    @Transactional
    public String modifyLecture(LectureModifyDto lectureModifyDto, Long memberId, String lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BaseException(LectureError.LECTURE_FORBIDDEN));
        lecture.modifyLecture(lectureModifyDto);
        return lecture.getId();
    }

    @Transactional
    public String joinLecture(Long memberId, String lectureId){
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BaseException(LectureError.LECTURE_NOT_FOUND));

        LectureEnrollment lectureEnrollment = LectureEnrollment.builder()
                .enrollmentStatus(EnrollmentStatus.ENROLLED)
                .lecture(lecture)
                .enrolledAt(LocalDateTime.now())
                .memberId(memberId)
                .build();

        Student student = studentRepository.findByMemberId(memberId)
                .orElseGet(() -> {
                    Student newStudent = Student.builder()
                            .memberId(memberId)
                            .enrollments(new ArrayList<>())
                            .build();
                    return studentRepository.save(newStudent);
                });
        student.addEnrollment(lectureEnrollment);

        lectureEnrollmentRepository.save(lectureEnrollment);

        return lecture.getId();
    }

    //수강생 인증
    @Transactional
    public boolean isExist(Long memberId, String lectureId) {

        return lectureEnrollmentRepository.existsByLectureIdAndStudent_MemberId(lectureId, memberId);
    }
}
