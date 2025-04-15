package com.example.demo.controller;

import com.example.demo.resp_dto.ApiResponse;
import com.example.demo.service.JeeniOldService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/mathpix2")
@CrossOrigin("*")
public class JeeniOldController {

@Autowired
private JeeniOldService jeeniOldService;

    @GetMapping("/getSessionHeader")
    public ResponseEntity<Object> getSessionHeader(@RequestParam(value = "sessionId") String sessionId){

        log.info("getSessionHeader api called");
        ApiResponse apiResponse = jeeniOldService.getSessionHeader(sessionId);

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);

    }
    @GetMapping("/getAllCourses")
    public ResponseEntity<ApiResponse> getAllCourses( @RequestHeader("Sessionheader") String sessionHeader){

        log.info("getAllCourses api called");
        ApiResponse apiResponse = jeeniOldService.getAllCourses(sessionHeader);

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);

    }
    @GetMapping("/getSubjectFromCourse")
    public ResponseEntity<ApiResponse> getSubjectFromCourse( @RequestParam(value = "courseId") Integer courseId,@RequestHeader("Sessionheader") String sessionHeader){

        log.info("getSubjectFromCourse api called");
        ApiResponse apiResponse = jeeniOldService.getSubjectFromCourse(courseId,sessionHeader);

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);

    }
    @PostMapping("/loginNew")
    public ResponseEntity<Object> loginNew(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password, HttpSession session){

        log.info("loginNew api called");
        ApiResponse apiResponse = jeeniOldService.LogIn(userName, password, session);

        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatusCode());

    }
    @GetMapping("/getChapterFromSubject")
    public ResponseEntity<ApiResponse> getChapterFromSubject( @RequestParam(value = "courseId") Integer courseId,@RequestParam(value = "subjectId") Integer subjectId,@RequestHeader("Sessionheader") String sessionHeader){

        log.info("getChapterFromSubject api called");
        ApiResponse apiResponse = jeeniOldService.getChapterFromSubject(courseId,subjectId, sessionHeader);

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);

    }

    @GetMapping("/getQuestionsForChapter")
    public ResponseEntity<ApiResponse> getQuestionsForChapter(
            @RequestParam Integer courseId,
            @RequestParam Integer subjectId,
            @RequestParam Integer chapterId,
            @RequestParam Integer topicId,
            @RequestParam String complexity,
            @RequestParam String questionType,
            @RequestParam String questionSubType,
            @RequestParam Integer pageNo,
            @RequestParam Integer noOfQuestionsPerPage,
            @RequestHeader("Sessionheader") String sessionHeader) {

        log.info("getQuestionsForChapter API called with topicId: {}, questionType: {}, questionSubType: {}",
                topicId, questionType, questionSubType);

        ApiResponse apiResponse = jeeniOldService.getQuestionsForChapter(
                courseId, subjectId, chapterId, topicId, complexity, questionType,
                questionSubType, pageNo, noOfQuestionsPerPage, sessionHeader);
        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);
    }


    @GetMapping("/getQuestionsForQuePaper")
    public ResponseEntity<ApiResponse> getQuestionsForQuePaper(
            @RequestParam(value = "questionPaperId") Integer questionPaperId,
            @RequestHeader("Sessionheader") String sessionHeader,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        log.info("getQuestionsForQuePaper API called for questionPaperId: {}, page: {}, size: {}",
                questionPaperId, page, size);

        ApiResponse apiResponse = jeeniOldService.getQuestionsForQuePaper(questionPaperId, sessionHeader, page, size);

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);
    }
    @GetMapping("/getQuestionsForQuePaperByName")
    public ResponseEntity<ApiResponse> getQuestionsForQuePaperByName(
            @RequestParam(value = "pageNo") Integer pageNo,
            @RequestParam(value = "noOfQuestionsPerPage") Integer noOfQuestionsPerPage,
            @RequestParam(value = "searchText", required = false) String searchText,
               @RequestHeader("Sessionheader") String sessionHeader

    ) {
        log.info("getQuestionsForQuePaper API called for questionPaperId: {}, page: {}, size: {}",
                pageNo, noOfQuestionsPerPage, searchText);

        ApiResponse apiResponse = jeeniOldService.getQuestionsForQuePaperByName(pageNo, noOfQuestionsPerPage, searchText, sessionHeader);

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);
    }
    @GetMapping("/getQuestionsForChapterNew")
    public ResponseEntity<ApiResponse> getQuestionsForChapterNew(
            @RequestParam Integer courseId,
            @RequestParam Integer subjectId,
            @RequestParam Integer chapterId,
            @RequestParam String questionType,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "size", defaultValue = "10") Integer noOfQuestionsPerPage,
            @RequestHeader("Sessionheader") String sessionHeader) {

        log.info("getQuestionsForChapter API called with topicId: {}, questionType: {}, questionSubType: {}",
                 questionType);

        ApiResponse apiResponse = jeeniOldService.getQuestionsForChapterNew(
                courseId, subjectId, chapterId,questionType,
                pageNo, noOfQuestionsPerPage, sessionHeader);
        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);
    }
    @PostMapping(value = "/uploadImageToS3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> uploadImageToS3(@RequestParam("file") MultipartFile file){

        log.info("uploadImageToS3 api called");
        ApiResponse apiResponse = jeeniOldService.uploadImageToS3(file);

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);
    }
}
