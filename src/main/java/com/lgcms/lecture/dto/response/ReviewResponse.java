package com.lgcms.lecture.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponse {
    private String comment;
    private String nickname;
    private Integer star;
    private LocalDateTime createdAt;
}
