package com.example.demo.model;

import org.hibernate.annotations.Immutable;

import java.util.Set;

@Immutable
public class QuestionData {
	private Integer chapterId;
	private Integer questionsCount;
	private Set<Integer> complexity;
	private Set<Integer> questionIds;
	private String chapterName;

	public Integer getChapterId() {
		return chapterId;
	}

	public void setChapterId(Integer chapterId) {
		this.chapterId = chapterId;
	}

	public Integer getQuestionsCount() {
		return questionsCount;
	}

	public void setQuestionsCount(Integer questionsCount) {
		this.questionsCount = questionsCount;
	}

	public Set<Integer> getComplexity() {
		return complexity;
	}

	public void setComplexity(Set<Integer> complexity) {
		this.complexity = complexity;
	}

	public Set<Integer> getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(Set<Integer> questionIds) {
		this.questionIds = questionIds;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

}
