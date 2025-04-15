package com.example.demo.controller;

import com.example.demo.input_dto.QuestionTempListDto;
import com.example.demo.input_dto.QuestionTempSaveDto;
import com.example.demo.input_dto.UpdateTempQuestionDTO;
import com.example.demo.resp_dto.ApiResponse;
import com.example.demo.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/mathpix2")
@CrossOrigin("*")
public class MathPixNewController {
    @Autowired
    private MathpixNewService mathpixNewService;

    @Autowired
    private GetTempService getTempService;

    @Autowired
    private QuestionUpdateService questionUpdateService;

    @Autowired
    private DummyResponce dummyResponce;

    @Autowired
    private ProcessTempListBatch processTempListBatch;

    @PostMapping("/createTempListForConversion")
    public ResponseEntity<ApiResponse> createTempListForConversion(@RequestBody  QuestionTempListDto questionTempListDto){

        log.info("createTempListForConversion api called");
        ApiResponse apiResponse = mathpixNewService.createTempListForConversion(questionTempListDto);

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);

    }

    @PostMapping("/processTempList")
    public ResponseEntity<ApiResponse> processTempList(){

        log.info("processTempList api called");
        ApiResponse apiResponse = mathpixNewService.processTempList();

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);

    }

    @GetMapping("/checkGenAiConversionStatus")
    public ResponseEntity<ApiResponse> checkGenAiConversionStatus(){

        log.info("checkGenAiConversionStatus api called");
        ApiResponse apiResponse = mathpixNewService.checkGenAiConversionStatus();

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);

    }






    @PostMapping("/getDataFromTempList")
    public ResponseEntity<Object> getDataFromTempList(){

        log.info("getDataFromTempList api called");
        ApiResponse apiResponse = getTempService.getDataFromTempList();

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);

    }

    @GetMapping("/getQuestion")
    public ResponseEntity<Object> getQuestion(@RequestParam Integer questionId){

        log.info("getQuestion  api called");
        ApiResponse apiResponse = questionUpdateService.getQuestion(questionId);

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);

    }


    @PostMapping("/updateTempQuestion")
    public ResponseEntity<Object> updateTempQuestion(@RequestBody UpdateTempQuestionDTO updateTempQuestionDTO){

        log.info("updateTempQuestion  api called");
        ApiResponse apiResponse = questionUpdateService.updateTempQuestion(updateTempQuestionDTO);

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);

    }

    @PostMapping("/saveAllQuestions")
    public ResponseEntity<Object> saveAllQuestions(){

        log.info("saveAllQuestions  api called");
        ApiResponse apiResponse = questionUpdateService.saveAllQuestions();

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);

    }


//@PostMapping("/processTempListBatch")
//public ResponseEntity<ApiResponse> processTempListBatch(){
//
//    log.info("processTempList api called");
//    ApiResponse apiResponse = processTempListBatch.processTempList();
//
//    return ResponseEntity
//            .status(apiResponse.getHttpStatusCode())
//            .body(apiResponse);
//
//}
@PostMapping("/updateMatchedStatus")
public ResponseEntity<Object> updateMatchedStatus(
        @RequestParam Long questionId,
        @RequestParam Boolean matchedStatus) {

    log.info("updateMatchedStatus API called for questionId: {}", questionId);
    ApiResponse apiResponse = questionUpdateService.updateMatchedStatusForQuestion(questionId, matchedStatus);

    return ResponseEntity
            .status(apiResponse.getHttpStatusCode())
            .body(apiResponse);
}

}
