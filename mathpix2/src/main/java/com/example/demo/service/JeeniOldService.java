package com.example.demo.service;

import com.example.demo.ExternalDto.*;
import com.example.demo.component.SessionBean;
import com.example.demo.exception.NotFoundException;
import com.example.demo.external.RestTemplateService;

import com.example.demo.model.OptionsData;
import com.example.demo.model.QuestionInfo;
import com.example.demo.projectionInterface.QuestionDtoInterface;
import com.example.demo.repository.OptionsDataRepository;
import com.example.demo.repository.QuestionInfoRepository;
import com.example.demo.repository.QuestionPaperRepository;
import com.example.demo.resp_dto.*;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Service
@Slf4j
public class JeeniOldService {
    private final SessionBean sessionBean;
    final String bucketName = "genius-parent";
    private final Region region = Region.AP_SOUTH_1;
    
    @Autowired
    private RestTemplateService restTemplateService;
    @Autowired
    private OptionsDataRepository optionsDataRepository;
    @Autowired
    private QuestionPaperRepository questionPaperRepository;
    @Autowired
    private QuestionInfoRepository questionInfoRepository;

    public JeeniOldService(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public static String convertA4ToGen(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return "";
        }

        return imageUrl.replace("%2Fa4", "%2Fgen"); // Replaces encoded "/a4" with "/gen"
    }

    public ApiResponse getSessionHeader(String sessionId) {
        if (sessionId == null || sessionId.isEmpty()) {
            return new ApiResponse(HttpStatus.BAD_REQUEST, "Session ID is missing", null);
        }

        // Call the RestTemplate service to get a session header
        sessionId = restTemplateService.getSessionHeader(sessionId);

        if (sessionId == null || sessionId.isEmpty()) {
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve session header", null);
        }

        return new ApiResponse(HttpStatus.OK, "Session header retrieved successfully", sessionId);
    }

    public ApiResponse LogIn(String userName, String password, HttpSession session) {
        log.info("LogIn method called");

        String sessionId = restTemplateService.login(userName, password, session.getId());

        return new ApiResponse(HttpStatus.OK, "success", sessionId);
    }

    public ApiResponse getAllCourses(String sessionHeader) {
        log.info("Fetching courses with sessionHeader: {}", sessionHeader);

        List<CourseVo> courseVos;
        try {
            // Fetch courses from REST API
            courseVos = restTemplateService.getAllCourse(sessionHeader);
        } catch (Exception e) {
            log.error("Error while calling getAllCourse API: {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch courses", null);
        }

        // Validate response
        if (courseVos == null || courseVos.isEmpty()) {
            log.warn("No courses found.");
            return new ApiResponse(HttpStatus.NO_CONTENT, "No courses found", null);
        }

        log.info("Courses received: {}", courseVos.size());

        // Convert CourseVo list to CourseDto list
        List<CourseDto> courses = courseVos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new ApiResponse(HttpStatus.OK, "Courses retrieved successfully", courses);
    }

    // Helper method to convert CourseVo to CourseDto
    private CourseDto convertToDto(CourseVo courseVo) {
        return CourseDto.builder()
                .courseId(courseVo.getId())
                .courseName(courseVo.getName()) // Add other fields if needed
                .build();
    }

    public ApiResponse getSubjectFromCourse(Integer courseId, String sessionHeader) {
        log.info("Fetching subjects for courseId: {} with sessionHeader: {}", courseId, sessionHeader);

        List<SubjectVo> subjectVos;
        try {
            // Call the REST service to fetch subjects
            subjectVos = restTemplateService.getSubjectFromCourse(courseId, sessionHeader);
        } catch (Exception e) {
            log.error("Error while calling getSubjectFromCourse API: {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch subjects", null);
        }

        // Validate response
        if (subjectVos == null || subjectVos.isEmpty()) {
            log.warn("No subjects found for courseId: {}", courseId);
            return new ApiResponse(HttpStatus.NO_CONTENT, "No subjects found", null);
        }

        log.info("Subjects retrieved successfully: {}", subjectVos);

        // Convert SubjectVo list to SubjectDto list
        List<SubjectDto> subjectDtos = subjectVos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new ApiResponse(HttpStatus.OK, "Subjects retrieved successfully", subjectDtos);
    }

