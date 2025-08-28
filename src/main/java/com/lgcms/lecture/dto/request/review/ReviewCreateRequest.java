package com.lgcms.lecture.dto.request.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequest {

    private String comment;

    private Integer star;

    private String nickname;

    private String suggestion;

    private List<ReviewContentRequest> reviewContentRequests;
}
