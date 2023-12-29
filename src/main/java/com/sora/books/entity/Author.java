package com.sora.books.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Data
@Entity
@Builder
@ToString
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@Table(name   = "author",
       schema = "library",
       uniqueConstraints = {
    @UniqueConstraint(columnNames = "author_id")
})
public class Author implements Serializable {
    @Id
    @JsonSerialize
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name     = "author_id",
            unique   = true,
            nullable = false)
    private Long id;


    @JsonSerialize
    @Column(name     = "author_full_name",
            unique   = false,
            nullable = true,
            length   = 1024)
    private String fullName;


    @JsonSerialize
    @Column(name     = "author_date_of_birth",
            unique   = false,
            nullable = true)
    private LocalDate dateOfBirth;


    @JsonSerialize
    @Column(name     = "author_date_of_death",
            unique   = false,
            nullable = true)
    private LocalDate dateOfDeath;


    @JsonSerialize
    @ElementCollection
    @CollectionTable(name   = "author_pseudonym",
                     schema = "library",
                     joinColumns = {
        @JoinColumn(name = "apfk_author_id")
    })
    @Column(name = "apvc_pseudonym")
    private List<String> pseudonyms;
}
