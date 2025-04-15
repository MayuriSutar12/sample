package com.example.demo.ExternalDto;

import java.util.List;

public class QuestionPaperVo {

	private int id;
	private String name;
	private String description;
	private int noOfQuestions;
	private int orgId;
	private int subjectId;
	private int testType;
	private int courseId;
	private boolean isNewQuestions = false;
	private List<QuestionDataVo> questionData;
	private boolean isAutoGenerate = false;
	private String createdBy;
	private String subjectName;
	private String createdOn;
	private String testNames;
	private Integer paperStatus;
	private Integer noOfTestAssociated;
	private int syncStatus;

	/**
	 * @return the paperStatus
	 */
	public Integer getPaperStatus() {
		return paperStatus;
	}

	/**
	 * @param paperStatus
	 *            the paperStatus to set
	 */
	public void setPaperStatus(Integer paperStatus) {
		this.paperStatus = paperStatus;
	}

	public Integer getNoOfTestAssociated() {
		return noOfTestAssociated;
	}

	public void setNoOfTestAssociated(Integer noOfTestAssociated) {
		this.noOfTestAssociated = noOfTestAssociated;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNoOfQuestions() {
		return noOfQuestions;
	}

	public void setNoOfQuestions(int noOfQuestions) {
		this.noOfQuestions = noOfQuestions;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public int getTestType() {
		return testType;
	}

	public void setTestType(int testType) {
		this.testType = testType;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public boolean isNewQuestions() {
		return isNewQuestions;
	}

	public void setIsNewQuestions(boolean isNewQuestions) {
		this.isNewQuestions = isNewQuestions;
	}

	public List<QuestionDataVo> getQuestionData() {
		return questionData;
	}

	public void setQuestionData(List<QuestionDataVo> questionData) {
		this.questionData = questionData;
	}

	public boolean isAutoGenerate() {
		return isAutoGenerate;
	}

	public void setIsAutoGenerate(boolean isAutoGenerate) {
		this.isAutoGenerate = isAutoGenerate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getTestNames() {
		return testNames;
	}

	public void setTestNames(String testNames) {
		this.testNames = testNames;
	}

	public int getSyncStatus() {
		return syncStatus;
	}

	public void setSyncStatus(int syncStatus) {
		this.syncStatus = syncStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + courseId;
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdOn == null) ? 0 : createdOn.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + (isAutoGenerate ? 1231 : 1237);
		result = prime * result + (isNewQuestions ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + noOfQuestions;
		result = prime * result + ((noOfTestAssociated == null) ? 0 : noOfTestAssociated.hashCode());
		result = prime * result + orgId;
		result = prime * result + ((paperStatus == null) ? 0 : paperStatus.hashCode());
		result = prime * result + ((questionData == null) ? 0 : questionData.hashCode());
		result = prime * result + subjectId;
		result = prime * result + ((subjectName == null) ? 0 : subjectName.hashCode());
		result = prime * result + syncStatus;
		result = prime * result + ((testNames == null) ? 0 : testNames.hashCode());
		result = prime * result + testType;
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
		QuestionPaperVo other = (QuestionPaperVo) obj;
		if (courseId != other.courseId)
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdOn == null) {
			if (other.createdOn != null)
				return false;
		} else if (!createdOn.equals(other.createdOn))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (isAutoGenerate != other.isAutoGenerate)
			return false;
		if (isNewQuestions != other.isNewQuestions)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (noOfQuestions != other.noOfQuestions)
			return false;
		if (noOfTestAssociated == null) {
			if (other.noOfTestAssociated != null)
				return false;
		} else if (!noOfTestAssociated.equals(other.noOfTestAssociated))
			return false;
		if (orgId != other.orgId)
			return false;
		if (paperStatus == null) {
			if (other.paperStatus != null)
				return false;
		} else if (!paperStatus.equals(other.paperStatus))
			return false;
		if (questionData == null) {
			if (other.questionData != null)
				return false;
		} else if (!questionData.equals(other.questionData))
			return false;
		if (subjectId != other.subjectId)
			return false;
		if (subjectName == null) {
			if (other.subjectName != null)
				return false;
		} else if (!subjectName.equals(other.subjectName))
			return false;
		if (syncStatus != other.syncStatus)
			return false;
		if (testNames == null) {
			if (other.testNames != null)
				return false;
		} else if (!testNames.equals(other.testNames))
			return false;
		if (testType != other.testType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QuestionPaperVo [id=" + id + ", name=" + name + ", description=" + description + ", noOfQuestions="
				+ noOfQuestions + ", orgId=" + orgId + ", subjectId=" + subjectId + ", testType=" + testType
				+ ", courseId=" + courseId + ", isNewQuestions=" + isNewQuestions + ", questionData=" + questionData
				+ ", isAutoGenerate=" + isAutoGenerate + ", createdBy=" + createdBy + ", subjectName=" + subjectName
				+ ", createdOn=" + createdOn + ", testNames=" + testNames + ", paperStatus=" + paperStatus
				+ ", noOfTestAssociated=" + noOfTestAssociated + ", syncStatus=" + syncStatus + "]";
	}

}
