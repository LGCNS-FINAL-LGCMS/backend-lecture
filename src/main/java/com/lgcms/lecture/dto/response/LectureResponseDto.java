package com.lgcms.lecture.dto.response;

import lombok.*;

@Getter @Setter
@Builder @AllArgsConstructor
@NoArgsConstructor
public class LectureResponseDto {

    private String lectureId;

    private String title;

    private Long price;

    private String thumbnail;

    private String reviewAvg;

    private String reviewCount;
}
