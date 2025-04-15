package com.example.demo.controller;

import com.example.demo.input_dto.ProcessImageUrlDto;
import com.example.demo.model.QuestionDataTemp;
import com.example.demo.repository.QuestionDataTempRepository;
import com.example.demo.resp_dto.ApiResponse;
import com.example.demo.resp_dto.ProcessImageResponse;
import com.example.demo.service.CallBatch;
import com.example.demo.service.GetData;
import com.example.demo.service.ProcessWithMathpix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mathpix2")
@CrossOrigin("*")
public class OldMathPixController {
@Autowired
private ProcessWithMathpix processWithMathpix;
    @Autowired
    private JobLauncher jobLauncher;


    @Autowired
    @Qualifier("processQuestionJob1")
    private Job processQuestionJob1;
    @Autowired
    private QuestionDataTempRepository questionDataTempRepository;
    @Autowired
    private CallBatch callBatch;
    @Value("${batch.limit}")
    private int batchLimit;

    @Autowired
    private GetData getData;

    @PostMapping("/processImageUrl")

    public ResponseEntity<ProcessImageResponse> processImageUrl(@RequestBody ProcessImageUrlDto processImageUrlDto) {
        ProcessImageResponse response = processWithMathpix.processImageUrl(processImageUrlDto);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.ok(response);
    }




//    @Async
//    public void callBatch() {
//
//        try {
//            JobParameters jobParameters = new JobParametersBuilder()
//                    .addLong("startAt", System.currentTimeMillis())
//                    .toJobParameters();
//
//            JobExecution jobExecution = jobLauncher.run(processQuestionJob, jobParameters);
//
//            log.info("====================Batch Completed ===========================");
//            Map<String, Object> data = new HashMap<>();
//            data.put("jobId", jobExecution.getJobId());
//            data.put("status", jobExecution.getStatus().toString());
//            data.put("startTime", jobExecution.getStartTime());
//
//            log.info("Batch Status: {}", jobExecution.getStatus());
//
//            log.info("===============================================");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    @PostMapping("/processQuestionUsingMathpix")
    public ResponseEntity<ApiResponse> startBatchJob1() {

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();               // 2025-03-19T00:00:00
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        int processedCount = questionDataTempRepository.getProcessedCountByDate(startOfDay, endOfDay);
        log.info("Processed count since {}: {}", startOfDay, processedCount);

        int remainingQuestions = batchLimit - processedCount;
        log.info("remaining count" + remainingQuestions);

        List<QuestionDataTemp> questions = questionDataTempRepository.getLimitedQuestions(
                batchLimit
        );
        if (remainingQuestions == 0 || questions.size()==0) {
            return ResponseEntity.ok(new ApiResponse(
                    HttpStatus.OK,
                    " No more questions to process. All questions are completed.",
                    null
            ));
        } else {
            callBatch.callBatchnew();
            return ResponseEntity.ok(new ApiResponse(
                    HttpStatus.OK,
                    "Batch Process started successfully.",
                    null
            ));

        }
    }

    @PostMapping("/getData")
    public ResponseEntity<Object> getData(){

        log.info("getData api called");
        ApiResponse apiResponse = getData.getData();

        return ResponseEntity
                .status(apiResponse.getHttpStatusCode())
                .body(apiResponse);

    }

}