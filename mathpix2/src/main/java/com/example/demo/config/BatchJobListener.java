package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BatchJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Batch job is starting...");
        jobExecution.getExecutionContext().putString("status", "STARTING");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus().isUnsuccessful()) {
            log.error("Batch job failed.");
            jobExecution.getExecutionContext().putString("status", "FAILED");
        } else {
            log.info("Batch job completed successfully.");
            jobExecution.getExecutionContext().putString("status", "COMPLETED");
        }
    }
}

