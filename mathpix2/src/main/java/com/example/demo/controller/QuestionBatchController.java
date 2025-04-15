package com.example.demo.controller;

import com.example.demo.exception.ResourceExhaustedException;
import com.example.demo.model.QuestionDataTemp;
import com.example.demo.repository.QuestionDataTempRepository;
import com.example.demo.resp_dto.ApiResponse;
import com.example.demo.service.CallBatch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/batch")
@Slf4j
@CrossOrigin("*")
public class QuestionBatchController {

    @Autowired
    private JobLauncher jobLauncher;

    @Qualifier("processQuestionJob2") // change as needed
    private  Job processQuestionJob;

    @Autowired
    private QuestionDataTempRepository questionDataTempRepository;
    @Autowired
    private CallBatch callBatch;
    @Value("${batch.limit}")
    private int batchLimit;


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


    @PostMapping("/processTempList")
    public ResponseEntity<ApiResponse> startBatchJob() {

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
            callBatch.callBatch();
            return ResponseEntity.ok(new ApiResponse(
                    HttpStatus.OK,
                    "Batch Process started successfully.",
                    null
            ));

        }
    }


}

