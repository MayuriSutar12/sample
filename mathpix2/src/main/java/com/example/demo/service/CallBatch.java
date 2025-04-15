package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Component
public class CallBatch {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    @Qualifier("processQuestionJob2")
    private Job processQuestionJob2;

    @Autowired
    @Qualifier("processQuestionJob1")
    private Job processQuestionJob1;
    @Async
    public void callBatch() {

        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(processQuestionJob2, jobParameters);

            log.info("====================Batch Completed ===========================");
            Map<String, Object> data = new HashMap<>();
            data.put("jobId", jobExecution.getJobId());
            data.put("status", jobExecution.getStatus().toString());
            data.put("startTime", jobExecution.getStartTime());

            log.info("Batch Status: {}", jobExecution.getStatus());

            log.info("===============================================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    public void callBatchnew() {

        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(processQuestionJob1, jobParameters);

            log.info("====================Batch Completed ===========================");
            Map<String, Object> data = new HashMap<>();
            data.put("jobId", jobExecution.getJobId());
            data.put("status", jobExecution.getStatus().toString());
            data.put("startTime", jobExecution.getStartTime());

            log.info("Batch Status: {}", jobExecution.getStatus());

            log.info("===============================================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
