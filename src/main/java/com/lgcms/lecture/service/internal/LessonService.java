package com.lgcms.lecture.service.internal;


import com.lgcms.lecture.dto.response.lesson.LessonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "RemoteLessonService")
public interface LessonService {

    @GetMapping("/internal/lesson/title/{id}")
    public List<LessonResponse> getLessons(@PathVariable("id") String lectureId);
}
