package com.lgcms.lecture.common.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressUpdate {
    private String lectureId;
    private String lessonId;
    private Long memberId;
    private Integer playtime;
}
