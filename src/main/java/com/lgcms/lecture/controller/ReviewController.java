package com.lgcms.lecture.controller;

import com.lgcms.lecture.common.dto.BaseResponse;
import com.lgcms.lecture.dto.request.review.ReviewCreateRequest;
import com.lgcms.lecture.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{id}")
    public ResponseEntity<BaseResponse> registerReview(@PathVariable("id") String lectureId,
                                                       @RequestBody ReviewCreateRequest reviewCreateRequest,
                                                       @RequestHeader("X-USER-ID") Long memberId){
        reviewService.createReview(lectureId,memberId,reviewCreateRequest);
        return ResponseEntity.ok(BaseResponse.ok(null));
    }
}
