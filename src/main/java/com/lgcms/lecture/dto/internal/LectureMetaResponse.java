package com.lgcms.lecture.dto.internal;

public record LectureMetaResponse(
            String id,
            Long memberId,
            String title,
            String level,
            Long price,
            Long avgRating,
            Long reviewCount,
            Long totalAmount
    ){}