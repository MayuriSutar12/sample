package com.example.demo.external;

import com.example.demo.ExternalDto.*;
import com.example.demo.component.SessionBean;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Course;
import com.example.demo.resp_dto.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RestTemplateService {

    private final SessionBean sessionBean;
    private final String jeeniService = "https://test2.jeeni.in/Jeeni";
    @Autowired
    private RestTemplate restTemplate;

//    @Value("${jeeniService}")
//    private String jeeniService;
    @Autowired
    private ModelMapper mapper;

    public RestTemplateService(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }


    public String getSessionHeader(String sessionId) {
        String url = jeeniService + "/login/authenticate";

        MultiValueMap<String, Object> hashMap = new LinkedMultiValueMap();
        hashMap.put("userName", Collections.singletonList("savita.jeeni@gmail.com"));
        hashMap.put("password", Collections.singletonList("J44$nAdmin"));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Cookie", String.format("JSESSIONID=%s; G_ENABLED_IDPS=google", sessionId));
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(hashMap, httpHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        if (exchange.getStatusCode().is3xxRedirection()) {
            String jSessionId = exchange.getHeaders().get("Set-Cookie").get(0).split("\\;")[0];
            String cookies = jSessionId.concat("; G_ENABLED_IDPS=google");
            return cookies;
        } else {
            return "";
        }
    }


    public String login(String userName, String password, String sessionId) {
        String url = jeeniService + "/login/authenticate";

        MultiValueMap<String, Object> hashMap = new LinkedMultiValueMap();
        hashMap.put("userName", Collections.singletonList(userName));
        hashMap.put("password", Collections.singletonList(password));

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Cookie", String.format("JSESSIONID=%s; G_ENABLED_IDPS=google", sessionId));
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(hashMap, httpHeaders);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        if (exchange.getStatusCode().is3xxRedirection()) {
            String jSessionId = exchange.getHeaders().get("Set-Cookie").get(0).split("\\;")[0];
            String cookies = jSessionId.concat("; G_ENABLED_IDPS=google");
            return cookies;
        } else {
            return "";
        }
    }


    public List<CourseVo> getAllCourse(String sessionHeader) {
        log.info("getAllCourse called from restTemplate service");

        String urlTemplate = jeeniService + "/rest/course/get";

        // Create HttpHeaders and include session info if necessary
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create HttpEntity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        List<CourseVo> courseVos = new ArrayList<>();

        try {
            log.info("Setting URI");
            URI uri = UriComponentsBuilder.fromHttpUrl(urlTemplate).build().encode().toUri();

            log.info("Calling REST template");
            log.info("Entity: " + entity);

            // Correct way to handle List<CourseVo> response
            ResponseEntity<List<CourseVo>> response = restTemplate.exchange(
                    uri.toString(),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<CourseVo>>() {
                    }
            );

            courseVos = response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Error response body: " + ex.getResponseBodyAsString());
            log.error("HTTP Status Code: " + ex.getStatusCode());
            throw new RuntimeException("Failed to retrieve courses. HTTP Error: " + ex.getStatusCode());
        } catch (Exception e) {
            log.info("Exception: " + e.getMessage());
            throw new NotFoundException("Course details not found");
        }

        return courseVos;
    }


    public List<SubjectVo> getSubjectFromCourse(Integer courseId, String sessionHeader) {
        log.info("Fetching subjects for course ID: {}", courseId);

        String urlTemplate = jeeniService + "/rest/subject/getByCourse/{id}";

        // Create HttpHeaders and include session info
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create HttpEntity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        List<SubjectVo> subjectVos = new ArrayList<>();

        try {
            // Build the final URL
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlTemplate)
                    .uriVariables(Collections.singletonMap("id", courseId));

            log.info("Calling REST API at URL: {}", builder.toUriString());

            // Correct usage of restTemplate.exchange
            ResponseEntity<List<SubjectVo>> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<SubjectVo>>() {
                    }
            );

            subjectVos = response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("Error response body: {}", ex.getResponseBodyAsString());
            log.error("HTTP Status Code: {}", ex.getStatusCode());
            throw new RuntimeException("Failed to retrieve subjects. HTTP Error: " + ex.getStatusCode());
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
            throw new NotFoundException("Subject details not found");
        }

        return subjectVos;
    }


    public List<ChapterVo> getChapterForSubject(Integer courseId, Integer subjectId, String sessionHeader) {
        log.info("Fetching chapters for courseId: {} and subjectId: {}", courseId, subjectId);

        // Base Jeeni service URL
        String urlTemplate = jeeniService + "/rest/chapter/getByCourseIdAndSubjectId/{courseId}/{subjectId}";

        // Create HttpHeaders and include session info if necessary
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create HttpEntity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        List<ChapterVo> chapterVos;
        try {
            // Build the final URL
            String finalUrl = UriComponentsBuilder.fromUriString(urlTemplate)
                    .buildAndExpand(courseId, subjectId)
                    .toUriString();

            log.info("Calling REST API: {}", finalUrl);

            // Use ParameterizedTypeReference to avoid casting issues
            ResponseEntity<List<ChapterVo>> response = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<ChapterVo>>() {
                    }
            );

            // Extract response body
            chapterVos = response.getBody();

            if (chapterVos == null || chapterVos.isEmpty()) {
                log.warn("No chapters found for courseId: {} and subjectId: {}", courseId, subjectId);
                return new ArrayList<>();  // Return empty list instead of null
            }

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("API Error: Status Code: {}, Response: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new RuntimeException("Error fetching chapters: " + ex.getResponseBodyAsString());

        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw new NotFoundException("Chapter details not found");
        }

        return chapterVos;
    }


    public QuestionCount getQuestionsForChapter(
            Integer courseId, Integer subjectId, Integer chapterId, Integer topicId,
            String complexity, String questionType, String questionSubType,
            Integer pageNo, Integer noOfQuestionsPerPage, String sessionHeader) {

        log.info("getQuestionsForChapter called from RestTemplate service");

        String urlTemplate = jeeniService +
                "/rest/mtest/getQuestionsBy/{courseId}/{subjectId}/{chapterId}/{topicId}/{complexity}/{questionType}/{questionSubType}/{pageNo}/{noOfQuestionsPerPage}";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Build the URI
            String finalUrl = UriComponentsBuilder.fromUriString(urlTemplate)
                    .buildAndExpand(courseId, subjectId, chapterId, topicId, complexity, questionType, questionSubType, pageNo, noOfQuestionsPerPage)
                    .toUriString();
//

            log.info("Calling REST template");
            log.info("URL: " + finalUrl);

            ResponseEntity<Map> response = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            Map<String, Object> objectMap = response.getBody();

            ObjectMapper objectMapper = new ObjectMapper();

            final boolean questionCount1 = objectMap.containsKey("questionCount");
            QuestionCount questionCount = new QuestionCount();
            if (questionCount1) {
                log.info("Found ,...");
               // questionCount.setQuestionCount(objectMap.get("questionCount").toString());
                String questionCountStr = (String) objectMap.get("questionCount"); // Get the value as a String
                Integer questionCountValue = Integer.parseInt(questionCountStr);  // Convert to Integer
                questionCount.setQuestionCount(questionCountValue);               // Set the value


            }

            if (objectMap.containsKey("questionVos")) {
                log.info("found...");


                List<QuestionVo> questionVos = objectMapper.readValue(
                        objectMap.get("questionVos").toString(), new TypeReference<List<QuestionVo>>() {
                        } // Convert to List<QuestionVo>
                );
                questionCount.setQuestionVos(questionVos); // Set in object
            }            // Convert the map to `QuestionCount`

            log.info("Question Count : " + questionCount);
            return questionCount;


        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("HTTP Error: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new RuntimeException("Failed to retrieve questions: " + ex.getMessage());
        } catch (Exception e) {
            log.error("Exception occurred: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

        public List<QuestionVo> getQuestionsForQuePaper(Integer questionPaperId, String sessionHeader) {
        log.info("getQuestionsForQuePaper called from restTemplate service");

        // Define the Jeeni service URL
        String urlTemplate = jeeniService + "/rest/questionpaper/getQuestionsByQuestionPaper/{id}";

        // Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, sessionHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create an HttpEntity with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Build the URI
            String finalUrl = UriComponentsBuilder.fromUriString(urlTemplate)
                    .buildAndExpand(questionPaperId)
                    .toUriString();

            log.info("Calling REST template");
            log.info("URL: " + finalUrl);
            log.info("Entity: " + entity);

            // Make the REST API call
            ResponseEntity<List<QuestionVo>> response = restTemplate.exchange(
                    finalUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<QuestionVo>>() {
                    }
            );

            // Return the list of questions
            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.error("HTTP Error: " + ex.getStatusCode() + " - " + ex.getResponseBodyAsString());
            throw new RuntimeException("Failed to retrieve questions. HTTP Error: " + ex.getStatusCode());
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
            throw new NotFoundException("Questions details not found");
        }
    }

//    public QuestionCount getQuestionsForQuePaperByName(Integer pageNo, Integer noOfQuestionsPerPage, String sessionHeader) {
//        log.info("getQuestionsForQuePaperByName called from restTemplate service");
//
//        // Define the Jeeni service URL
//        String urlTemplate = jeeniService + "/rest/questionpaper/getQuestionPaper/{pageNo}/{noOfQuestionsPerPage}";
//
//        // Create headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.COOKIE, sessionHeader);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // Create an HttpEntity with headers
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        try {
//            // Build the URI
//            String finalUrl = UriComponentsBuilder.fromUriString(urlTemplate)
//                    .buildAndExpand(pageNo, noOfQuestionsPerPage)
//                    .toUriString();
//
//            log.info("Calling REST template");
//            log.info("URL: " + finalUrl);
//
//            ResponseEntity<Map> response = restTemplate.exchange(
//                    finalUrl,
//                    HttpMethod.GET,
//                    entity,
//                    Map.class
//            );
//
//            Map<String, Object> objectMap = response.getBody();
//
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            final boolean questionCount1 = objectMap.containsKey("questionCount");
//            QuestionCount questionCount = new QuestionCount();
//            if (questionCount1) {
//                log.info("Found ,...");
//                questionCount.setQuestionCount(objectMap.get("questionCount").toString());
//            }
//
//            if (objectMap.containsKey("questionVos")) {
//                log.info("found...");
//
//
//                List<QuestionPaperVo> questionVos = objectMapper.readValue(
//                        objectMap.get("questionVos").toString(), new TypeReference<List<QuestionPaperVo>>() {
//                        } // Convert to List<QuestionVo>
//                );
//                questionCount.setQuestionVos(questionVos); // Set in object
//            }            // Convert the map to `QuestionCount`
//
//// Fix for `questionVos` (which is coming as a JSON string)
////            if (objectMap.containsKey("questionVos")) {
////                String questionVosJson = (String) objectMap.get("questionVos"); // Get JSON String
////                List<QuestionVo> questionVos = objectMapper.readValue(
////                        questionVosJson, new TypeReference<List<QuestionVo>>() {} // Convert to List<QuestionVo>
////                );
////                questionCount.setQuestionVos(questionVos); // Set in object
////            }
////            if (responseBody == null || !responseBody.containsKey("questionCount") || !responseBody.containsKey("questionVos")) {
////                throw new NotFoundException("Question details not found");
////            }
//
//// Extract `questionCount`
////            String questionCount = (String) responseBody.get("questionCount");
//
//// Extract `questionVos` from String and convert to List<QuestionVo>
////            ObjectMapper objectMapper = new ObjectMapper();
////            List<QuestionVo> questionVos = objectMapper.readValue(
////                    responseBody.get("questionVos").toString(),
////                    new TypeReference<List<QuestionVo>>() {}
////            );
//
////            System.out.println("Questions List: " + questionVos);
////
////
////            QuestionCount questionCountResponse = new QuestionCount();
////            questionCountResponse.setQuestionCount(questionCount);
////            questionCountResponse.setQuestionVos(questionVos);
////
////            return questionCountResponse;
//
//            log.info("Question Count : " + questionCount);
//            return questionCount;
//
//
//        } catch (HttpClientErrorException | HttpServerErrorException ex) {
//            log.error("HTTP Error: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
//            throw new RuntimeException("Failed to retrieve questions: " + ex.getMessage());
//        } catch (Exception e) {
//            log.error("Exception occurred: {}", e.getMessage());
//            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
//        }
//    }
//

}
