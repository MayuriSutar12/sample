package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "subject")
@Immutable
public class Subject {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer subject_id;
	 
	 @Size(max = 45)
	 private String subject_name;
	 
	 @NotNull
	 private Date createdOn;

}
