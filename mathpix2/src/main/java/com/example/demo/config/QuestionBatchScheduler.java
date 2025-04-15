package com.example.demo.config;

import com.example.demo.exception.AllQuestionsProcessedException;
import com.example.demo.exception.DailyBatchLimitReachedException;
import com.example.demo.exception.ResourceExhaustedException;
import com.example.demo.repository.QuestionDataTempRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

//@Slf4j
//@Service
//public class QuestionBatchScheduler {
//
//    @Autowired
//    private JobLauncher jobLauncher;
//
//    @Autowired
//    private Job processQuestionJob;
//
//    @Autowired
//    private JobExplorer jobExplorer;
//
//    @Autowired
//    private QuestionDataTempRepository questionDataTempRepository;
//
//    // Scheduled to run daily at 11:00 AM
//    @Scheduled(cron = "0 0 11 * * ?")  // Run daily at 11:00 AM
//    public void runBatchJob() {
//        log.info("Starting batch job at 11:00 AM");
//
//        String lastProcessedDate = getLastProcessedDate();
//        log.info("Using last processed date: {}", lastProcessedDate);
//
//        try {
//            startBatch(lastProcessedDate);
//        } catch (ResourceExhaustedException e) {
//            log.warn("Batch stopped: {}", e.getMessage());
//        } catch (Exception e) {
//            log.error("Failed to run batch job: {}", e.getMessage(), e);
//        }
//    }
//
//    private void startBatch(String lastProcessedDate) {
//        try {
//            JobParameters jobParameters = new JobParametersBuilder()
//                    .addString("lastProcessedDate", lastProcessedDate)
//                    .addLong("startAt", System.currentTimeMillis())
//                    .toJobParameters();
//
//            JobExecution execution = jobLauncher.run(processQuestionJob, jobParameters);
//
//            // Check for custom exceptions thrown by the batch job
//            if (execution.getExitStatus().getExitCode().equals("FAILED")) {
//                log.warn("Batch job failed.");
//            } else {
//                log.info("Batch job completed successfully.");
//            }
//
//        } catch (ResourceExhaustedException e) {
//            log.warn("No more questions to process: {}", e.getMessage());
//            throw e;  // Rethrow the exception to propagate it to `runBatchJob`
//        } catch (Exception e) {
//            log.error(" Failed to run batch job: {}", e.getMessage(), e);
//            try {
//                throw e;  // Rethrow general exceptions
//            } catch (JobExecutionAlreadyRunningException ex) {
//                throw new RuntimeException(ex);
//            } catch (JobRestartException ex) {
//                throw new RuntimeException(ex);
//            } catch (JobInstanceAlreadyCompleteException ex) {
//                throw new RuntimeException(ex);
//            } catch (JobParametersInvalidException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//    }

@Slf4j
@Service
public class QuestionBatchScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("processQuestionJob2")
    private Job processQuestionJob2;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private QuestionDataTempRepository questionDataTempRepository;

    @Scheduled(cron = "0 0 11 * * ?")  // Run daily at 11:00 AM
//@Scheduled(cron = "0 */5 * * * ?")  // Every 5 minutes
    public void runBatchJob() {
        log.info("Starting batch job at 11:00 AM");
//
//        String lastProcessedDate = getLastProcessedDate();
//        log.info("Using last processed date: {}", lastProcessedDate);

        try {
//            startBatch(lastProcessedDate);
            startBatch();

//        } catch (AllQuestionsProcessedException e) {
//            log.warn("All questions processed: {}", e.getMessage());
//        } catch (DailyBatchLimitReachedException e) {
//            log.info("Daily batch limit reached: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Failed to run batch job: {}", e.getMessage(), e);
        }
    }
//    private void startBatch(String lastProcessedDate) {

    private void startBatch() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
//                    .addString("lastProcessedDate", lastProcessedDate)
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(processQuestionJob2, jobParameters);

            if (execution.getExitStatus().getExitCode().equals("FAILED")) {
                log.warn("Batch job failed.");
            } else {
                log.info("Batch job completed successfully.");
            }

//        } catch (AllQuestionsProcessedException e) {
//            log.warn("No more questions to process: {}", e.getMessage());
//            throw e;
//        } catch (DailyBatchLimitReachedException e) {
//            log.info("Daily batch limit reached: {}", e.getMessage());
//            throw e;
        } catch (Exception e) {
            log.error("Failed to run batch job: {}", e.getMessage(), e);
            try {
                throw e;
            } catch (JobExecutionAlreadyRunningException ex) {
                throw new RuntimeException(ex);
            } catch (JobRestartException ex) {
                throw new RuntimeException(ex);
            } catch (JobInstanceAlreadyCompleteException ex) {
                throw new RuntimeException(ex);
            } catch (JobParametersInvalidException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    // Get the last processed date from previous successful executions
//    private String getLastProcessedDate() {
//        List<JobInstance> jobInstances = jobExplorer.findJobInstancesByJobName("processQuestionJob", 0, 1);
//
//        if (!jobInstances.isEmpty()) {
//            JobInstance instance = jobInstances.get(0);
//            List<JobExecution> executions = jobExplorer.getJobExecutions(instance);
//
//            for (JobExecution execution : executions) {
//                if (execution.getStatus() == BatchStatus.COMPLETED) {
//                    JobParameters parameters = execution.getJobParameters();
//                    String lastDateStr = parameters.getString("lastProcessedDate");
//
//                    if (lastDateStr != null) {
//                        LocalDateTime lastDate = LocalDateTime.parse(lastDateStr);
//
//                        //  Check if all 1,500 questions were processed
//                        int processedCount = questionDataTempRepository.getProcessedCountByDate(lastDate);
//                        log.info("Processed on {}: {}/1500", lastDate, processedCount);
//
//                        if (processedCount >= 1500) {
//                            log.info("All 1500 questions processed. Moving to the next day.");
//                            return lastDate.plusDays(1).toString();  // Move to the next day
//                        }
//
//                        return lastDateStr;
//                    }
//                }
//            }
//        }
//
//        // Default to yesterday if no previous execution is found
//        log.warn("No previous successful batch found. Using yesterday's date.");
//        return LocalDateTime.now().minusDays(1).toString();
//    }


    // Check if any incomplete job exists
//    private boolean hasIncompleteJobs() {
//        List<JobInstance> jobInstances = jobExplorer.findJobInstancesByJobName("processQuestionJob", 0, 10);
//
//        for (JobInstance instance : jobInstances) {
//            List<JobExecution> executions = jobExplorer.getJobExecutions(instance);
//            for (JobExecution execution : executions) {
//                if (execution.getStatus() == BatchStatus.STARTED || execution.getStatus() == BatchStatus.FAILED) {
//                    return true;  // Incomplete job found
//                }
//            }
//        }
//        return false;
//    }
}
