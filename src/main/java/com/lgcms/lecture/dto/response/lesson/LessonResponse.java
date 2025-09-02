package com.lgcms.lecture.dto.response.lesson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonResponse {

    private String id;

    private String title;

    private String lectureId;

    private Integer playtime;

    private String information;

    private String thumbnail;

    private String videoUrl;

    private Integer progress;

    private LocalDateTime createdAt;

}
