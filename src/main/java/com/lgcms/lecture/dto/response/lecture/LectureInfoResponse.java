package com.lgcms.lecture.dto.response.lecture;

import com.lgcms.lecture.dto.response.lesson.LessonResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureInfoResponse {
    private LectureResponseDto lectureResponseDto;
    private List<LessonResponse> lessonResponses;
    private Boolean isStudent;
    private Integer progress;
}