    // Convert SubjectVo to SubjectDto
    private SubjectDto convertToDto(SubjectVo subjectVo) {
        return SubjectDto.builder()
                .subjectId(subjectVo.getId())
                .subjectName(subjectVo.getName()) // Add other fields if needed
                .build();
    }

    public ApiResponse getChapterFromSubject(Integer courseId, Integer subjectId, String sessionHeader) {
        log.info("Fetching chapters for courseId: {} and subjectId: {} with sessionHeader: {}", courseId, subjectId, sessionHeader);

        List<ChapterDto> chapterDtos = new ArrayList<>();

        try {
            // Call the REST service
            List<ChapterVo> chapterVos = restTemplateService.getChapterForSubject(courseId, subjectId, sessionHeader);

            if (chapterVos == null || chapterVos.isEmpty()) {
                log.warn("No chapters found for courseId: {} and subjectId: {}", courseId, subjectId);
                return new ApiResponse(HttpStatus.NO_CONTENT, "No chapters found", chapterDtos);
            }

            // Convert ChapterVo list to ChapterDto list
            for (ChapterVo chapterVo : chapterVos) {
                chapterDtos.add(convertToDto(chapterVo));
            }

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("API Error: Status Code: {}, Response: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch chapters", ex.getResponseBodyAsString());

        } catch (Exception e) {
            log.error("Exception while calling getChapterForSubject API: {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", null);
        }

        return new ApiResponse(HttpStatus.OK, "Chapters retrieved successfully", chapterDtos);
    }

    private ChapterDto convertToDto(ChapterVo chapterVo) {
        return ChapterDto.builder()
                .chapterId(chapterVo.getId())
                .chapterName(chapterVo.getName()) // Add other fields if needed
                .build();
    }

    public ApiResponse getQuestionsForQuePaper(Integer questionPaperId, String sessionHeader, int page, int size) {
        log.info("Fetching questions for questionPaperId: {} with sessionHeader: {}", questionPaperId, sessionHeader);
QuestionRespWithCount questionRespWithCount=new QuestionRespWithCount();
        List<QuestionVo> questionVos;
        try {
            // Fetch all questions
            questionVos = restTemplateService.getQuestionsForQuePaper(questionPaperId, sessionHeader);
        } catch (Exception e) {
            log.error("Error while calling getQuestionsForQuePaper API: {}", e.getMessage());
            return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch questions", null);
        }

        if (questionVos == null || questionVos.isEmpty()) {
            log.warn("No questions found for questionPaperId: {}", questionPaperId);
            //  return new ApiResponse(HttpStatus.NO_CONTENT, "No questions found", Collections.emptyList());
            throw new NotFoundException("Question paper not found");
        }
         // **Set the total count of questions before pagination**
        int totalQuestionCount = questionVos.size();

        // Implement Pagination using SubList
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, questionVos.size());

        if (fromIndex >= questionVos.size()) {
            return new ApiResponse(HttpStatus.NO_CONTENT, "No questions found for the given page", Collections.emptyList());
        }

        List<QuestionVo> paginatedQuestionVos = questionVos.subList(fromIndex, toIndex);

