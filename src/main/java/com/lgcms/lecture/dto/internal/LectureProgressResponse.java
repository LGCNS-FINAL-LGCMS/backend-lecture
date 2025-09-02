package com.lgcms.lecture.dto.internal;

public record LectureProgressResponse(
        Long id, //index
        Long memberId,
        String lectureId,
        Integer progressRate  //Integer로 바뀜
    ) {}