package com.lgcms.lecture.common.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EncodingStatus {

    private String lectureId;
    private Long memberId;
    private String lectureName;
    private String status;
}
