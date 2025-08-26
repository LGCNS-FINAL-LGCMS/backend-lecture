package com.lgcms.lecture.common.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncodingSuccess {

    private String status;
    private String message;
    private String lectureId;
    private String lessonId;
    private Integer duration;
    private String url;

}
