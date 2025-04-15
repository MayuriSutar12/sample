package com.example.demo.config;

import com.example.demo.exception.AllQuestionsProcessedException;
import com.example.demo.exception.DailyBatchLimitReachedException;
import com.example.demo.exception.ResourceExhaustedException;
import com.example.demo.model.QuestionDataTemp;
import com.example.demo.repository.QuestionDataTempRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
//
//@Slf4j
//@Configuration
//@EnableBatchProcessing
////@EnableAsync
//public class QuestionBatchConfig {
//    @Value("${batch.limit}")       // Inject batch limit from YML
//    private int batchLimit;
//
//    @Value("${batch.chunk-size}")  // Inject chunk size from YML
//    private int chunkSize;
//    @Autowired
//    private EntityManagerFactory entityManagerFactory;
//
//    @Autowired
//    private QuestionDataTempRepository questionDataTempRepository;
//
//    // Defines the batch job with a step
//    @Bean
//    public Job processQuestionJob(JobRepository jobRepository, Step processQuestionStep) {
//        return new JobBuilder("processQuestionJob", jobRepository)
//                .incrementer(new RunIdIncrementer())  // Ensures unique job instance
//                .start(processQuestionStep)
//                .build();
//    }
//
//    // Defines the step with chunk processing
//    @Bean
//    public Step processQuestionStep(JobRepository jobRepository,
//                                    PlatformTransactionManager transactionManager,
//                                    ItemReader<QuestionDataTemp> reader,
//                                    ItemProcessor<QuestionDataTemp, QuestionDataTemp> processor,
//                                    ItemWriter<QuestionDataTemp> writer) {
//        return new StepBuilder("processQuestionStep", jobRepository)
//                .<QuestionDataTemp, QuestionDataTemp>chunk(chunkSize, transactionManager)
//                .reader(reader)
//                .processor(processor)
//                .writer(writer)
//                .build();
//    }
//
//
////    @Bean
////    @StepScope
////    public JpaPagingItemReader<QuestionDataTemp> reader(
////            @Value("#{jobParameters['batchExecutionId']}") Long batchExecutionId,
////            @Value("#{jobParameters['lastProcessedDate']}") String lastProcessedDate) {
////
////        int totalQuestions = (int) questionDataTempRepository.count();
////
////        log.info("Batch execution ID: " + batchExecutionId);
////        log.info("Last processed date: " + lastProcessedDate);
////
////        LocalDateTime lastDate = lastProcessedDate != null
////                ? LocalDateTime.parse(lastProcessedDate)
////                : LocalDateTime.now().minusDays(1);
////
////        int processedCount = questionDataTempRepository.getProcessedCountByDate(lastDate);
////        log.info("Processed count since {}: {}", lastDate, processedCount);
////
////        int remainingQuestions = 1500 - processedCount;
////
////        //  Handle the exhaustion gracefully by returning an empty reader
////        if (processedCount >= totalQuestions || remainingQuestions <= 0) {
////            String message = " All questions are processed. No more questions left.";
////            log.info(message);
////
////            // Instead of throwing an exception, return an empty reader
////            JpaPagingItemReader<QuestionDataTemp> emptyReader = new JpaPagingItemReader<>();
////            emptyReader.setEntityManagerFactory(entityManagerFactory);
////            emptyReader.setQueryString("SELECT q FROM QuestionDataTemp q WHERE 1 = 0");  // Empty query
////            emptyReader.setPageSize(1);
////            return emptyReader;
////        }
////
////        JpaPagingItemReader<QuestionDataTemp> reader = new JpaPagingItemReader<>();
////        reader.setEntityManagerFactory(entityManagerFactory);
////
////        String query = "SELECT q FROM QuestionDataTemp q WHERE q.genAiConversionCompleted = false limit ? ";
////        reader.setQueryString(query);
////        reader.setPageSize(Math.min(remainingQuestions, 100));
////
////        log.info("Processing remaining {} questions.", remainingQuestions);
////
////        return reader;
////    }
//@Bean
//@StepScope
//public ListItemReader<QuestionDataTemp> reader(
//        @Value("#{jobParameters['batchExecutionId']}") Long batchExecutionId,
//        @Value("#{jobParameters['lastProcessedDate']}") String lastProcessedDate) {
//
//    int totalQuestions = (int) questionDataTempRepository.count();
//
//    log.info("Batch execution ID: " + batchExecutionId);
//    log.info("Last processed date: " + lastProcessedDate);
//
//    LocalDateTime lastDate = lastProcessedDate != null
//            ? LocalDateTime.parse(lastProcessedDate)
//            : LocalDateTime.now().minusDays(1);
//
//    int processedCount = questionDataTempRepository.getProcessedCountByDate(lastDate);
//    log.info("Processed count since {}: {}", lastDate, processedCount);
//
//    int remainingQuestions = batchLimit - processedCount;
//
//    // Handle exhaustion gracefully by returning an empty reader
//    if (processedCount >= totalQuestions || remainingQuestions <= 0) {
//        log.info("All questions are processed. No more questions left.");
//        return new ListItemReader<>(Collections.emptyList());
//    }
////    if (processedCount >= totalQuestions) {
////        throw new AllQuestionsProcessedException("All questions in the table are processed. No more questions left.");
////    }
////
////    if (remainingQuestions <= 0) {
////        throw new DailyBatchLimitReachedException("Daily batch limit reached. No more questions for today.");
////    }
//
//    // Fetch remaining questions using the repository method with native SQL and limit
//    List<QuestionDataTemp> questions = questionDataTempRepository.getLimitedQuestions(Math.min(remainingQuestions, chunkSize));
//
//    log.info("Processing {} questions.", questions.size());
//
//    // Use ListItemReader to process the fetched questions
//    return new ListItemReader<>(questions);
//}
//
//
////    @Bean
////    @StepScope
////    public ListItemReader<QuestionDataTemp> reader(
////            @Value("#{jobParameters['batchExecutionId']}") Long batchExecutionId,
////            @Value("#{jobParameters['lastProcessedDate']}") String lastProcessedDate) {
////
////        int totalQuestions = (int) questionDataTempRepository.count();
////
////        log.info("Batch execution ID: " + batchExecutionId);
////        log.info("Last processed date: " + lastProcessedDate);
////
////        LocalDateTime lastDate = lastProcessedDate != null
////                ? LocalDateTime.parse(lastProcessedDate)
////                : LocalDateTime.now().minusDays(1);
////
////        int processedCount = questionDataTempRepository.getProcessedCountByDate(lastDate);
////        log.info("Processed count since {}: {}", lastDate, processedCount);
////
////        int batchSize = 15;  // Daily batch limit
////        int remainingQuestions = batchSize - processedCount;
////
////        // Exception for all questions processed in the table
////        if (processedCount >= totalQuestions) {
////            String message = "All questions in the table are processed. No more questions left.";
////            log.warn(message);
////            throw new AllQuestionsProcessedException(message);
////        }
////
////        //  Exception when today's batch limit is reached
////        if (remainingQuestions <= 0) {
////            String message = " All 15 questions processed for today's batch.";
////            log.info(message);
////            throw new DailyBatchLimitReachedException(message);
////        }
////
////        // Fetch remaining questions using native SQL with limit
////        List<QuestionDataTemp> questions = questionDataTempRepository.getLimitedQuestions(Math.min(remainingQuestions, 5));
////
////        log.info("Processing {} questions.", questions.size());
////
////        return new ListItemReader<>(questions);
////    }
//
//    // JPA writer to save processed data
//    @Bean
//    public JpaItemWriter<QuestionDataTemp> writer() {
//        JpaItemWriter<QuestionDataTemp> writer = new JpaItemWriter<>();
//        writer.setEntityManagerFactory(entityManagerFactory);
//        return writer;
//    }
//    @Bean
//    public ThreadPoolTaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(5);        // Minimum number of threads
//        executor.setMaxPoolSize(10);        // Maximum number of threads
//        executor.setQueueCapacity(25);      // Queue size
//        executor.setThreadNamePrefix("batch-thread-");
//        executor.initialize();
//
//        log.info("Async batch executor initialized.");
//        return executor;
//    }
@Slf4j
@Configuration
@EnableBatchProcessing

