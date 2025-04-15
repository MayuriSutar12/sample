package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.sql.Timestamp;

@Entity
@Table(name = "question_paper")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Immutable
public class QuestionPaper {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "paper_name")
	private String paperName;

	@Column(name = "subject_id")
	private Integer subjectId;

	@Column(name = "no_of_questions")
	private Integer noOfQuestions;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "description")
	private String description;

	@Column(name = "test_type")
	private Integer testType;

	@Column(name = "created_on", nullable = false, updatable = false, insertable = false)
	private Timestamp createdOn;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "auto_generate")
	private Boolean autoGenerate;

	@Column(name = "paper_status")
	private Boolean paperStatus;

	@Column(name = "sync_status")
	private Integer syncStatus;
}
