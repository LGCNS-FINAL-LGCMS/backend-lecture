package com.lgcms.lecture.dto.response.lecture;

import lombok.*;

@Data
@Builder
public class LectureResponseDto {

    private String lectureId;

    private String nickname;

    private String description;

    private String title;

    private Long price;

    private String thumbnail;

    private Double averageStar;

    private Long reviewCount;

    private String information;
}
