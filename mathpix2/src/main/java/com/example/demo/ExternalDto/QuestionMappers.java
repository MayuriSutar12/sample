package com.example.demo.ExternalDto;

public class QuestionMappers {

	private int courseId;
	private int subjectId;
	
	
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + courseId;
		result = prime * result + subjectId;
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
		QuestionMappers other = (QuestionMappers) obj;
		if (courseId != other.courseId)
			return false;
		if (subjectId != other.subjectId)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "QuestionMappers [courseId=" + courseId + ", subjectId=" + subjectId + "]";
	}
	
}
