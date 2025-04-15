package com.example.demo.ExternalDto;



import java.awt.image.BufferedImage;
import java.sql.Blob;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.context.annotation.EnableMBeanExport;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Question extends BaseEo {
	private String question;
	private String questionText;
	private Integer courseId;
	private String courseName;
	private Integer subjectId;
	private String subjectName;
	private Integer chapterId;
	private String chapterName;
	private Integer topicId;
	private String topicName;
	private Integer complexity;
	private Long timeLimit;
	private String summary;
	private String copyright;
	private Boolean approved;
	private Integer type;
	private Integer questionStatus;
	private String comment;
	private Boolean deleted;
	private String solution;
	private String solutionText;
	private String solutionHint;
	private String tag;
	private String tagTypeName;
	private String genericImage;
	private String hdpiImage;
	private String mdpiImage;
	private String ldpiImage;
	private String a4Image;
	private Map<String, Boolean> options;
	private Set<Integer> courseIds;
	private Set<Integer> subjectIds;
	private Set<Integer> chapterIds;
	private Set<Integer> topicIds;
	private Integer operatorId;
	private Integer duration;
	private Boolean isSolutionAvailable = Boolean.FALSE;
	private Integer questionTypeId;
	private String questionTypeName;
	private Integer groupId;
	private String groupName;
	private String groupImage;
	private String groupText;
	private Boolean isMultipleAnswer = Boolean.FALSE;
	private Blob genericBlobImage;
	private String numericAnswer;
	private Integer partialRule = 1;
	private String[] columnMatchAnswer;
	private List<Question> duplicateQuestions;
	private Integer questionSubType;
	private Integer duplicateMapperId;
	private String dedupeCourses;
	private String dedupeSubjects;
	private String dedupeChapters;
	private Set<QuestionMappers> questionMappers;
	private String answer;
	private Integer correctAnsCount;
	private Integer inCorrectAnsCount;
	private Integer issueReportedCount;
	
	//added by PS as per JE277
	private String genericImageUrl;
	private String hdpiImageUrl;
	private String mdpiImageUrl;
	private String ldpiImageUrl;
	private String a4ImageUrl;
	private String questionUrl;
	private String solutionUrl;
	
	//for faculty app
	private Integer attemptedQuesCount;
	private Integer unAttemptedQuesCount;
	private Integer optionACount;
	private Integer optionBCount;
	private Integer optionCCount;
	private Integer optionDCount;
	private Integer averageTimeTaken;
	
	
	


		

		public String getQuestionUrl() {
			return questionUrl;
		}

		public void setQuestionUrl(String questionUrl) {
			this.questionUrl = questionUrl;
		}

		public String getSolutionUrl() {
			return solutionUrl;
		}

		public void setSolutionUrl(String solutionUrl) {
			this.solutionUrl = solutionUrl;
		}
		
		public String getGenericImageUrl() {
			return genericImageUrl;
		}

		public void setGenericImageUrl(String genericImageUrl) {
			this.genericImageUrl = genericImageUrl;
		}

		public String getHdpiImageUrl() {
			return hdpiImageUrl;
		}

		public void setHdpiImageUrl(String hdpiImageUrl) {
			this.hdpiImageUrl = hdpiImageUrl;
		}

		public String getMdpiImageUrl() {
			return mdpiImageUrl;
		}

		public void setMdpiImageUrl(String mdpiImageUrl) {
			this.mdpiImageUrl = mdpiImageUrl;
		}

		public String getLdpiImageUrl() {
			return ldpiImageUrl;
		}

		public void setLdpiImageUrl(String ldpiImageUrl) {
			this.ldpiImageUrl = ldpiImageUrl;
		}

		public String getA4ImageUrl() {
			return a4ImageUrl;
		}

		public void setA4ImageUrl(String a4ImageUrl) {
			this.a4ImageUrl = a4ImageUrl;
		}


	public Integer getCorrectAnsCount() {
		return correctAnsCount;
	}

	public void setCorrectAnsCount(Integer correctAnsCount) {
		this.correctAnsCount = correctAnsCount;
	}

	public Integer getInCorrectAnsCount() {
		return inCorrectAnsCount;
	}

	public void setInCorrectAnsCount(Integer inCorrectAnsCount) {
		this.inCorrectAnsCount = inCorrectAnsCount;
	}

	public Integer getIssueReportedCount() {
		return issueReportedCount;
	}

	public void setIssueReportedCount(Integer issueReportedCount) {
		this.issueReportedCount = issueReportedCount;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Set<QuestionMappers> getQuestionMappers() {
		return questionMappers;
	}

	public void setQuestionMappers(Set<QuestionMappers> questionMappers) {
		this.questionMappers = questionMappers;
	}

	public Integer getDuplicateMapperId() {
		return duplicateMapperId;
	}

	public String getDedupeCourses() {
		return dedupeCourses;
	}

	public void setDedupeCourses(String dedupeCourses) {
		this.dedupeCourses = dedupeCourses;
	}

	public String getDedupeSubjects() {
		return dedupeSubjects;
	}

	public void setDedupeSubjects(String dedupeSubjects) {
		this.dedupeSubjects = dedupeSubjects;
	}

	public String getDedupeChapters() {
		return dedupeChapters;
	}

	public void setDedupeChapters(String dedupeChapters) {
		this.dedupeChapters = dedupeChapters;
	}

	public void setDuplicateMapperId(Integer duplicateMapperId) {
		this.duplicateMapperId = duplicateMapperId;
	}

	public Integer getQuestionSubType() {
		return questionSubType;
	}

	public void setQuestionSubType(Integer questionSubType) {
		this.questionSubType = questionSubType;
	}

	public List<Question> getDuplicateQuestions() {
		return duplicateQuestions;
	}

	public void setDuplicateQuestions(List<Question> duplicateQuestions) {
		this.duplicateQuestions = duplicateQuestions;
	}

	public String[] getColumnMatchAnswer() {
		return columnMatchAnswer;
	}

	public void setColumnMatchAnswer(String[] columnMatchAnswer) {
		this.columnMatchAnswer = columnMatchAnswer;
	}

	public Integer getPartialRule() {
		return partialRule;
	}

	public void setPartialRule(Integer partialRule) {
		this.partialRule = partialRule;
	}

	public String getNumericAnswer() {
		return numericAnswer;
	}

	public void setNumericAnswer(String numericAnswer) {
		this.numericAnswer = numericAnswer;
	}

	/**
	 * @return the duration
	 */
	public Integer getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	
	/**
	 * 
	 */
	public Question() {
		super();
	}

	public Question(QuestionType type) {
		this.type = type.getCode();
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getChapterId() {
		return chapterId;
	}

	public void setChapterId(Integer chapterId) {
		this.chapterId = chapterId;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public Integer getComplexity() {
		return complexity;
	}

	public void setComplexity(Integer complexity) {
		this.complexity = complexity;
	}

	public Long getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Long timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public Integer getQuestionStatus() {
		return questionStatus;
	}

	public void setQuestionStatus(Integer questionStatus) {
		this.questionStatus = questionStatus;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public String getSolutionText() {
		return solutionText;
	}

	public void setSolutionText(String solutionText) {
		this.solutionText = solutionText;
	}

	public String getSolutionHint() {
		return solutionHint;
	}

	public void setSolutionHint(String solutionHint) {
		this.solutionHint = solutionHint;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTagTypeName() {
		return tagTypeName;
	}

	public void setTagTypeName(String tagTypeName) {
		this.tagTypeName = tagTypeName;
	}

	public String getGenericImage() {
		return genericImage;
	}

	public void setGenericImage(String genericImage) {
		this.genericImage = genericImage;
	}

	public String getHdpiImage() {
		return hdpiImage;
	}

	public void setHdpiImage(String hdpiImage) {
		this.hdpiImage = hdpiImage;
	}

	public String getMdpiImage() {
		return mdpiImage;
	}

	public void setMdpiImage(String mdpiImage) {
		this.mdpiImage = mdpiImage;
	}

	public String getLdpiImage() {
		return ldpiImage;
	}

	public void setLdpiImage(String ldpiImage) {
		this.ldpiImage = ldpiImage;
	}

	public String getA4Image() {
		return a4Image;
	}

	public void setA4Image(String a4Image) {
		this.a4Image = a4Image;
	}

	public Map<String, Boolean> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Boolean> options) {
		this.options = options;
	}

	public Integer getType() {
		return type;
	}

	public Set<Integer> getCourseIds() {
		return courseIds;
	}

	public void setCourseIds(Set<Integer> courseIds) {
		this.courseIds = courseIds;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public Set<Integer> getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(Set<Integer> subjectIds) {
		this.subjectIds = subjectIds;
	}

	public Set<Integer> getChapterIds() {
		return chapterIds;
	}

	public void setChapterIds(Set<Integer> chapterIds) {
		this.chapterIds = chapterIds;
	}

	public Set<Integer> getTopicIds() {
		return topicIds;
	}

	public void setTopicIds(Set<Integer> topicIds) {
		this.topicIds = topicIds;
	}

	public Boolean getIsSolutionAvailable() {
		return isSolutionAvailable;
	}

	public void setIsSolutionAvailable(Boolean isSolutionAvailable) {
		this.isSolutionAvailable = isSolutionAvailable;
	}

	public Integer getQuestionTypeId() {
		return questionTypeId;
	}

	public void setQuestionTypeId(Integer questionTypeId) {
		this.questionTypeId = questionTypeId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getQuestionTypeName() {
		return questionTypeName;
	}

	public void setQuestionTypeName(String questionTypeName) {
		this.questionTypeName = questionTypeName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupImage() {
		return groupImage;
	}

	public void setGroupImage(String groupImage) {
		this.groupImage = groupImage;
	}

	public String getGroupText() {
		return groupText;
	}

	public void setGroupText(String groupText) {
		this.groupText = groupText;
	}

	public Boolean getIsMultipleAnswer() {
		return isMultipleAnswer;
	}

	public void setIsMultipleAnswer(Boolean isMultipleAnswer) {
		this.isMultipleAnswer = isMultipleAnswer;
	}

	public Blob getGenericBlobImage() {
		return genericBlobImage;
	}

	public void setGenericBlobImage(Blob genericBlobImage) {
		this.genericBlobImage = genericBlobImage;
	}
	
	
	public Integer getAttemptedQuesCount() {
		return attemptedQuesCount;
	}

	public void setAttemptedQuesCount(Integer attemptedQuesCount) {
		this.attemptedQuesCount = attemptedQuesCount;
	}

	public Integer getUnAttemptedQuesCount() {
		return unAttemptedQuesCount;
	}

	public void setUnAttemptedQuesCount(Integer unAttemptedQuesCount) {
		this.unAttemptedQuesCount = unAttemptedQuesCount;
	}

	public Integer getOptionACount() {
		return optionACount;
	}

	public void setOptionACount(Integer optionACount) {
		this.optionACount = optionACount;
	}

	public Integer getOptionBCount() {
		return optionBCount;
	}

	public void setOptionBCount(Integer optionBCount) {
		this.optionBCount = optionBCount;
	}

	public Integer getOptionCCount() {
		return optionCCount;
	}

	public void setOptionCCount(Integer optionCCount) {
		this.optionCCount = optionCCount;
	}

	public Integer getOptionDCount() {
		return optionDCount;
	}

	public void setOptionDCount(Integer optionDCount) {
		this.optionDCount = optionDCount;
	}
	
	public Integer getAverageTimeTaken() {
		return averageTimeTaken;
	}

	public void setAverageTimeTaken(Integer averageTimeTaken) {
		this.averageTimeTaken = averageTimeTaken;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((a4Image == null) ? 0 : a4Image.hashCode());
		result = prime * result + ((approved == null) ? 0 : approved.hashCode());
		result = prime * result + ((chapterId == null) ? 0 : chapterId.hashCode());
		result = prime * result + ((chapterIds == null) ? 0 : chapterIds.hashCode());
		result = prime * result + ((chapterName == null) ? 0 : chapterName.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((complexity == null) ? 0 : complexity.hashCode());
		result = prime * result + ((copyright == null) ? 0 : copyright.hashCode());
		result = prime * result + ((courseId == null) ? 0 : courseId.hashCode());
		result = prime * result + ((courseIds == null) ? 0 : courseIds.hashCode());
		result = prime * result + ((courseName == null) ? 0 : courseName.hashCode());
		result = prime * result + ((deleted == null) ? 0 : deleted.hashCode());
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((genericBlobImage == null) ? 0 : genericBlobImage.hashCode());
		result = prime * result + ((genericImage == null) ? 0 : genericImage.hashCode());
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		result = prime * result + ((groupImage == null) ? 0 : groupImage.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((groupText == null) ? 0 : groupText.hashCode());
		result = prime * result + ((hdpiImage == null) ? 0 : hdpiImage.hashCode());
		result = prime * result + ((isMultipleAnswer == null) ? 0 : isMultipleAnswer.hashCode());
		result = prime * result + ((isSolutionAvailable == null) ? 0 : isSolutionAvailable.hashCode());
		result = prime * result + ((ldpiImage == null) ? 0 : ldpiImage.hashCode());
		result = prime * result + ((mdpiImage == null) ? 0 : mdpiImage.hashCode());
		result = prime * result + ((numericAnswer == null) ? 0 : numericAnswer.hashCode());
		result = prime * result + ((operatorId == null) ? 0 : operatorId.hashCode());
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		result = prime * result + ((partialRule == null) ? 0 : partialRule.hashCode());
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		result = prime * result + ((questionStatus == null) ? 0 : questionStatus.hashCode());
		result = prime * result + ((questionText == null) ? 0 : questionText.hashCode());
		result = prime * result + ((questionTypeId == null) ? 0 : questionTypeId.hashCode());
		result = prime * result + ((questionTypeName == null) ? 0 : questionTypeName.hashCode());
		result = prime * result + ((solution == null) ? 0 : solution.hashCode());
		result = prime * result + ((solutionHint == null) ? 0 : solutionHint.hashCode());
		result = prime * result + ((solutionText == null) ? 0 : solutionText.hashCode());
		result = prime * result + ((subjectId == null) ? 0 : subjectId.hashCode());
		result = prime * result + ((subjectIds == null) ? 0 : subjectIds.hashCode());
		result = prime * result + ((subjectName == null) ? 0 : subjectName.hashCode());
		result = prime * result + ((summary == null) ? 0 : summary.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + ((tagTypeName == null) ? 0 : tagTypeName.hashCode());
		result = prime * result + ((timeLimit == null) ? 0 : timeLimit.hashCode());
		result = prime * result + ((topicId == null) ? 0 : topicId.hashCode());
		result = prime * result + ((topicIds == null) ? 0 : topicIds.hashCode());
		result = prime * result + ((topicName == null) ? 0 : topicName.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((genericImageUrl == null) ? 0 : genericImageUrl.hashCode());
		result = prime * result + ((hdpiImageUrl == null) ? 0 : hdpiImageUrl.hashCode());
		result = prime * result + ((ldpiImageUrl == null) ? 0 : ldpiImageUrl.hashCode());
		result = prime * result + ((a4ImageUrl == null) ? 0 : a4ImageUrl.hashCode());
		result = prime * result + ((mdpiImageUrl == null) ? 0 : mdpiImageUrl.hashCode());
		result = prime * result + ((questionUrl == null) ? 0 : questionUrl.hashCode());
		result = prime * result + ((solutionUrl == null) ? 0 : solutionUrl.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (a4Image == null) {
			if (other.a4Image != null)
				return false;
		} else if (!a4Image.equals(other.a4Image))
			return false;
		if (approved == null) {
			if (other.approved != null)
				return false;
		} else if (!approved.equals(other.approved))
			return false;
		if (chapterId == null) {
			if (other.chapterId != null)
				return false;
		} else if (!chapterId.equals(other.chapterId))
			return false;
		if (chapterIds == null) {
			if (other.chapterIds != null)
				return false;
		} else if (!chapterIds.equals(other.chapterIds))
			return false;
		if (chapterName == null) {
			if (other.chapterName != null)
				return false;
		} else if (!chapterName.equals(other.chapterName))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (complexity == null) {
			if (other.complexity != null)
				return false;
		} else if (!complexity.equals(other.complexity))
			return false;
		if (copyright == null) {
			if (other.copyright != null)
				return false;
		} else if (!copyright.equals(other.copyright))
			return false;
		if (courseId == null) {
			if (other.courseId != null)
				return false;
		} else if (!courseId.equals(other.courseId))
			return false;
		if (courseIds == null) {
			if (other.courseIds != null)
				return false;
		} else if (!courseIds.equals(other.courseIds))
			return false;
		if (courseName == null) {
			if (other.courseName != null)
				return false;
		} else if (!courseName.equals(other.courseName))
			return false;
		if (deleted == null) {
			if (other.deleted != null)
				return false;
		} else if (!deleted.equals(other.deleted))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (genericBlobImage == null) {
			if (other.genericBlobImage != null)
				return false;
		} else if (!genericBlobImage.equals(other.genericBlobImage))
			return false;
		if (genericImage == null) {
			if (other.genericImage != null)
				return false;
		} else if (!genericImage.equals(other.genericImage))
			return false;
		if (groupId == null) {
			if (other.groupId != null)
				return false;
		} else if (!groupId.equals(other.groupId))
			return false;
		if (groupImage == null) {
			if (other.groupImage != null)
				return false;
		} else if (!groupImage.equals(other.groupImage))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (groupText == null) {
			if (other.groupText != null)
				return false;
		} else if (!groupText.equals(other.groupText))
			return false;
		if (hdpiImage == null) {
			if (other.hdpiImage != null)
				return false;
		} else if (!hdpiImage.equals(other.hdpiImage))
			return false;
		if (isMultipleAnswer == null) {
			if (other.isMultipleAnswer != null)
				return false;
		} else if (!isMultipleAnswer.equals(other.isMultipleAnswer))
			return false;
		if (isSolutionAvailable == null) {
			if (other.isSolutionAvailable != null)
				return false;
		} else if (!isSolutionAvailable.equals(other.isSolutionAvailable))
			return false;
		if (ldpiImage == null) {
			if (other.ldpiImage != null)
				return false;
		} else if (!ldpiImage.equals(other.ldpiImage))
			return false;
		if (mdpiImage == null) {
			if (other.mdpiImage != null)
				return false;
		} else if (!mdpiImage.equals(other.mdpiImage))
			return false;
		if (numericAnswer == null) {
			if (other.numericAnswer != null)
				return false;
		} else if (!numericAnswer.equals(other.numericAnswer))
			return false;
		if (operatorId == null) {
			if (other.operatorId != null)
				return false;
		} else if (!operatorId.equals(other.operatorId))
			return false;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (!options.equals(other.options))
			return false;
		if (partialRule == null) {
			if (other.partialRule != null)
				return false;
		} else if (!partialRule.equals(other.partialRule))
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		if (questionStatus == null) {
			if (other.questionStatus != null)
				return false;
		} else if (!questionStatus.equals(other.questionStatus))
			return false;
		if (questionText == null) {
			if (other.questionText != null)
				return false;
		} else if (!questionText.equals(other.questionText))
			return false;
		if (questionTypeId == null) {
			if (other.questionTypeId != null)
				return false;
		} else if (!questionTypeId.equals(other.questionTypeId))
			return false;
		if (questionTypeName == null) {
			if (other.questionTypeName != null)
				return false;
		} else if (!questionTypeName.equals(other.questionTypeName))
			return false;
		if (solution == null) {
			if (other.solution != null)
				return false;
		} else if (!solution.equals(other.solution))
			return false;
		if (solutionHint == null) {
			if (other.solutionHint != null)
				return false;
		} else if (!solutionHint.equals(other.solutionHint))
			return false;
		if (solutionText == null) {
			if (other.solutionText != null)
				return false;
		} else if (!solutionText.equals(other.solutionText))
			return false;
		if (subjectId == null) {
			if (other.subjectId != null)
				return false;
		} else if (!subjectId.equals(other.subjectId))
			return false;
		if (subjectIds == null) {
			if (other.subjectIds != null)
				return false;
		} else if (!subjectIds.equals(other.subjectIds))
			return false;
		if (subjectName == null) {
			if (other.subjectName != null)
				return false;
		} else if (!subjectName.equals(other.subjectName))
			return false;
		if (summary == null) {
			if (other.summary != null)
				return false;
		} else if (!summary.equals(other.summary))
			return false;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		if (tagTypeName == null) {
			if (other.tagTypeName != null)
				return false;
		} else if (!tagTypeName.equals(other.tagTypeName))
			return false;
		if (timeLimit == null) {
			if (other.timeLimit != null)
				return false;
		} else if (!timeLimit.equals(other.timeLimit))
			return false;
		if (topicId == null) {
			if (other.topicId != null)
				return false;
		} else if (!topicId.equals(other.topicId))
			return false;
		if (topicIds == null) {
			if (other.topicIds != null)
				return false;
		} else if (!topicIds.equals(other.topicIds))
			return false;
		if (topicName == null) {
			if (other.topicName != null)
				return false;
		} else if (!topicName.equals(other.topicName))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (genericImageUrl == null) {
			if (other.genericImageUrl != null)
				return false;
		} else if (!genericImageUrl.equals(other.genericImageUrl))
			return false;
		if (ldpiImageUrl == null) {
			if (other.ldpiImageUrl != null)
				return false;
		} else if (!ldpiImageUrl.equals(other.ldpiImageUrl))
			return false;
		if (mdpiImageUrl == null) {
			if (other.mdpiImageUrl != null)
				return false;
		} else if (!mdpiImageUrl.equals(other.mdpiImageUrl))
			return false;
		if (hdpiImageUrl == null) {
			if (other.hdpiImageUrl != null)
				return false;
		} else if (!hdpiImageUrl.equals(other.hdpiImageUrl))
			return false;
		if (a4ImageUrl == null) {
			if (other.a4ImageUrl != null)
				return false;
		} else if (!a4ImageUrl.equals(other.a4ImageUrl))
			return false;
		if (questionUrl == null) {
			if (other.questionUrl != null)
				return false;
		} else if (!questionUrl.equals(other.questionUrl))
			return false;
		if (solutionUrl == null) {
			if (other.solutionUrl != null)
				return false;
		} else if (!solutionUrl.equals(other.solutionUrl))
			return false;
		return true;
	}

	public Question(String question, String questionText, Integer courseId, String courseName, Integer subjectId,
			String subjectName, Integer chapterId, String chapterName, Integer topicId, String topicName,
			Integer complexity, Long timeLimit, String summary, String copyright, Boolean approved, Integer type,
			Integer questionStatus, String comment, Boolean deleted, String solution, String solutionText,
			String solutionHint, String tag, String tagTypeName, String genericImage, String hdpiImage,
			String mdpiImage, String ldpiImage, String a4Image, Map<String, Boolean> options, Set<Integer> courseIds,
			Set<Integer> subjectIds, Set<Integer> chapterIds, Set<Integer> topicIds, Integer operatorId,
			Integer duration, Boolean isSolutionAvailable, Integer questionTypeId, String questionTypeName,
			Integer groupId, String groupName, String groupImage, String groupText, Boolean isMultipleAnswer,
			Blob genericBlobImage, String numericAnswer, Integer partialRule, String[] columnMatchAnswer,
			String genericImageUrl,String hdpiImageUrl,String mdpiImageUrl,String ldpiImageUrl,String a4ImageUrl,
			String questionUrl, String solutionUrl,Integer attemptedQuesCount,Integer unAttemptedQuesCount, 
			Integer optionACount,Integer optionBCount,Integer optionCCount,Integer optionDCount, Integer averageTimeTaken) {
		super();
		this.question = question;
		this.questionText = questionText;
		this.courseId = courseId;
		this.courseName = courseName;
		this.subjectId = subjectId;
		this.subjectName = subjectName;
		this.chapterId = chapterId;
		this.chapterName = chapterName;
		this.topicId = topicId;
		this.topicName = topicName;
		this.complexity = complexity;
		this.timeLimit = timeLimit;
		this.summary = summary;
		this.copyright = copyright;
		this.approved = approved;
		this.type = type;
		this.questionStatus = questionStatus;
		this.comment = comment;
		this.deleted = deleted;
		this.solution = solution;
		this.solutionText = solutionText;
		this.solutionHint = solutionHint;
		this.tag = tag;
		this.tagTypeName = tagTypeName;
		this.genericImage = genericImage;
		this.hdpiImage = hdpiImage;
		this.mdpiImage = mdpiImage;
		this.ldpiImage = ldpiImage;
		this.a4Image = a4Image;
		this.options = options;
		this.courseIds = courseIds;
		this.subjectIds = subjectIds;
		this.chapterIds = chapterIds;
		this.topicIds = topicIds;
		this.operatorId = operatorId;
		this.duration = duration;
		this.isSolutionAvailable = isSolutionAvailable;
		this.questionTypeId = questionTypeId;
		this.questionTypeName = questionTypeName;
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupImage = groupImage;
		this.groupText = groupText;
		this.isMultipleAnswer = isMultipleAnswer;
		this.genericBlobImage = genericBlobImage;
		this.numericAnswer = numericAnswer;
		this.partialRule = partialRule;
		this.columnMatchAnswer = columnMatchAnswer;
		this.genericImageUrl = genericImageUrl;
		this.mdpiImageUrl = mdpiImageUrl;
		this.hdpiImageUrl = hdpiImageUrl;
		this.ldpiImageUrl = ldpiImageUrl;
		this.a4ImageUrl = a4ImageUrl;
		this.questionUrl = questionUrl;
		this.solutionUrl = solutionUrl;
		this.attemptedQuesCount=attemptedQuesCount;
		this.unAttemptedQuesCount= unAttemptedQuesCount;
		this.optionACount= optionACount;
		this.optionBCount= optionBCount;
		this.optionCCount= optionCCount;
		this.optionDCount= optionDCount;
		this.averageTimeTaken= averageTimeTaken;
	}

	

}