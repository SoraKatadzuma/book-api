package com.sora.books.entity;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
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
@Table(name   = "book_publication",
       schema = "library",
       uniqueConstraints = {
    @UniqueConstraint(columnNames = "bppk_mapping_id"),
    @UniqueConstraint(columnNames = "bpfk_book_id"),
    @UniqueConstraint(columnNames = "bpfk_publisher_id")
})
public final class Publication implements Serializable {
//     @Id
//     @JsonSerialize
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     @Column(name     = "bppk_mapping_id",
//             unique   = true,
//             nullable = false)
//     private Long id;


    @EmbeddedId
    private PublicationKey publicationKey;


    @ManyToOne
    @JsonIgnore
    @MapsId("bookId")
    @JoinColumn(name = "bpfk_book_id")
    private Book book;


    @ManyToOne
    @JsonSerialize
    @MapsId("publisherId")
    @JoinColumn(name = "bpfk_publisher_id")
    private Publisher publisher;


    @JsonSerialize
    @Column(name     = "bpti_edition",
            unique   = false,
            nullable = false)
    private Long edition;


    @JsonSerialize
    @Column(name     = "bpdt_copyright",
            unique   = false,
            nullable = false)
    private LocalDate copyright;
}
