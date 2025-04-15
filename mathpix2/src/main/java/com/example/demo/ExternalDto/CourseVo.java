package com.example.demo.ExternalDto;

import java.util.Set;

public class CourseVo {

	private int id;
	private String name;
	private Set<Integer> subjectIds;
	private Set<Integer> chapterIds;
	private Integer status;
	private Integer orgId;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chapterIds == null) ? 0 : chapterIds.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((orgId == null) ? 0 : orgId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((subjectIds == null) ? 0 : subjectIds.hashCode());
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
		CourseVo other = (CourseVo) obj;
		if (chapterIds == null) {
			if (other.chapterIds != null)
				return false;
		} else if (!chapterIds.equals(other.chapterIds))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (orgId == null) {
			if (other.orgId != null)
				return false;
		} else if (!orgId.equals(other.orgId))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (subjectIds == null) {
			if (other.subjectIds != null)
				return false;
		} else if (!subjectIds.equals(other.subjectIds))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CourseVo [id=" + id + ", name=" + name + ", subjectIds=" + subjectIds + ", chapterIds=" + chapterIds
				+ ", status=" + status + ", orgId=" + orgId + "]";
	}
}