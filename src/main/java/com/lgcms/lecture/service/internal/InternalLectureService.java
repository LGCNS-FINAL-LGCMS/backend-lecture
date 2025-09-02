package com.lgcms.lecture.service.internal;

import com.lgcms.lecture.domain.LectureEnrollment;
import com.lgcms.lecture.dto.internal.*;
import com.lgcms.lecture.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternalLectureService {

    private final LectureRepository lectureRepository;
    private final LectureEnrollmentRepository lectureEnrollmentRepository;
    private final ReviewRepository reviewRepository;
    private final LectureQuestionRepository lectureQuestionRepository;
    private final LectureProgressRepository lectureProgressRepository;

    @Transactional
    public List<LectureMetaResponse> findAllLectures() {

        return lectureRepository.findAll().stream()
                .map(lecture -> new LectureMetaResponse(lecture.getId(),lecture.getMemberId(),
                        lecture.getTitle(),lecture.getLevel(),lecture.getPrice(),lecture.getReviewCount(),lecture.getTotalAmount()))
                .toList();
    }

    @Transactional
    public List<LectureQuestionsResponse> findAllQuestions() {

        return lectureQuestionRepository.findAll().stream()
                .map(lectureQuestion -> new LectureQuestionsResponse(lectureQuestion.getId(),lectureQuestion.getMemberId(),
                        lectureQuestion.getLectureId(),lectureQuestion.getTitle(),lectureQuestion.getContent(),
                        lectureQuestion.getCreatedAt()))
                .toList();
    }

    @Transactional
    public List<LectureProgressResponse> findAllProgress() {
        return lectureProgressRepository.findAll().stream()
                .map(lectureProgress -> new LectureProgressResponse(lectureProgress.getId(),lectureProgress.getMemberId(),
                        lectureProgress.getLectureId(),lectureProgress.getProgress()))
                .toList();
    }

    @Transactional
    public List<LectureEnrollmentsResponse> findAllEnrolledStudent() {
        return lectureEnrollmentRepository.findAllEnrollment();
    }

    @Transactional
    public List<LectureReviewsResponse> findAllReview() {
        return reviewRepository.findAllReview();
    }
}
