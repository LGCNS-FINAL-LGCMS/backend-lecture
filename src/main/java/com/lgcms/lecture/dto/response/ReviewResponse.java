package com.lgcms.lecture.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {
    private String content;
    private String nickname;
    private Integer start;
}
