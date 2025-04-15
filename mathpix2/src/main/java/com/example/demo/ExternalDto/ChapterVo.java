package com.example.demo.ExternalDto;

import java.util.Set;

public class ChapterVo {

	private int id;
	private String name;
	private Set<Integer> courseIds;
	private Integer subjectId;
	private Set<Integer> topicIds;

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

	public Set<Integer> getCourseIds() {
		return courseIds;
	}

	public void setCourseIds(Set<Integer> courseIds) {
		this.courseIds = courseIds;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Set<Integer> getTopicIds() {
		return topicIds;
	}

	public void setTopicIds(Set<Integer> topicIds) {
		this.topicIds = topicIds;
	}

	@Override
	public String toString() {
		return "ChapterVo [id=" + id + ", name=" + name + ", courseIds=" + courseIds + ", subjectId=" + subjectId
				+ ", topicIds=" + topicIds + "]";
	}

}