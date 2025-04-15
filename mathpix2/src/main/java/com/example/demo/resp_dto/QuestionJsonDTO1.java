//package com.example.demo.resp_dto;
//
//package com.example.demo.resp_dto;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
////
////@JsonIgnoreProperties(ignoreUnknown = true)
////public class QuestionJsonDTO {
////
////    @JsonProperty("questionId")
////    private Long questionId;
////
////    @JsonProperty("questionLatex")
////    private String questionLatex;
////
////    @JsonDeserialize(using = OptionsLatexDeserializer.class) // Custom deserializer for handling "NA"
////    @JsonProperty("optionsLatex")
////    private List<String> optionsLatex;
////
////    @JsonProperty("solutionLatex")
////    private String solutionLatex;
////
////    @JsonProperty("answerKey")
////    private String answerKey;
////
////    @JsonProperty("complexity")
////    private int complexity;
////
////    @JsonProperty("topics")
////    private List<String> topics;
////
////    @JsonProperty("passageLatex")
////    private String passageLatex;
////
////    // Getters and Setters
////    public Long getQuestionId() {
////        return questionId;
////    }
////
////    public void setQuestionId(String questionId) {
////        this.questionId = Long.parseLong(questionId); // Convert String to Long
////    }
////
////    public String getQuestionLatex() {
////        return questionLatex;
////    }
////
////    public void setQuestionLatex(String questionLatex) {
////        this.questionLatex = questionLatex;
////    }
////
////    public List<String> getOptionsLatex() {
////        return optionsLatex;
////    }
////
////    public void setOptionsLatex(List<String> optionsLatex) {
////        this.optionsLatex = optionsLatex;
////    }
////
////    public String getSolutionLatex() {
////        return solutionLatex;
////    }
////
////    public void setSolutionLatex(String solutionLatex) {
////        this.solutionLatex = solutionLatex;
////    }
////
////    public String getAnswerKey() {
////        return answerKey;
////    }
////
////    public void setAnswerKey(String answerKey) {
////        this.answerKey = answerKey;
////    }
////
////    public int getComplexity() {
////        return complexity;
////    }
////
////    public void setComplexity(String complexity) {
////        this.complexity = Integer.parseInt(complexity); // Convert String to int
////    }
////
////    public List<String> getTopics() {
////        return topics;
////    }
////
////    public void setTopics(List<String> topics) {
////        this.topics = topics;
////    }
////
////    public String getPassageLatex() {
////        return passageLatex;
////    }
////
////    public void setPassageLatex(String passageLatex) {
////        this.passageLatex = passageLatex;
////    }
////}
//
////@JsonIgnoreProperties(ignoreUnknown = true)
////public class QuestionJsonDTO {
////
////    @JsonProperty("questionId")
////    private Long questionId;
////
////    @JsonProperty("questionLatex")
////    private String questionLatex;
////
////    @JsonDeserialize(using = OptionsLatexDeserializer.class) // Custom deserializer for handling "NA"
////    @JsonProperty("optionsLatex")
////    private List<String> optionsLatex;
////
////    @JsonProperty("solutionLatex")
////    private String solutionLatex;
////
////    @JsonProperty("answerKey")
////    private String answerKey;
////
////    @JsonProperty("complexity")
////    private int complexity;
////
////    @JsonProperty("topics")
////    private List<String> topics;
////
////    @JsonProperty("passageLatex")
////    private String passageLatex;
////
////    // Getters and Setters
////    public Long getQuestionId() {
////        return questionId;
////    }
////
////    public void setQuestionId(String questionId) {
////        this.questionId = Long.parseLong(questionId);
////    }
////
////    public String getQuestionLatex() {
////        return questionLatex;
////    }
////
////    public void setQuestionLatex(String questionLatex) {
////        this.questionLatex = questionLatex;
////    }
////
////    public List<String> getOptionsLatex() {
////        return optionsLatex;
////    }
////
////    public void setOptionsLatex(List<String> optionsLatex) {
////        this.optionsLatex = optionsLatex;
////    }
////
////    public String getSolutionLatex() {
////        return solutionLatex;
////    }
////
////    public void setSolutionLatex(String solutionLatex) {
////        this.solutionLatex = solutionLatex;
////    }
////
////    public String getAnswerKey() {
////        return answerKey;
////    }
////
////    public void setAnswerKey(String answerKey) {
////        this.answerKey = answerKey;
////    }
////
////    public int getComplexity() {
////        return complexity;
////    }
////
////    public void setComplexity(String complexity) {
////        this.complexity = Integer.parseInt(complexity);
////    }
////
////    public List<String> getTopics() {
////        return topics;
////    }
////
////    public void setTopics(Object topics) {
////        if (topics instanceof String) {
////            // Convert a single string into a list
////            this.topics = Arrays.asList((String) topics);
////        } else if (topics instanceof List) {
////            // Use the list as is
////            this.topics = (List<String>) topics;
////        } else {
////            this.topics = null; // Handle null case
////        }
////    }
////
////    public String getPassageLatex() {
////        return passageLatex;
////    }
////
////    public void setPassageLatex(String passageLatex) {
////        this.passageLatex = passageLatex;
////    }
////}
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class QuestionJsonDTO1 {
//
//    @JsonProperty("questionId")
//    private Long questionId;
//
//    @JsonDeserialize(using = SafeLatexDeserializer.class)
//    @JsonProperty("questionLatex")
//    private String questionLatex;
//
//
//    @JsonProperty("optionsLatex")
//    @JsonDeserialize(using = OptionsLatexDeserializer.class) // Custom deserializer for handling "NA"
//    private List<String> optionsLatex;
//
//    @JsonProperty("solutionLatex")
//    private String solutionLatex;
//
//    @JsonProperty("answerKey")
//    @JsonDeserialize(using = AnswerKeyDeserializer.class)
//    private String answerKey;
//
//    @JsonProperty("complexity")
//    private int complexity;
//
//    @JsonProperty("topics")
//    @JsonDeserialize(using = TopicsDeserializer.class)
//    private List<String> topics;
//
//    //    @JsonProperty("passageLatex")
////    @JsonDeserialize(using = LatexDeserializer.class)
////    private String passageLatex;
//    @JsonDeserialize(using = SafeLatexDeserializer.class)
//    @JsonProperty("passageLatex")
//    private String passageLatex;
//
//
//    @JsonProperty("options")
//    @JsonDeserialize(using = OptionsDeserializer.class)
//    private Map<String, String> options = new LinkedHashMap<>();
//
//
//    public Map<String, String> getOptions() {
//        return options;
//    }
//
//    public void setOptions(Map<String, String> options) {
//        this.options = options;
//    }
//
//
//}
//
