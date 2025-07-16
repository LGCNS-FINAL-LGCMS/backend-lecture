package com.lgcms.lecture.dto.request.lecture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureModifyDto {

    private String lectureId;
    
    private String title;

    private String information;

    private Long price;
}