public class QuestionBatchConfig {

    @Value("${batch.limit}")
    private int batchLimit;

    @Value("${batch.chunk-size}")
    private int chunkSize;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private QuestionDataTempRepository questionDataTempRepository;

    @Autowired
    private BatchJobListener batchJobListener;  // Inject Listener

    // ThreadPoolTaskExecutor for async execution
//    @Bean
//    public ThreadPoolTaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(10);         // Increased thread pool size
//        executor.setMaxPoolSize(20);
//        executor.setQueueCapacity(50);
//        executor.setThreadNamePrefix("batch-thread-");
//        executor.initialize();
//        log.info("Async batch executor initialized.");
//        return executor;
//    }

    // Batch Job with Listener
    @Bean(name = "processQuestionJob2")
    public Job processQuestionJob(JobRepository jobRepository,   @Qualifier("processQuestionStep2") Step processQuestionStep) {
        return new JobBuilder("processQuestionJob2", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(batchJobListener)     // Register Listener
                .start(processQuestionStep)
                .build();
    }

    // Define the batch step
//    @Bean
//    public Step processQuestionStep(
//            JobRepository jobRepository,
//            PlatformTransactionManager transactionManager,
//            ItemReader<QuestionDataTemp> reader,
//            ItemProcessor<QuestionDataTemp, QuestionDataTemp> processor,
//            ItemWriter<QuestionDataTemp> writer) {
//
//        return new StepBuilder("processQuestionStep", jobRepository)
//                .<QuestionDataTemp, QuestionDataTemp>chunk(chunkSize, transactionManager)
//                .reader(reader)
//                .processor(processor)
//                .writer(writer)
//                //.taskExecutor(taskExecutor())   // Async Execution
//                .build();
//    }
//    @Bean
//    public Step processQuestionStep(
//            JobRepository jobRepository,
//            PlatformTransactionManager transactionManager,
//            ItemReader<QuestionDataTemp> reader,
//            ItemProcessor<QuestionDataTemp, QuestionDataTemp> processor,
//            ItemWriter<QuestionDataTemp> writer,
//            @Value("${batch.chunk-size}") int chunkSize) {
//
//        return new StepBuilder("processQuestionStep", jobRepository)
//                .<QuestionDataTemp, QuestionDataTemp>chunk(chunkSize, transactionManager)
//                .reader(reader)
//                .processor(processor)
//                .writer(writer)
//                .build();
//    }
    @Bean(name = "processQuestionStep2")
    public Step processQuestionStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("reader2") ItemReader<QuestionDataTemp> reader,
            @Qualifier("processor2") ItemProcessor<QuestionDataTemp, QuestionDataTemp> processor,
            @Qualifier("writer2") ItemWriter<QuestionDataTemp> writer,
            @Value("${batch.chunk-size}") int chunkSize) {

        return new StepBuilder("processQuestionStep2", jobRepository)
                .<QuestionDataTemp, QuestionDataTemp>chunk(chunkSize, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }


    //    // Reader: Fetches questions for processing
//    @Bean
//    @StepScope
//    public ListItemReader<QuestionDataTemp> reader(
//            @Value("#{jobParameters['batchExecutionId']}") Long batchExecutionId,
//            @Value("#{jobParameters['lastProcessedDate']}") String lastProcessedDate) {
//
//        int totalQuestions = (int) questionDataTempRepository.count();
//
//        log.info("Batch execution ID: {}", batchExecutionId);
//        log.info("Last processed date: {}", lastProcessedDate);
//
//
//
//        LocalDate today = LocalDate.now();
//        LocalDateTime startOfDay = today.atStartOfDay();               // 2025-03-19T00:00:00
//        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
//
//        int processedCount = questionDataTempRepository.getProcessedCountByDate(startOfDay,endOfDay);
//        log.info("Processed count since {}: {}", startOfDay, processedCount);
//
//        int remainingQuestions = batchLimit - processedCount;
//
//        if ( remainingQuestions <= 0) {
//            log.info("All questions are processed. No more questions left.");
//            return new ListItemReader<>(Collections.emptyList());
//        }
//
//        List<QuestionDataTemp> questions = questionDataTempRepository.getLimitedQuestions(
//                Math.min(remainingQuestions, chunkSize)
//        );
//
//        log.info("Processing {} questions.", questions.size());
//        return new ListItemReader<>(questions);
//    }
@Bean(name = "reader2")
@StepScope
public ListItemReader<QuestionDataTemp> reader(
        @Value("${batch.limit}") int batchLimit,
        @Value("${batch.chunk-size}") int chunkSize,
        @Value("#{jobParameters['batchExecutionId']}") Long batchExecutionId,
        @Value("#{jobParameters['lastProcessedDate']}") String lastProcessedDate) {

    log.info("Batch execution ID: {}", batchExecutionId);
    log.info("Last processed date: {}", lastProcessedDate);

    LocalDate today = LocalDate.now();
    LocalDateTime startOfDay = today.atStartOfDay();
    LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

    int processedCount = questionDataTempRepository.getProcessedCountByDate(startOfDay, endOfDay);
    log.info("Processed count since {}: {}", startOfDay, processedCount);

    int remainingQuestions = batchLimit - processedCount;
    if (remainingQuestions <= 0) {
        log.info("All questions are processed. No more questions left.");
        return new ListItemReader<>(Collections.emptyList());
    }

    List<QuestionDataTemp> questions = questionDataTempRepository.getLimitedQuestions(
            Math.min(remainingQuestions, batchLimit)
    );

    log.info("Fetched {} questions to process (1 per chunk).", questions.size());
    return new ListItemReader<>(questions);
}

    // Writer: Writes processed questions
    @Bean(name = "writer2")
    public JpaItemWriter<QuestionDataTemp> writer() {
        JpaItemWriter<QuestionDataTemp> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}




