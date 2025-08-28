package com.lgcms.lecture.controller.internal;

import com.lgcms.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/internal/lecture")
@RequiredArgsConstructor
public class InternalLectureController {
    private final LectureService lectureService;

    @GetMapping("/lecturer/verify")
    public boolean verifyLecture(@RequestParam Long memberId, @RequestParam String lectureId){

        return lectureService.isLecturer(memberId, lectureId);
    }

    @GetMapping("/student/verify")
    public boolean verifyStudent(@RequestParam Long memberId, @RequestParam String lectureId){

        return lectureService.isStudent(memberId, lectureId);
    }
}
