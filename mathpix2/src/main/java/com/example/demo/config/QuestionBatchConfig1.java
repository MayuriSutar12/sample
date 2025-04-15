package com.example.demo.config;

import com.example.demo.model.QuestionDataTemp;
import com.example.demo.model.SolutionDataTemp;
import com.example.demo.repository.QuestionDataTempRepository;
import com.example.demo.repository.SolutionDataTempRepository;
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
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableBatchProcessing
public class QuestionBatchConfig1 {

    @Value("${batch.limit}")
    private int batchLimit;

    @Value("${batch.chunk-size}")
    private int chunkSize;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private QuestionDataTempRepository questionDataTempRepository;

    @Autowired
    private SolutionDataTempRepository solutionDataTempRepository;

    @Autowired
    private BatchJobListener batchJobListener;

    // ====== Job ======
    @Bean(name = "processQuestionJob1")
    public Job processQuestionJob1(JobRepository jobRepository,  @Qualifier("processQuestionStep1")  Step processQuestionStep) {
        return new JobBuilder("processQuestionJob1", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(batchJobListener)
                .start(processQuestionStep)
                .build();
    }

    // ====== Step ======
//    @Bean(name = "processQuestionStep1")
//    public Step processQuestionStep1(
//            JobRepository jobRepository,
//            PlatformTransactionManager transactionManager,
//            ItemReader<QuestionSolutionWrapper> reader,
//            ItemProcessor<QuestionSolutionWrapper, QuestionDataTemp> processor,
//            ItemWriter<QuestionDataTemp> writer,
//            @Value("${batch.chunk-size}") int chunkSize) {
//
//        return new StepBuilder("processQuestionStep1", jobRepository)
//                .<QuestionSolutionWrapper, QuestionDataTemp>chunk(chunkSize, transactionManager)
//                .reader(reader)
//                .processor(processor)
//                .writer(writer)
//                .build();
//    }
    @Bean(name = "processQuestionStep1")
    public Step processQuestionStep1(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("reader1") ItemReader<QuestionSolutionWrapper> reader,
            @Qualifier("processor1") ItemProcessor<QuestionSolutionWrapper, QuestionDataTemp> processor,
            @Qualifier("writer1") ItemWriter<QuestionDataTemp> writer) {

        return new StepBuilder("processQuestionStep1", jobRepository)
                .<QuestionSolutionWrapper, QuestionDataTemp>chunk(chunkSize, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    // ====== Reader ======
//    @Bean(name = "reader1")
//    @StepScope
//    public ListItemReader<QuestionSolutionWrapper> reader1(
//            @Value("${batch.limit}") int batchLimit,
//            @Value("${batch.chunk-size}") int chunkSize,
//            @Value("#{jobParameters['batchExecutionId']}") Long batchExecutionId,
//            @Value("#{jobParameters['lastProcessedDate']}") String lastProcessedDate) {
//
//        log.info("Batch execution ID: {}", batchExecutionId);
//        log.info("Last processed date: {}", lastProcessedDate);
//
//        LocalDate today = LocalDate.now();
//        LocalDateTime startOfDay = today.atStartOfDay();
//        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
//
//        int processedCount = questionDataTempRepository.getProcessedCountByDate(startOfDay, endOfDay);
//        log.info("Processed count since {}: {}", startOfDay, processedCount);
//
//        int remainingQuestions = batchLimit - processedCount;
//        if (remainingQuestions <= 0) {
//            log.info("All questions are processed. No more questions left.");
//            return new ListItemReader<>(Collections.emptyList());
//        }
//
//        List<QuestionDataTemp> questions = questionDataTempRepository.getLimitedQuestions(
//                Math.min(remainingQuestions, chunkSize)
//        );
//
//        List<QuestionSolutionWrapper> wrappedList = questions.stream()
//                .map(q -> {
//                     List<SolutionDataTemp> solutionDataTemps = solutionDataTempRepository.fetchSolutionForQuestionIdStatus(q.getQuestionId());
//                    return new QuestionSolutionWrapper(q, solutionDataTemps);
//                })
//                .collect(Collectors.toList());
//
//        log.info("Fetched {} questions with solutions to process.", wrappedList.size());
//        return new ListItemReader<>(wrappedList);
//    }
    @Bean(name = "reader1")
    @StepScope
    public ListItemReader<QuestionSolutionWrapper> reader1(
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
        int remainingQuestions = batchLimit - processedCount;

        if (remainingQuestions <= 0) {
            log.info("All questions are processed. No more questions left.");
            return new ListItemReader<>(Collections.emptyList());
        }

        List<QuestionDataTemp> questions = questionDataTempRepository.getLimitedQuestions(
                Math.min(remainingQuestions, batchLimit)
        );

        List<QuestionSolutionWrapper> wrappedList = questions.stream()
                .map(q -> {
                    SolutionDataTemp solution = solutionDataTempRepository
                            .fetchSolutionForQuestionId(q.getQuestionId());
                    return new QuestionSolutionWrapper(q, solution);
                })
                .collect(Collectors.toList());

        log.info("Fetched {} questions with single solutions to process.", wrappedList.size());
        return new ListItemReader<>(wrappedList);
    }



    // ====== Writer ======
    @Bean(name = "writer1")
    public ItemWriter<QuestionDataTemp> writer1() {
        JpaItemWriter<QuestionDataTemp> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