        // Convert paginated QuestionVo list to QuestionListDto list
        List<QuestionListDto> questionDtos = paginatedQuestionVos.stream()
                .map(questionVo -> {
                    Integer questionId = questionVo.getId();
                    QuestionInfo questionInfo = questionInfoRepository.findByQuestionId(questionId);
                    Integer questionSubType = (questionInfo != null) ? questionInfo.getQuestion_sub_type() : null;

                    // Fetch options for the specific questionId
                    List<OptionsData> optionsDataList = Optional.ofNullable(optionsDataRepository.fetchOptionsByQuestionId(questionId.longValue()))
                            .orElse(Collections.emptyList());
                    log.info("Options data for questionId {}: {}", questionId, optionsDataList.size());

                    int questionType = questionVo.getQuestionTypeId();
                    log.info("Processing questionType: {}", questionType);

                    // Determine correct answer based on question type
                    String correctAnswer;
                    switch (questionType) {
                        case 3 -> correctAnswer = processNumericOptions(optionsDataList);
                        case 6 -> correctAnswer = processNumericAnswer(optionsDataList);
                        case 7 -> correctAnswer = processType7(optionsDataList, questionVo.getType());
                        default -> correctAnswer = processMultipleChoiceOptions(optionsDataList, questionVo.getType());
                    }

                    return convertToDto(questionVo, correctAnswer, questionSubType);
                })
                .collect(Collectors.toList());

        questionRespWithCount.setQuestionCount(totalQuestionCount);
        questionRespWithCount.setQuestionList(questionDtos);
        return new ApiResponse(HttpStatus.OK, "Questions retrieved successfully", questionRespWithCount);
    }

    // Convert QuestionVo to QuestionListDto
    private QuestionListDto convertToDto(QuestionVo questionVo, String correctAnswer, Integer questionSubType) {
        return QuestionListDto.builder()
                .questionId(questionVo.getId())
                .questionUrl(questionVo.getGenericImageUrl())
                .questionType(questionVo.getQuestionTypeId()+ "-" +(questionSubType != null ? questionSubType : "") )
                .solutionUrl(questionVo.getSolutionUrl() != null ? questionVo.getSolutionUrl() : "") // Handle null values
                .answerKey(correctAnswer) // Set correct answer
                .build();
    }

    // Process numeric options (0-9) and return the correct answer
    private String processNumericOptions(List<OptionsData> optionsDataList) {
        String[] optionLabels = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        for (int i = 0; i < Math.min(optionsDataList.size(), 10); i++) {
            if (Boolean.TRUE.equals(optionsDataList.get(i).getIs_answer())) {
                return optionLabels[i];
            }
        }
        return "N/A"; // Default if no correct answer is found
    }

    // Process numeric answer (for type 6) and return the exact stored value
    private String processNumericAnswer(List<OptionsData> optionsDataList) {
        for (OptionsData optionData : optionsDataList) {
            if (optionData.getAnswer() != null && !optionData.getAnswer().trim().isEmpty()) {
                String answer = optionData.getAnswer().trim();
                log.info("Numeric answer retrieved: {}", answer);
                return optionData.getAnswer().trim();
            }
        }
        return "N/A";
    }

    // Process multiple-choice options (A, B, C, D)
    private String processMultipleChoiceOptions(List<OptionsData> optionsDataList, int questionType) {
        String[] optionLabels = {"A", "B", "C", "D"};
        Set<String> correctAnswersSet = new HashSet<>();

        for (int i = 0; i < Math.min(optionsDataList.size(), optionLabels.length); i++) {
            if (Boolean.TRUE.equals(optionsDataList.get(i).getIs_answer())) {
                if (questionType == 7) {
                    String answerText = (optionsDataList.get(i).getAnswer() != null) ? optionsDataList.get(i).getAnswer().trim() : "N/A";
                    log.info("answers" + answerText);

                    correctAnswersSet.add(optionLabels[i] + "-" + answerText);
                } else {
                    correctAnswersSet.add(optionLabels[i]);
                }
            }
        }
        return String.join(",", correctAnswersSet);
    }

    private String processType7(List<OptionsData> optionsDataList, int questionType) {
        String[] optionLabels = {"A", "B", "C", "D"};
        Set<String> correctAnswersSet = new HashSet<>();

        for (int i = 0; i < Math.min(optionsDataList.size(), optionLabels.length); i++) {
            if (Boolean.TRUE.equals(optionsDataList.get(i).getIs_answer())) {

                String answerText = (optionsDataList.get(i).getAnswer() != null) ? optionsDataList.get(i).getAnswer().trim() : "N/A";
                log.info("answers" + answerText);

                correctAnswersSet.add(optionLabels[i] + "-" + answerText);
            }
        }
        return String.join(",", correctAnswersSet);
    }

    public ApiResponse getQuestionsForQuePaper(Integer questionPaperId) {
        List<QuestionDtoInterface> questionDtoInterface = questionPaperRepository.findQuestionsByQuestionPaperDetailId(questionPaperId);
        if (questionDtoInterface.isEmpty()) {
            throw new NotFoundException("No questions found for chapter ID: " + questionPaperId);

        }
        return new ApiResponse(HttpStatus.OK, "questions retrieved successfully", questionDtoInterface);
    }

    public ApiResponse getQuestionsForChapter(
            Integer courseId, Integer subjectId, Integer chapterId, Integer topicId,
            String complexity, String questionType, String questionSubType,
            Integer pageNo, Integer noOfQuestionsPerPage, String sessionHeader) {
        {

            log.info("Fetching questions for Chapter: {}, Course: {}, Subject: {}", chapterId, courseId, subjectId);

            // Call REST template service to get questions
            QuestionCount questionCount = restTemplateService.getQuestionsForChapter(
                    courseId, subjectId, chapterId, topicId, complexity,
                    questionType, questionSubType, pageNo, noOfQuestionsPerPage, sessionHeader
            );

            return new ApiResponse(HttpStatus.OK, "Questions retrieved successfully", questionCount);
        }
    }

    public ApiResponse getQuestionsForQuePaperByName(Integer pageNo, Integer noOfQuestionsPerPage, String searchText, String sessionHeader) {
        //     QuestionCount questionCount = restTemplateService.getQuestionsForQuePaperByName(pageNo,noOfQuestionsPerPage, sessionHeader);
        return new ApiResponse(HttpStatus.OK, "Questions retrieved successfully", null);

    }

