package com.lgcms.lecture.controller;

import com.lgcms.lecture.common.dto.BaseResponse;
import com.lgcms.lecture.dto.request.qna.AnswerRequest;
import com.lgcms.lecture.dto.request.qna.AnswerUpdateRequest;
import com.lgcms.lecture.dto.request.qna.QuestionCreateRequest;
import com.lgcms.lecture.dto.request.qna.QuestionUpdateRequest;
import com.lgcms.lecture.dto.response.qna.QnaListResponse;
import com.lgcms.lecture.service.QnaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class QnaController {

    private final QnaService qnaService;

    @PostMapping("/student/lecture/qna")
    public ResponseEntity<BaseResponse<Long>> registerQuestion(@RequestBody QuestionCreateRequest questionCreateRequest,
                                                               @RequestHeader("X-USER-ID") Long memberId) {
        Long qnaId = qnaService.registerQuestion(memberId, questionCreateRequest);

        return ResponseEntity.ok(BaseResponse.ok(qnaId));
    }

    @PatchMapping("/student/lecture/qna/{id}")
    public ResponseEntity<BaseResponse> updateQuestion(@PathVariable("id") Long questionId,
                                                       @RequestBody QuestionUpdateRequest questionUpdateRequest,
                                                       @RequestHeader("X-USER-ID") Long memberId) {
        qnaService.updateQuestion(memberId, questionId, questionUpdateRequest);
        return ResponseEntity.ok(BaseResponse.ok(null));
    }

    @DeleteMapping("/student/lecture/qna/{id}")
    public ResponseEntity<BaseResponse> deleteQuestion(@PathVariable("id") Long questionId,
                                                       @RequestHeader("X-USER-ID") Long memberId) {
        qnaService.deleteQuestion(memberId, questionId);
        return ResponseEntity.ok(BaseResponse.ok(null));
    }

    @GetMapping("/lecture/qna/{id}")
    public ResponseEntity<BaseResponse<Page<QnaListResponse>>> getQnaList(@PathVariable("id") String lectureId,
                                                                          @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<QnaListResponse> qnaListResponse = qnaService.getQnaList(lectureId, pageable);
        return ResponseEntity.ok(BaseResponse.ok(qnaListResponse));
    }

    @GetMapping("/student/lecture/qna/member")
    public ResponseEntity<BaseResponse<List<QnaListResponse>>> getMemberQnaList(@RequestHeader("X-USER-ID") Long memberId) {
        List<QnaListResponse> qnaListResponse = qnaService.getMemberQnaList(memberId);
        return ResponseEntity.ok(BaseResponse.ok(qnaListResponse));
    }

    @PostMapping("/lecturer/lecture/qna/answer/{id}")
    public ResponseEntity<BaseResponse> registerAnswer(@PathVariable("id") Long questionId,
                                                       @RequestBody AnswerRequest answerCreateRequest,
                                                       @RequestHeader("X-USER-ID") Long memberId) {
        qnaService.registerAnswer(questionId, memberId, answerCreateRequest);
        return ResponseEntity.ok(BaseResponse.ok(null));
    }

    @PutMapping("/lecturer/lecture/qna/answer/{id}")
    public ResponseEntity<BaseResponse> updateAnswer(@PathVariable("id") Long answerId,
                                                     @RequestBody AnswerRequest answerRequest,
                                                     @RequestHeader("X-USER-ID") Long memberId) {
        qnaService.updateAnswer(memberId, answerId, answerRequest);
        return ResponseEntity.ok(BaseResponse.ok(null));
    }
}
