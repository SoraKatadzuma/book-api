package com.sora.books.entity;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "publisher",
       schema = "library",
       uniqueConstraints = {
    @UniqueConstraint(columnNames = "publisher_id"),
    @UniqueConstraint(columnNames = "publisher_name")
})
public final class Publisher implements Serializable {
    @Id
    @JsonSerialize
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name     = "publisher_id",
            unique   = true,
            nullable = false)
    private Long id;


    @JsonSerialize
    @Column(name     = "publisher_name",
            unique   = true,
            nullable = false,
            length   = 1024)
    private String name;


    @JsonSerialize
    @Column(name     = "publisher_locale",
            unique   = false,
            nullable = false,
            length   = 2)
    private String locale;
}
