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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/qna")
@RequiredArgsConstructor
@Slf4j
public class QnaController {

    private final QnaService qnaService;

    @PostMapping("")
    public ResponseEntity<BaseResponse> registerQuestion(@RequestBody QuestionCreateRequest questionCreateRequest,
                                                         @RequestHeader("X-USER-ID") String id){
        Long memberId = Long.parseLong("1");
        Long qnaId = qnaService.registerQuestion(memberId,questionCreateRequest);

        return ResponseEntity.ok(BaseResponse.ok(qnaId));
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
        return ResponseEntity.ok(BaseResponse.ok(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getQnaList(@PathVariable("id") String lectureId){
        List<QnaListResponse> qnaListResponse = qnaService.getQnaList(lectureId);
        return ResponseEntity.ok(BaseResponse.ok(qnaListResponse));
    }

    @GetMapping("/member")
    public ResponseEntity<BaseResponse> getMemberQnaList(@RequestHeader("X-USER-ID") String id){
        Long memberId = Long.parseLong("1");
        List<QnaListResponse> qnaListResponse = qnaService.getMemberQnaList(memberId);
        return ResponseEntity.ok(BaseResponse.ok(qnaListResponse));
    }

    @PostMapping("/answer/{id}")
    public ResponseEntity<BaseResponse> registerAnswer(@PathVariable("id") Long questionId,
                                                       @RequestBody AnswerRequest answerCreateRequest,
                                                       @RequestHeader("X-USER-ID") String id){
        Long memberId = Long.parseLong("1");
        qnaService.registerAnswer(questionId,memberId,answerCreateRequest);
        return ResponseEntity.ok(BaseResponse.ok(null));
    }

    @PutMapping("/answer/{id}")
    public ResponseEntity<BaseResponse> updateAnswer(@PathVariable("id") Long answerId,
                                                     @RequestBody AnswerRequest answerRequest,
                                                     @RequestHeader("X-USER-ID") String id){
        Long memberId = Long.parseLong("1");
        qnaService.updateAnswer(memberId, answerId,answerRequest);
        return ResponseEntity.ok(BaseResponse.ok(null));
    }
}
