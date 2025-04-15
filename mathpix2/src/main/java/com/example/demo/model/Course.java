package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "course")
@Immutable
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer course_id;
	
	@Size(max = 45)
	private String course_name;
	
	private Date createdOn;
	
	@Column(columnDefinition = "TINYINT")
	private Integer course_status = 1;

//	@ManyToMany(mappedBy = "courses")
//	private List<FacultyInfo> facultyInfoList;
}
