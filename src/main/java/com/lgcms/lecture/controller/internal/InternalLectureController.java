package com.lgcms.lecture.controller.internal;

import com.lgcms.lecture.dto.internal.*;
import com.lgcms.lecture.service.LectureService;
import com.lgcms.lecture.service.internal.InternalLectureService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/lecture")
@RequiredArgsConstructor
public class InternalLectureController {

    private final LectureService lectureService;
    private final InternalLectureService internalLectureService;

    @GetMapping("/lecturer/verify")
    public boolean verifyLecture(@RequestParam Long memberId, @RequestParam String lectureId){

        return lectureService.isLecturer(memberId, lectureId);
    }

    @GetMapping("/student/verify")
    public boolean verifyStudent(@RequestParam Long memberId, @RequestParam String lectureId){

        return lectureService.isStudent(memberId, lectureId);
    }

    @GetMapping("/data")
    public ResponseEntity<List<LectureMetaResponse>> getDataFromLecture(){
        return ResponseEntity.ok(internalLectureService.findAllLectures());
    }

    @GetMapping("/questions")
    public ResponseEntity<List<LectureQuestionsResponse>> getQuestionsFromLecture(){
        return ResponseEntity.ok(internalLectureService.findAllQuestions());
    }

    @GetMapping("/progress")
    public ResponseEntity<List<LectureProgressResponse>> getProgressFromLecture(){
        return ResponseEntity.ok(internalLectureService.findAllProgress());
    }

    @GetMapping("/enrollments")
    public ResponseEntity<List<LectureEnrollmentsResponse>> getEnrollmentsFromLecture(){
        return ResponseEntity.ok(internalLectureService.findAllEnrolledStudent());
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<LectureReviewsResponse>> getReviewsFromLecture(){
        return ResponseEntity.ok(internalLectureService.findAllReview());
    }


}
