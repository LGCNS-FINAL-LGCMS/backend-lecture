package com.lgcms.lecture.dto.request.lecture;


import lombok.Data;

@Data
public class LectureRequestDto {

    private String title;

    private String thumbnail;

    private String category;

    private String information;

    private String level;

    private Long price;

}
