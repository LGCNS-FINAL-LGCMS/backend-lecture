package com.lgcms.lecture.dto.response.lecture;

import lombok.*;

@Data
@Builder
public class LectureResponseDto {

    private String lectureId;

    private String title;

    private Long price;

    private String thumbnail;

    private String reviewAvg;

    private String reviewCount;
}
