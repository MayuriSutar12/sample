package com.example.demo.ExternalDto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Blob;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionVo {

	private int id;
	private String question;
	private String questionText;
	private int courseId;
	private String courseName;
	private int subjectId;
	private String subjectName;
	private int chapterId;
	private String chapterName;
	private int topicId;
	private String topicName;
	private int complexity;
	private long timeLimit;
	private String summary;
	private String copyright;
	private boolean approved;
	private int type;
	private int questionStatus;
	private String comment;
	private boolean deleted;
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
	private int operatorId;
	private int duration;
	private boolean isSolutionAvailable = false;
	private byte[] facultySolution;
	private int questionTypeId;
	private int groupId;
	private String questionTypeName;
	private String groupName;
	private String groupImage;
	private String groupText;
	private boolean isMultipleAnswer = Boolean.FALSE;
	private Blob genericBlobImage;
	private String numericAnswer;
	private String[] columnMatchAnswer;
	private List<Question> duplicateQuestions;
	private int questionSubType;
	private Set<QuestionMappers> questionMappers;
	private String answer;
	private Integer correctAnsCount;
	private Integer inCorrectAnsCount;
	private Integer issueReportedCount;
	//added by PS as per JE291
	private String genericImageUrl;
	private String hdpiImageUrl;
	private String mdpiImageUrl;
	private String ldpiImageUrl;
	private String a4ImageUrl;
	private String questionUrl;
	private String solutionUrl;
	//added for Faculty App
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

	public int getQuestionSubType() {
		return questionSubType;
	}

	public void setQuestionSubType(int questionSubType) {
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

	public String getNumericAnswer() {
		return numericAnswer;
	}

	public void setNumericAnswer(String numericAnswer) {
		this.numericAnswer = numericAnswer;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	

	/**
	 * 
	 */
	public QuestionVo() {
		super();
	}

	public QuestionVo(QuestionType type) {
		this.type = type.getCode();
	}
	
	public QuestionVo(int type) {
		this.type = type;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public int getChapterId() {
		return chapterId;
	}

	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public int getComplexity() {
		return complexity;
	}

	public void setComplexity(int complexity) {
		this.complexity = complexity;
	}

	public long getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(long timeLimit) {
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

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approvalStatus) {
		this.approved = approvalStatus;
	}

	public int getType() {
		return type;
	}

	public int getQuestionStatus() {
		return questionStatus;
	}

	public void setQuestionStatus(int questionStatus) {
		this.questionStatus = questionStatus;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
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

	public Map<String, Boolean> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Boolean> options) {
		this.options = options;
	}

	public String getSolutionText() {
		return solutionText;
	}

	public void setSolutionText(String solutionText) {
		this.solutionText = solutionText;
	}

	public String getA4Image() {
		return a4Image;
	}

	public void setA4Image(String a4Image) {
		this.a4Image = a4Image;
	}

	public String getTagTypeName() {
		return tagTypeName;
	}

	public void setTagTypeName(String tagTypeName) {
		this.tagTypeName = tagTypeName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public Set<Integer> getCourseIds() {
		return courseIds;
	}

	public void setCourseIds(Set<Integer> courseIds) {
		this.courseIds = courseIds;
	}

	public int getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(int operatorId) {
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

	public boolean isSolutionAvailable() {
		return isSolutionAvailable;
	}

	public void setSolutionAvailable(boolean isSolutionAvailable) {
		this.isSolutionAvailable = isSolutionAvailable;
	}

	public byte[] getFacultySolution() {
		return facultySolution;
	}

	public void setFacultySolution(byte[] facultySolution) {
		this.facultySolution = facultySolution;
	}

	public int getQuestionTypeId() {
		return questionTypeId;
	}

	public void setQuestionTypeId(int questionTypeId) {
		this.questionTypeId = questionTypeId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
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

	public boolean isMultipleAnswer() {
		return isMultipleAnswer;
	}

	public void setMultipleAnswer(boolean isMultipleAnswer) {
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
		int result = 1;
		result = prime * result + ((a4Image == null) ? 0 : a4Image.hashCode());
		result = prime * result + (approved ? 1231 : 1237);
		result = prime * result + chapterId;
		result = prime * result + ((chapterIds == null) ? 0 : chapterIds.hashCode());
		result = prime * result + ((chapterName == null) ? 0 : chapterName.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + complexity;
		result = prime * result + ((copyright == null) ? 0 : copyright.hashCode());
		result = prime * result + courseId;
		result = prime * result + ((courseIds == null) ? 0 : courseIds.hashCode());
		result = prime * result + ((courseName == null) ? 0 : courseName.hashCode());
		result = prime * result + (deleted ? 1231 : 1237);
		result = prime * result + duration;
		result = prime * result + Arrays.hashCode(facultySolution);
		result = prime * result + ((genericImage == null) ? 0 : genericImage.hashCode());
		result = prime * result + groupId;
		result = prime * result + ((groupImage == null) ? 0 : groupImage.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
		result = prime * result + ((groupText == null) ? 0 : groupText.hashCode());
		result = prime * result + ((hdpiImage == null) ? 0 : hdpiImage.hashCode());
		result = prime * result + id;
		result = prime * result + (isMultipleAnswer ? 1231 : 1237);
		result = prime * result + (isSolutionAvailable ? 1231 : 1237);
		result = prime * result + ((ldpiImage == null) ? 0 : ldpiImage.hashCode());
		result = prime * result + ((mdpiImage == null) ? 0 : mdpiImage.hashCode());
		result = prime * result + operatorId;
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		result = prime * result + questionStatus;
		result = prime * result + ((questionText == null) ? 0 : questionText.hashCode());
		result = prime * result + questionTypeId;
		result = prime * result + ((questionTypeName == null) ? 0 : questionTypeName.hashCode());
		result = prime * result + ((solution == null) ? 0 : solution.hashCode());
		result = prime * result + ((solutionHint == null) ? 0 : solutionHint.hashCode());
		result = prime * result + ((solutionText == null) ? 0 : solutionText.hashCode());
		result = prime * result + subjectId;
		result = prime * result + ((subjectIds == null) ? 0 : subjectIds.hashCode());
		result = prime * result + ((subjectName == null) ? 0 : subjectName.hashCode());
		result = prime * result + ((summary == null) ? 0 : summary.hashCode());
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		result = prime * result + ((tagTypeName == null) ? 0 : tagTypeName.hashCode());
		result = prime * result + (int) (timeLimit ^ (timeLimit >>> 32));
		result = prime * result + topicId;
		result = prime * result + ((topicIds == null) ? 0 : topicIds.hashCode());
		result = prime * result + ((topicName == null) ? 0 : topicName.hashCode());
		result = prime * result + type;
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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuestionVo other = (QuestionVo) obj;
		if (a4Image == null) {
			if (other.a4Image != null)
				return false;
		} else if (!a4Image.equals(other.a4Image))
			return false;
		if (approved != other.approved)
			return false;
		if (chapterId != other.chapterId)
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
		if (complexity != other.complexity)
			return false;
		if (copyright == null) {
			if (other.copyright != null)
				return false;
		} else if (!copyright.equals(other.copyright))
			return false;
		if (courseId != other.courseId)
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
		if (deleted != other.deleted)
			return false;
		if (duration != other.duration)
			return false;
		if (!Arrays.equals(facultySolution, other.facultySolution))
			return false;
		if (genericImage == null) {
			if (other.genericImage != null)
				return false;
		} else if (!genericImage.equals(other.genericImage))
			return false;
		if (groupId != other.groupId)
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
		if (id != other.id)
			return false;
		if (isMultipleAnswer != other.isMultipleAnswer)
			return false;
		if (isSolutionAvailable != other.isSolutionAvailable)
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
		if (operatorId != other.operatorId)
			return false;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (!options.equals(other.options))
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		if (questionStatus != other.questionStatus)
			return false;
		if (questionText == null) {
			if (other.questionText != null)
				return false;
		} else if (!questionText.equals(other.questionText))
			return false;
		if (questionTypeId != other.questionTypeId)
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
		if (subjectId != other.subjectId)
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
		if (timeLimit != other.timeLimit)
			return false;
		if (topicId != other.topicId)
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
		if (type != other.type)
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

	@Override
	public String toString() {
		return "QuestionVo [id=" + id + ", question=" + question + ", questionText=" + questionText + ", courseId="
				+ courseId + ", courseName=" + courseName + ", subjectId=" + subjectId + ", subjectName=" + subjectName
				+ ", chapterId=" + chapterId + ", chapterName=" + chapterName + ", topicId=" + topicId + ", topicName="
				+ topicName + ", complexity=" + complexity + ", timeLimit=" + timeLimit + ", summary=" + summary
				+ ", copyright=" + copyright + ", approved=" + approved + ", type=" + type + ", questionStatus="
				+ questionStatus + ", comment=" + comment + ", deleted=" + deleted + ", solution=" + solution
				+ ", solutionText=" + solutionText + ", solutionHint=" + solutionHint + ", tag=" + tag
				+ ", tagTypeName=" + tagTypeName + ", genericImage=" + genericImage + ", hdpiImage=" + hdpiImage
				+ ", mdpiImage=" + mdpiImage + ", ldpiImage=" + ldpiImage + ", a4Image=" + a4Image + ", options="
				+ options + ", courseIds=" + courseIds + ", subjectIds=" + subjectIds + ", chapterIds=" + chapterIds
				+ ", topicIds=" + topicIds + ", operatorId=" + operatorId + ", duration=" + duration
				+ ", isSolutionAvailable=" + isSolutionAvailable + ", facultySolution="
				+ Arrays.toString(facultySolution) + ", questionTypeId=" + questionTypeId + ", groupId=" + groupId
				+ ", questionTypeName=" + questionTypeName + ", groupName=" + groupName + ", groupImage=" + groupImage
				+ ", groupText=" + groupText + ", isMultipleAnswer=" + isMultipleAnswer + ", genericBlobImage="
				+ genericBlobImage + ", numericAnswer=" + numericAnswer + ", columnMatchAnswer="
				+ Arrays.toString(columnMatchAnswer) +", genericImageUrl="+genericImageUrl+ ", ldpiImageUrl="
				+ldpiImageUrl+ ", mdpiImageUrl="+mdpiImageUrl+ ", hdpiImageUrl="+hdpiImageUrl
				+ ", a4ImageUrl=" +a4ImageUrl+ ", questionUrl=" + questionUrl +", solutionUrl=" + solutionUrl 
				+",attemptedQuesCount="+ attemptedQuesCount +",unAttemptedQuesCount="+ unAttemptedQuesCount 
				+",correctAnsCount="+ correctAnsCount +",inCorrectAnsCount=" + inCorrectAnsCount 
				+",optionACount=" + optionACount + ",optionBCount=" + optionBCount +",optionCCount=" + optionCCount
				+",optionDCount=" + optionDCount +",averageTimeTaken=" + averageTimeTaken +"]";
	}

	
	

}