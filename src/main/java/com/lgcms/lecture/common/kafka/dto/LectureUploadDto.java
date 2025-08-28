package com.lgcms.lecture.common.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureUploadDto {

    private String lectureId;
    private String thumbnailKey;
    private String bookKey;
}
