package com.example.demo.ExternalDto;

import java.util.Set;

public class QuestionDataVo {

	private int chapterId;
	private int questionsCount;
	private Set<Integer> complexity;
	private Set<Integer> questionIds;
	private String chapterName;

	public int getChapterId() {
		return chapterId;
	}

	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}

	public int getQuestionsCount() {
		return questionsCount;
	}

	public void setQuestionsCount(int questionsCount) {
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
