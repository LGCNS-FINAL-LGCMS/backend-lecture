package com.lgcms.lecture.controller;


import com.lgcms.lecture.common.dto.BaseResponse;
import com.lgcms.lecture.dto.request.lecture.LectureModifyDto;
import com.lgcms.lecture.dto.request.lecture.LectureRequestDto;
import com.lgcms.lecture.dto.request.lecture.LectureStatusDto;
import com.lgcms.lecture.dto.response.lecture.LectureResponseDto;
import com.lgcms.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/lecture")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    //강의 개설 신청
    @PostMapping("")
    public ResponseEntity<BaseResponse> saveLecture(@RequestBody LectureRequestDto lectureRequestDto ) {
        Long memberId = Long.parseLong("1");
        String lectureId = lectureService.saveLecture(lectureRequestDto, memberId);
        return ResponseEntity.ok(BaseResponse.ok(lectureId));
    }

    //검색 조건에 맞게 검색
    @GetMapping("")
    public ResponseEntity<BaseResponse> getLectureList(@PageableDefault(size=12, sort="createdAt", direction= Sort.Direction.DESC) Pageable pageable,
                                                       @RequestParam(value = "keyword", required = false) String keyword,
                                                       @RequestParam(value = "category", required = false) String category) {
        Long memberId = Long.parseLong("1");
        Page<LectureResponseDto> lectureList = lectureService.getLectureList(pageable,keyword,category);
        return ResponseEntity.ok(BaseResponse.ok(lectureList));
    }

    //카테고리만 조회
    @GetMapping("/category/{category}")
    public ResponseEntity<BaseResponse> getLectureByCategory(@PathVariable("category")String category,
                                                             @RequestParam(value = "keyword",required = false) String keyword){
        List<LectureResponseDto> lectureList = lectureService.getLectureByCategory(category);
        return ResponseEntity.ok(BaseResponse.ok(lectureList));
    }
    //검색어만 조회
    @GetMapping("/search/{keyword}")
    public ResponseEntity<BaseResponse> getLectureByKeyword(@PathVariable("keyword")String keyword){
        List<LectureResponseDto> lectureList = lectureService.getLectureByKeyword(keyword);
        return ResponseEntity.ok(BaseResponse.ok(lectureList));
    }

    //단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getLecture(@PathVariable("id") String lectureId){
        Long memberId = Long.parseLong("1");
        LectureResponseDto lecture = lectureService.getLecture(lectureId, memberId);
        return ResponseEntity.ok(BaseResponse.ok(lecture));
    }
    //강의 상태 수정
    @PutMapping("")
    public ResponseEntity<BaseResponse> modifyLectureStatus(@RequestBody LectureStatusDto lectureStatusDto) {
        return ResponseEntity.ok(BaseResponse.ok(lectureService.modifyLectureStatus(lectureStatusDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> modifyLecture(@RequestBody LectureModifyDto lectureModifyDto,
                                                      @PathVariable("id") String lectureId){
        Long memberId = Long.parseLong("1");
        String id = lectureService.modifyLecture(lectureModifyDto, memberId, lectureId);
        return ResponseEntity.ok(BaseResponse.ok(id));
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<BaseResponse> joinLecture(@PathVariable("id") String lectureId){
        Long memberId = Long.parseLong("1");
        lectureService.joinLecture(memberId,lectureId);
        return ResponseEntity.ok(BaseResponse.ok(null));
    }

    @GetMapping("/verify")
    public boolean verifyLecture(@RequestHeader("X-USER-ID") String memberId, @RequestParam String lectureId){

        return lectureService.isExist(Long.parseLong(memberId), lectureId);
    }
}