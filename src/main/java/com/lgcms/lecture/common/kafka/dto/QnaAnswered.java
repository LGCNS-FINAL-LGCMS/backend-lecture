package com.lgcms.lecture.common.kafka.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaAnswered {

    private Long qnaId;
    private String questionTitle;
    private Long memberId;
}
