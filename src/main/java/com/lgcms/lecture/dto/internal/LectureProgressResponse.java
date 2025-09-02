package com.lgcms.lecture.dto.internal;

public record LectureProgressResponse(
        Long id,
        Long memberId,
        String lectureId,
        Long progressRate
    ) {}