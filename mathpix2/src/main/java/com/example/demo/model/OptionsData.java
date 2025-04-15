package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.sql.Blob;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Immutable
@Table(name = "optionsdata")
public class OptionsData {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long options_id;

    private String text_data;

    private Blob blob_data;

    @NotNull
    private Date createdOn;

    private Boolean order_nature;

    @NotNull
    private Date updatedOn;

    private Boolean is_answer;

    private Integer sync_status;

    private String answer;

    @JoinColumn(name = "question_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private QuestionInfo question_id;


}
