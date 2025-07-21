package com.lgcms.lecture.controller;

import com.lgcms.lecture.common.dto.BaseResponse;
import com.lgcms.lecture.dto.request.qna.QuestionCreateRequest;
import com.lgcms.lecture.dto.request.qna.QuestionUpdateRequest;
import com.lgcms.lecture.service.LectureService;
import com.lgcms.lecture.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qna")
@RequiredArgsConstructor
@Slf4j
public class QnaController {

    private final QnaService qnaService;

    @PostMapping("")
    public ResponseEntity<BaseResponse> registerQuestion(@RequestBody QuestionCreateRequest questionCreateRequest,
                                                         @RequestHeader("X-USER-ID") String id){
        Long memberId = Long.parseLong("1");
        qnaService.registerQuestion(memberId,questionCreateRequest);

        return ResponseEntity.ok(BaseResponse.ok(null));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse> updateQuestion(@PathVariable("id") Long questionId,
                                                       @RequestBody QuestionUpdateRequest questionUpdateRequest,
                                                       @RequestHeader("X-USER-ID") String id){
        Long memberId = Long.parseLong("1");
        qnaService.updateQuestion(memberId,questionId, questionUpdateRequest);
        return ResponseEntity.ok(BaseResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteQuestion(@PathVariable("id") Long questionId,
                                                       @RequestHeader("X-USER-ID") String id){
        Long memberId = Long.parseLong("1");
        qnaService.deleteQuestion(memberId,questionId);
        ResponseEntity.ok(BaseResponse.ok(null));
    }
}
