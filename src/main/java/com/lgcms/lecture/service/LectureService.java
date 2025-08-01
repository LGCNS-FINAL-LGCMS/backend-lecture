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
import com.lgcms.lecture.dto.response.lecture.LectureResponseDto;
import com.lgcms.lecture.repository.LectureEnrollmentRepository;
import com.lgcms.lecture.repository.LectureRepository;
import com.lgcms.lecture.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
                        .description(lectureRequestDto.getDescription())
                        .nickname(lectureRequestDto.getNickname())
                        .category(lectureRequestDto.getCategory())
                        .price(lectureRequestDto.getPrice())
                        .title(lectureRequestDto.getTitle())
                        .level(lectureRequestDto.getLevel())
                        .information(lectureRequestDto.getInformation())
                        .createdAt(LocalDateTime.now())
                .build());

        joinLecture(memberId,lectureId);

        return lectureId;
    }

    @Transactional
    public Page<LectureResponseDto> getLectureList(Pageable pageable, String keyword, String category) {
        Page<Lecture> lectureList;
//                = lectureRepository.findAll(pageable);
        if(keyword != null && category != null && !keyword.isBlank() && !category.isBlank()){
            lectureList = lectureRepository.findAllByKeywordAndCategory(keyword,category,pageable);
        }else if(keyword != null && !keyword.isBlank()){
            lectureList = lectureRepository.findByKeyword(keyword, pageable);
        }else if(category != null && !category.isBlank()){
            lectureList = lectureRepository.findByCategoryAsPage(category, pageable);
        }else{
            lectureList = lectureRepository.findAll(pageable);
        }
        return lectureList
                .map(lecture -> LectureResponseDto.builder()
                                .lectureId(lecture.getId())
                                .description(lecture.getDescription())
                                .nickname(lecture.getNickname())
                                .title(lecture.getTitle())
                                .price(lecture.getPrice())
                                .thumbnail(lecture.getThumbnail())
                                .information(lecture.getInformation())
                                .build()
                                                );
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
                .nickname(lecture.getNickname())
                .description(lecture.getDescription())
                .lectureId(lecture.getId())
                .thumbnail(lecture.getThumbnail())
                .title(lecture.getTitle())
                .information(lecture.getInformation())
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

    //강사 인증
    @Transactional
    public boolean isLecturer(Long memberId, String lectureId){
        return lectureRepository.existsByIdAndMemberId(lectureId, memberId);
    }

    @Transactional
    public List<LectureResponseDto> getLectureByCategory(String category) {
        List<Lecture> lectureList = lectureRepository.findByCategory(category);
        return lectureList.stream()
                .map(lecture -> LectureResponseDto.builder()
                        .lectureId(lecture.getId())
                        .description(lecture.getDescription())
                        .nickname(lecture.getNickname())
                        .thumbnail(lecture.getThumbnail())
                        .price(lecture.getPrice())
                        .title(lecture.getTitle())
                        .build()
                ).toList();
    }

    @Transactional
    public List<LectureResponseDto> getLectureByKeyword(String keyword) {
        List<Lecture> lectureList = lectureRepository.findByTitleContaining(keyword);
        return lectureList.stream()
                .map(
                        lecture -> LectureResponseDto.builder()
                                .title(lecture.getTitle())
                                .description(lecture.getDescription())
                                .nickname(lecture.getNickname())
                                .price(lecture.getPrice())
                                .lectureId(lecture.getId())
                                .thumbnail(lecture.getThumbnail())
                                .build()
                ).toList();
    }
}
