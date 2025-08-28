package com.lgcms.lecture.common.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaCreated {

    private Long memberId;
    private Long qnaId;
    private String lectureName;
}
