package com.lgcms.lecture.dto.internal;

import java.time.LocalDateTime;

public record LectureEnrollmentsResponse(
            Long id,
            Long studentId, //memberId
            String lectureId,
            LocalDateTime enrollmentAt //createdAt
    ) {
    }