//    public ApiResponse getQuestionsForChapterNew(
//            Integer courseId, Integer subjectId, Integer chapterId,
//            String questionType, Integer pageNo, Integer noOfQuestionsPerPage,
//            String sessionHeader) {
//
//        log.info("Fetching questions for Chapter: {}, Course: {}, Subject: {}", chapterId, courseId, subjectId);
//
//        Integer topicId = 0;
//        String complexity = "1,2,3,4,5,6,7";
//        String questionSubType = "1,2";
//
//        // Call REST template service to get questions
//        QuestionCount questionCount = restTemplateService.getQuestionsForChapter(
//                courseId, subjectId, chapterId, topicId, complexity,
//                questionType, questionSubType, pageNo, noOfQuestionsPerPage, sessionHeader
//        );
//
//        // List to store DTO objects
//        List<QuestionListDto> questionDtos = new ArrayList<>();
//        QuestionRespWithCount questionRespWithCount = new QuestionRespWithCount();
//        if (questionCount != null && questionCount.getQuestionVos() != null) {
//            for (QuestionVo question : questionCount.getQuestionVos()) {
//                // Initialize the data map
//                Map<String, Object> questionData = new HashMap<>();
//                questionData.put("questionId", question.getId());
//                questionData.put("questionType", question.getQuestionTypeId());
//
//
//                // Fix the URL conversion issue
//                String a4ImageUrl = question.getA4ImageUrl();
//                String genImageUrl = (a4ImageUrl != null) ? convertA4ToGen(a4ImageUrl) : null;
//                questionData.put("genericUrl", genImageUrl);
//
//                log.info("Original A4 Image URL: {}", a4ImageUrl);
//                log.info("Modified Gen Image URL: {}", genImageUrl);
//
//                // Fetch question information
//                QuestionInfo questionInfo = questionInfoRepository.findByQuestionId(question.getId());
//                Integer questionSubTypes = (questionInfo != null) ? questionInfo.getQuestion_sub_type() : null;
//
//                // Fetch options for the specific questionId
//                List<OptionsData> optionsDataList = Optional.ofNullable(
//                        optionsDataRepository.fetchOptionsByQuestionId((long) question.getId())
//                ).orElse(Collections.emptyList());
//
//                log.info("Options data for questionId {}: {}", question.getId(), optionsDataList.size());
//
//                // Get the question type
//                int questionTypeValue = question.getQuestionTypeId();
//                log.info("Processing questionType: {}", questionTypeValue);
//
//                // Determine correct answer based on question type
//                String correctAnswer;
//                switch (questionTypeValue) {
//                    case 3 -> correctAnswer = processNumericOptions(optionsDataList);
//                    case 6 -> correctAnswer = processNumericAnswer(optionsDataList);
//                    case 7 -> correctAnswer = processType7(optionsDataList, question.getType());
//                    default -> correctAnswer = processMultipleChoiceOptions(optionsDataList, question.getType());
//                }
//
//                // Convert the question to DTO and add it to the list
//                questionDtos.add(convertToDto1(question, correctAnswer, questionSubTypes, genImageUrl));
//
//                questionRespWithCount.setQuestionCount(questionCount.getQuestionCount());
//                questionRespWithCount.setQuestionList(questionDtos);
//            }
//        } else {
//            log.warn("No questions found or API response is empty.");
//            return new ApiResponse(HttpStatus.NO_CONTENT, "No questions found", null);
//        }
//
//        return new ApiResponse(HttpStatus.OK, "Questions retrieved successfully", questionRespWithCount);
//    }


        // Cache to store the total count by course-subject-chapter combination
        private final Map<String, Integer> questionCountCache = new ConcurrentHashMap<>();

        public ApiResponse getQuestionsForChapterNew(
                Integer courseId, Integer subjectId, Integer chapterId,
                String questionType, Integer pageNo, Integer noOfQuestionsPerPage,
                String sessionHeader) {

            log.info("Fetching questions for Chapter: {}, Course: {}, Subject: {}, Page: {}", chapterId, courseId, subjectId, pageNo);

            Integer topicId = 0;
            String complexity = "1,2,3,4,5,6,7";
            String questionSubType = "1,2";

            // Create a unique cache key
            String cacheKey = courseId + "-" + subjectId + "-" + chapterId;

            // Fetch questions from the API
            QuestionCount questionCount = restTemplateService.getQuestionsForChapter(
                    courseId, subjectId, chapterId, topicId, complexity,
                    questionType, questionSubType, pageNo, noOfQuestionsPerPage, sessionHeader
            );

            // Handle empty responses
            if (questionCount == null || questionCount.getQuestionVos() == null) {
                log.warn("No questions found or API response is empty.");

                // If cache exists, use the cached count even if the page is empty
                int cachedCount = questionCountCache.getOrDefault(cacheKey, 0);

                if (cachedCount > 0) {
                    return new ApiResponse(HttpStatus.OK, "No questions on this page, but count is consistent",
                            new QuestionRespWithCount(cachedCount, Collections.emptyList()));
                } else {
                    return new ApiResponse(HttpStatus.NO_CONTENT, "No questions found", null);
                }
            }

            // Cache the count only on the first page
            if (pageNo == 1) {
                questionCountCache.put(cacheKey, questionCount.getQuestionCount());
                log.info("Caching question count: {} for key {}", questionCount.getQuestionCount(), cacheKey);
            }

            // Retrieve cached count or use the current count as fallback
            int totalCount = questionCountCache.getOrDefault(cacheKey, questionCount.getQuestionCount());

            List<QuestionListDto> questionDtos = new ArrayList<>();

            for (QuestionVo question : questionCount.getQuestionVos()) {
                Map<String, Object> questionData = new HashMap<>();
                questionData.put("questionId", question.getId());
                questionData.put("questionType", question.getQuestionTypeId());

                // Handle image URL conversion
                String a4ImageUrl = question.getA4ImageUrl();
                String genImageUrl = (a4ImageUrl != null) ? convertA4ToGen(a4ImageUrl) : null;
                questionData.put("genericUrl", genImageUrl);

                log.info("Original A4 Image URL: {}", a4ImageUrl);
                log.info("Modified Gen Image URL: {}", genImageUrl);

                // Fetch question information
                QuestionInfo questionInfo = questionInfoRepository.findByQuestionId(question.getId());
                Integer questionSubTypes = (questionInfo != null) ? questionInfo.getQuestion_sub_type() : null;

                // Fetch options
                List<OptionsData> optionsDataList = Optional.ofNullable(
                        optionsDataRepository.fetchOptionsByQuestionId((long) question.getId())
                ).orElse(Collections.emptyList());

                log.info("Options data for questionId {}: {}", question.getId(), optionsDataList.size());

                // Process correct answer
                int questionTypeValue = question.getQuestionTypeId();
                String correctAnswer;
                switch (questionTypeValue) {
                    case 3 -> correctAnswer = processNumericOptions(optionsDataList);
                    case 6 -> correctAnswer = processNumericAnswer(optionsDataList);
                    case 7 -> correctAnswer = processType7(optionsDataList, question.getType());
                    default -> correctAnswer = processMultipleChoiceOptions(optionsDataList, question.getType());
                }

                // Add question DTO
                questionDtos.add(convertToDto1(question, correctAnswer, questionSubTypes, genImageUrl));
            }

            // Return the response with the cached total count
            QuestionRespWithCount responseWithCount = new QuestionRespWithCount();
            responseWithCount.setQuestionCount(totalCount);  // Use cached total count
            responseWithCount.setQuestionList(questionDtos);

            return new ApiResponse(HttpStatus.OK, "Questions retrieved successfully", responseWithCount);
        }


    // Updated DTO conversion method to include Gen image URL
    private QuestionListDto convertToDto1(QuestionVo questionVo, String correctAnswer, Integer questionSubType, String genImageUrl) {
        return QuestionListDto.builder()
                .questionId(questionVo.getId())
                .questionType(questionVo.getQuestionTypeId()+ "-" +(questionSubType != null ? questionSubType : "")  )
                .solutionUrl(questionVo.getSolutionUrl() != null ? questionVo.getSolutionUrl() : "")
                .questionUrl(genImageUrl) // Set converted Gen image URL
                .answerKey(correctAnswer) // Set correct answer
                .build();
    }


    public ApiResponse uploadImageToS3(MultipartFile file) {


            try {

                // Example validation before upload
                long maxSizeInBytes = 10 * 1024 * 1024;  // 10 MB
                if (file.getSize() > maxSizeInBytes) {
                    return new ApiResponse(HttpStatus.BAD_REQUEST, "File size must be less than 10 MB.", null);
                }

                // Convert MultipartFile to Path
                Path tempFile = Files.createTempFile(file.getOriginalFilename(), null);
                file.transferTo(tempFile);

                // Initialize AWS S3 Client
                S3Client s3 = S3Client.builder()
                        .region(region)
                        .credentialsProvider(StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
                        .build();

                // Upload to S3
                String objectKey = UUID.randomUUID() + "-" + file.getOriginalFilename();
                PutObjectRequest request = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .acl("public-read")
                        .contentDisposition("inline")
                        .contentType(file.getContentType())
                        .build();

                s3.putObject(request, tempFile);

                // Construct file URL
                String fileUrl = "https://" + bucketName + ".s3." + region.id() + ".amazonaws.com/" + objectKey;
                return new ApiResponse(HttpStatus.OK, "File uploaded successfully!", fileUrl);
            } catch (Exception e) {
                e.printStackTrace();
                return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file", null);
            }
        }
    }

