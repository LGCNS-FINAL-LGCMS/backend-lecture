package com.lgcms.lecture.service;

import com.lgcms.lecture.common.dto.exception.BaseException;
import com.lgcms.lecture.common.dto.exception.LectureError;
import com.lgcms.lecture.common.kafka.dto.EncodingStatus;
import com.lgcms.lecture.common.kafka.dto.EncodingSuccess;
import com.lgcms.lecture.common.kafka.dto.LectureUploadDto;
import com.lgcms.lecture.common.kafka.dto.ProgressUpdate;
import com.lgcms.lecture.domain.Lecture;
import com.lgcms.lecture.domain.LectureEnrollment;
import com.lgcms.lecture.domain.LectureProgress;
import com.lgcms.lecture.domain.Student;
import com.lgcms.lecture.domain.type.EnrollmentStatus;
import com.lgcms.lecture.domain.type.LectureStatus;
import com.lgcms.lecture.dto.request.lecture.LectureModifyDto;
import com.lgcms.lecture.dto.request.lecture.LectureRequestDto;
import com.lgcms.lecture.dto.request.lecture.LectureStatusDto;
import com.lgcms.lecture.dto.response.lecture.LectureInfoResponse;
import com.lgcms.lecture.dto.response.lecture.LectureResponseDto;
import com.lgcms.lecture.dto.response.lesson.LessonResponse;
import com.lgcms.lecture.event.producer.EncodingSuccessProducer;
import com.lgcms.lecture.repository.LectureEnrollmentRepository;
import com.lgcms.lecture.repository.LectureProgressRepository;
import com.lgcms.lecture.repository.LectureRepository;
import com.lgcms.lecture.repository.StudentRepository;
import com.lgcms.lecture.service.internal.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LectureService {

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;
    private final LectureEnrollmentRepository lectureEnrollmentRepository;
    private final LectureProgressRepository lectureProgressRepository;
    private final LessonService lessonService;
    private final EncodingSuccessProducer encodingSuccessProducer;

    @Transactional  //메타정보 저장 --> file service 에 썸네일 동영상 전달 후 처리
    public String saveLecture(LectureRequestDto lectureRequestDto, Long memberId) {
        String lectureId = UUID.randomUUID() + "_" + lectureRequestDto.getTitle();
        lectureRepository.save(Lecture.builder()
                .lectureStatus(LectureStatus.HIDDEN)
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

        joinLecture(memberId, lectureId);

        return lectureId;
    }

    @Transactional
    public Page<LectureResponseDto> getLectureList(Pageable pageable, String keyword, String category) {
        Page<Lecture> lectureList;
        if (keyword != null && category != null && !keyword.isBlank() && !category.isBlank()) {
            lectureList = lectureRepository.findAllByKeywordAndCategory(keyword, category, pageable);
        } else if (keyword != null && !keyword.isBlank()) {
            lectureList = lectureRepository.findByKeyword(keyword, pageable);
        } else if (category != null && !category.isBlank()) {
            lectureList = lectureRepository.findByCategoryAsPage(category, pageable);
        } else {
            lectureList = lectureRepository.findAllLecture(pageable);
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
                        .averageStar(lecture.getAverageStar())
                        .reviewCount(lecture.getReviewCount())
                        .build()
                );
    }

    @Transactional
    public String modifyLectureStatus(LectureStatusDto lectureStatusDto) {
        Lecture lecture = lectureRepository.findById(lectureStatusDto.getLectureId())
                .orElseThrow(() -> new BaseException(LectureError.LECTURE_NOT_FOUND));
        List<LessonResponse> lessonResponses = lessonService.getLessons(lecture.getId());
        if(lessonResponses == null || lessonResponses.isEmpty()) throw new BaseException(LectureError.LECTURE_ENROLL_FORBIDDEN);

        lecture.modifyLectureStatus();

        return lectureStatusDto.getLectureStatus();
    }

    @Transactional
    public LectureInfoResponse getLecture(String lectureId, Long memberId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BaseException(LectureError.LECTURE_NOT_FOUND));

        Optional<LectureProgress> lectureProgress = lectureProgressRepository.findByMemberIdAndLectureId(memberId, lectureId);
        Integer progress = null;

        if(lectureProgress.isPresent()) progress = lectureProgress.get().getProgress();

        boolean isStudent = isStudent(memberId, lectureId);

        List<LessonResponse> lessonResponses = lessonService.getLessons(lectureId);

        LectureResponseDto lectureResponseDto = LectureResponseDto.builder()
                .price(lecture.getPrice())
                .nickname(lecture.getNickname())
                .averageStar(lecture.getAverageStar())
                .description(lecture.getDescription())
                .reviewCount(lecture.getReviewCount())
                .lectureId(lecture.getId())
                .thumbnail(lecture.getThumbnail())
                .textbook(lecture.getTextbook())
                .title(lecture.getTitle())
                .information(lecture.getInformation())
                .build();



        return LectureInfoResponse.builder()
                .lectureResponseDto(lectureResponseDto)
                .lessonResponses(lessonResponses)
                .isStudent(isStudent)
                .progress(progress)
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
    public String joinLecture(Long memberId, String lectureId) {
        if(isStudent(memberId, lectureId)) throw new BaseException(LectureError.LECTURE_DUPLICATED);

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BaseException(LectureError.LECTURE_NOT_FOUND));

        LectureEnrollment lectureEnrollment = LectureEnrollment.builder()
                .enrollmentStatus(EnrollmentStatus.PENDING)
                .lecture(lecture)
                .createdAt(LocalDateTime.now())
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

        LectureProgress lectureProgress = LectureProgress.builder()
                .lectureId(lectureId)
                .progress(0)
                .memberId(memberId)
                .build();
        lectureProgressRepository.save(lectureProgress);

        return lecture.getId();
    }

    //수강생 인증
    @Transactional
    public boolean isStudent(Long memberId, String lectureId) {

        return lectureEnrollmentRepository.existsByLectureIdAndStudent_MemberId(lectureId, memberId);
    }

    //강사 인증
    @Transactional
    public boolean isLecturer(Long memberId, String lectureId) {
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

    @Transactional
    public Page<LectureResponseDto> getStudentLectures(Long memberId, Pageable pageable) {

        Page<LectureEnrollment> enrollments = lectureEnrollmentRepository.findByMemberIdWithLecture(memberId, pageable);
        List<String> lecturedIds = enrollments.stream()
                .map(e -> e.getLecture().getId())
                .toList();
        Map<String, Integer> progressMap = lectureProgressRepository
                .findAllByMemberIdAndLectureIdIn(memberId, lecturedIds)
                .stream()
                .collect(Collectors.toMap(LectureProgress::getLectureId, LectureProgress::getProgress));

        return enrollments.map(enrollment -> LectureResponseDto.builder()
                .thumbnail(enrollment.getLecture().getThumbnail())
                .title(enrollment.getLecture().getTitle())
                .lectureId(enrollment.getLecture().getId())
                .nickname(enrollment.getLecture().getNickname())
                .progress(progressMap.getOrDefault(enrollment.getLecture().getId(),0))
                .build()
        );
    }

    @Transactional
    public void updateThumbnailAndTextbook(LectureUploadDto lectureUploadDto) {
        Lecture lecture = lectureRepository.findById(lectureUploadDto.getLectureId())
                .orElseThrow(() -> new BaseException(LectureError.LECTURE_NOT_FOUND));
        lecture.updateThumbnailAndTextbook(lectureUploadDto.getThumbnailKey(), lectureUploadDto.getBookKey());
    }

    @Transactional
    public Page<LectureResponseDto> getLecturerLectures(Long memberId, Pageable pageable) {
        Page<Lecture> lectures = lectureRepository.findAllByMemberId(memberId, pageable);
        return lectures.map(lecture -> LectureResponseDto.builder()
                .lectureId(lecture.getId())
                .description(lecture.getDescription())
                .nickname(lecture.getNickname())
                .title(lecture.getTitle())
                .price(lecture.getPrice())
                .thumbnail(lecture.getThumbnail())
                .information(lecture.getInformation())
                .status(lecture.getLectureStatus().toString())
                .averageStar(lecture.getAverageStar())
                .reviewCount(lecture.getReviewCount())
                .build());
    }

    @Transactional
    public void updateTotalPlaytime(EncodingSuccess encodingSuccess) {
        Lecture lecture = lectureRepository.findById(encodingSuccess.getLectureId())
                .orElseThrow(() -> new BaseException(LectureError.LECTURE_NOT_FOUND));
        lecture.updateTotalPlaytime(encodingSuccess.getDuration());

        String[] parts = encodingSuccess.getLectureId().split("_", 2); // limit=2 로 나누기
        String lectureName = parts[1];

        EncodingStatus encodingStatus = EncodingStatus.builder()
                .lectureId(encodingSuccess.getLectureId())
                .memberId(encodingSuccess.getMemberId())
                .lectureName(lectureName)
                .status("성공")
                .build();

        encodingSuccessProducer.EncodingStatusNotification(encodingStatus);
    }

    @Transactional
    public void updateLectureProgress(ProgressUpdate progressUpdate) {
        Lecture lecture = lectureRepository.findById(progressUpdate.getLectureId())
                .orElseThrow(() -> new BaseException(LectureError.LECTURE_NOT_FOUND));

        int percent = progressUpdate.getPlaytime() / lecture.getTotalPlaytime() * 100;
        log.info("진도율 퍼센트 : {}",percent);
        LectureProgress progress = lectureProgressRepository.findByMemberIdAndLectureId(progressUpdate.getMemberId(), progressUpdate.getLectureId())
                .orElseThrow(()-> new BaseException(LectureError.LECTURE_NOT_FOUND));

        progress.updateProgress(percent);
        progress.updateLastWatched(progressUpdate.getLessonId());

        lectureProgressRepository.save(progress);
    }

    @Transactional
    public void withdrawLecture(Long memberId, String lectureId) {
        lectureEnrollmentRepository.deleteByMemberIdAndLectureId(memberId, lectureId);
    }
}
