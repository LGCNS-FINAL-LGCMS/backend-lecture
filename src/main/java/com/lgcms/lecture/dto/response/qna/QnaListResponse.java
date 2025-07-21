package com.lgcms.lecture.dto.response.qna;

import java.util.List;

public record QnaListResponse(String title,
                              String content,
                              Long id,
                              List<AnswerResponse> answers
){}
