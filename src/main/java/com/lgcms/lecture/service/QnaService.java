package com.lgcms.lecture.service;

import com.lgcms.lecture.common.dto.exception.BaseException;
import com.lgcms.lecture.common.dto.exception.LectureError;
import com.lgcms.lecture.common.dto.exception.QnaError;
import com.lgcms.lecture.common.kafka.dto.QnaAnswered;
import com.lgcms.lecture.common.kafka.dto.QnaCreated;
import com.lgcms.lecture.domain.Lecture;
import com.lgcms.lecture.domain.LectureAnswer;
import com.lgcms.lecture.domain.LectureQuestion;
import com.lgcms.lecture.dto.request.qna.AnswerRequest;
import com.lgcms.lecture.dto.request.qna.QuestionCreateRequest;
import com.lgcms.lecture.dto.request.qna.QuestionUpdateRequest;
import com.lgcms.lecture.dto.response.qna.AnswerResponse;
import com.lgcms.lecture.dto.response.qna.QnaListResponse;
import com.lgcms.lecture.dto.response.qna.QnaResponse;
import com.lgcms.lecture.event.producer.QnaEventProducer;
import com.lgcms.lecture.repository.LectureAnswerRepository;
import com.lgcms.lecture.repository.LectureQuestionRepository;
import com.lgcms.lecture.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class QnaService {
    private final LectureQuestionRepository lectureQuestionRepository;
    private final LectureAnswerRepository lectureAnswerRepository;
    private final LectureRepository lectureRepository;
    private final LectureService lectureService;
    private final QnaEventProducer qnaCreatedEvent;

    @Transactional
    public Long registerQuestion(Long memberId, QuestionCreateRequest questionCreateRequest) {
        if (!lectureService.isStudent(memberId, questionCreateRequest.getLectureId())) {
            throw new BaseException(LectureError.LECTURE_NOT_STUDENT);
        }
        Lecture lecture = lectureRepository.findById(questionCreateRequest.getLectureId())
                .orElseThrow(() -> new BaseException(LectureError.LECTURE_NOT_FOUND));

        LectureQuestion lectureQuestion = LectureQuestion.builder()
                .lectureId(questionCreateRequest.getLectureId())
                .memberId(memberId)
                .title(questionCreateRequest.getTitle())
                .content(questionCreateRequest.getContent())
                .lectureAnswers(new ArrayList<>())
                .build();
        QnaCreated qnaCreated = QnaCreated.builder()
                .lectureName(lecture.getTitle())
                .memberId(lecture.getMemberId())
                .qnaId(lectureQuestion.getId())
                .build();
        qnaCreatedEvent.QnaCreated(qnaCreated);
        lectureQuestionRepository.save(lectureQuestion);
        return lectureQuestion.getId();
    }

    @Transactional
    public void updateQuestion(Long memberId, Long qeustionId, QuestionUpdateRequest questionUpdateRequest) {
        verifyQuestion(memberId, qeustionId);

        LectureQuestion question = lectureQuestionRepository.findById(qeustionId)
                .orElseThrow(() -> new BaseException(QnaError.QNA_NOT_FOUND));

        question.updateQuestion(questionUpdateRequest.title(), questionUpdateRequest.content());
    }

    @Transactional
    public void deleteQuestion(Long memberId, Long questionId) {
        verifyQuestion(memberId, questionId);

        lectureQuestionRepository.deleteById(questionId);
    }

    @Transactional
    public void verifyQuestion(Long memberId, Long qnaId) {
        if (!lectureQuestionRepository.existsByIdAndMemberId(qnaId, memberId)) {
            throw new BaseException(QnaError.QNA_FORBIDDEN);
        }
    }

    @Transactional
    public void verifyAnswer(Long memberId, String lectureId) {
        if (!lectureService.isLecturer(memberId, lectureId)) throw new BaseException(QnaError.QNA_NOT_FOUND);
    }

    @Transactional
    public Page<QnaListResponse> getQnaList(String lectureId, Pageable pageable) {
        Page<LectureQuestion> lectureQuestions = lectureQuestionRepository.findAllByLectureId(lectureId, pageable);

        return lectureQuestions
                .map(question -> new QnaListResponse(
                        question.getTitle(),
                        question.getContent(),
                        question.getId(),
                        question.getLectureAnswers().stream()
                                .map(answer -> new AnswerResponse(
                                        answer.getContent(),
                                        answer.getId()
                                )).toList()
                ));
    }

    @Transactional
    public List<QnaListResponse> getMemberQnaList(Long memberId) {
        List<LectureQuestion> lectureQuestions = lectureQuestionRepository.findAllByMemberId(memberId);

        return lectureQuestions.stream()
                .map(question -> new QnaListResponse(
                        question.getTitle(),
                        question.getContent(),
                        question.getId(),
                        question.getLectureAnswers().stream()
                                .map(answer -> new AnswerResponse(
                                        answer.getContent(),
                                        answer.getId()
                                )).toList()
                )).toList();
    }

    @Transactional
    public void registerAnswer(Long questionId, Long memberId, AnswerRequest answerCreateRequest) {
        if (!lectureService.isLecturer(memberId, answerCreateRequest.lectureId()))
            throw new BaseException(QnaError.QNA_FORBIDDEN);
        LectureQuestion lectureQuestion = lectureQuestionRepository.findById(questionId)
                .orElseThrow(() -> new BaseException(QnaError.QNA_NOT_FOUND));
        LectureAnswer lectureAnswer = LectureAnswer.builder()
                .content(answerCreateRequest.content())
                .build();
        QnaAnswered qnaAnswered = QnaAnswered.builder()
                .qnaId(lectureQuestion.getId())
                .questionTitle(lectureQuestion.getTitle())
                .memberId(lectureQuestion.getMemberId())
                .build();
        lectureQuestion.addAnswer(lectureAnswer);
    }

    @Transactional
    public void updateAnswer(Long memberId, Long answerId, AnswerRequest answerUpdateRequest) {
        if (!lectureService.isLecturer(memberId, answerUpdateRequest.lectureId()))
            throw new BaseException(QnaError.QNA_FORBIDDEN);

        LectureAnswer lectureAnswer = lectureAnswerRepository.findById(answerId)
                .orElseThrow(() -> new BaseException(QnaError.QNA_NOT_FOUND));

        lectureAnswer.updateAnswer(answerUpdateRequest.content());
    }

    @Transactional
    public QnaResponse getQna(Long qnaId) {
        LectureQuestion lectureQuestion = lectureQuestionRepository.findById(qnaId)
                .orElseThrow(() -> new BaseException(QnaError.QNA_NOT_FOUND));
        QnaResponse qnaResponse = QnaResponse.builder()
                .title(lectureQuestion.getTitle())
                .question(lectureQuestion.getContent())
                .questionCreatedAt(lectureQuestion.getCreatedAt().toString())
                .answer(
                        lectureQuestion.getLectureAnswers().stream()
                                .map(answer -> new AnswerResponse(answer.getContent(), answer.getId()))
                                .toList()
                )
                .build();

        return qnaResponse;
    }
}
