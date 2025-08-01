package com.lgcms.lecture.controller;

import com.lgcms.lecture.common.dto.BaseResponse;
import com.lgcms.lecture.dto.request.review.ReviewCreateRequest;
import com.lgcms.lecture.dto.response.ReviewResponse;
import com.lgcms.lecture.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/list/{id}")
    public ResponseEntity<BaseResponse> getAllReview(@PathVariable("id") String lectureId,
                                                     @RequestHeader("X-USER-ID") Long memberId){
        List<ReviewResponse> reviewResponseList = reviewService.getAllReviews(lectureId);

        return ResponseEntity.ok(BaseResponse.ok(reviewResponseList));
    }

    @GetMapping("")
    public ResponseEntity<BaseResponse> getReview(@PathVariable("id") String lectureId,
                                                  @RequestHeader("X-USER-ID") Long memberId){
        List<ReviewResponse> reviewResponseList = reviewService.getReview(memberId);

        return ResponseEntity.ok(BaseResponse.ok(reviewResponseList));
    }
}
