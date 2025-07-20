package com.lgcms.lecture.dto.request.lecture;

import lombok.*;

@Data
public class LectureModifyDto {
    
    private String title;

    private String information;

    private Long price;
}
