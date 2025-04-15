package com.example.demo.ExternalDto;

import java.util.Set;

public class SubjectVo {

	private int id;
	private String name;
	private Set<Integer> courseIds;
	private Set<Integer> chapterIds;

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

	public Set<Integer> getChapterIds() {
		return chapterIds;
	}

	public void setChapterIds(Set<Integer> chapterIds) {
		this.chapterIds = chapterIds;
	}

	@Override
	public String toString() {
		return "SubjectVo [id=" + id + ", name=" + name + ", courseIds="
		        + courseIds + ", chapterIds=" + chapterIds + "]";
	}

}
