package com.lgcms.lecture.dto.internal;

public record LectureMetaResponse(
            String id, //lectureId
            Long memberId,
            String title,
            String level,
            Long price,
            Long reviewCount,
            Long totalAmount
    ){}