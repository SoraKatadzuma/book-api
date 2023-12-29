package com.sora.books.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
@Table(name   = "book",
       schema = "library",
       uniqueConstraints = {
    @UniqueConstraint(columnNames = "book_id")
})
public class Book implements Serializable {
    @Id
    @JsonSerialize
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name     = "book_id",
            unique   = true, 
            nullable = false)
    private Long id;


    @JsonSerialize
    @Column(name     = "book_name",
            unique   = false,
            nullable = false,
            length   = 1024)
    private String name;


    @JsonSerialize
    @JsonFormat(shape   = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name     = "book_added",
            unique   = false,
            nullable = false)
    private LocalDateTime added;


    @JsonSerialize
    @JsonFormat(shape   = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name     = "book_deleted",
            unique   = false,
            nullable = true)
    private LocalDateTime deleted;


    @JsonSerialize
    @ManyToMany
    @JoinTable(name               = "author_book",
               schema             = "library",
               joinColumns        = @JoinColumn(name = "abfk_book_id"),
               inverseJoinColumns = @JoinColumn(name = "abfk_author_id"))
    private List<Author> authors;

    
    @JsonSerialize
    @OneToMany(mappedBy = "book")
//     @JoinTable(name               = "book_publication",
//                schema             = "library",
//                joinColumns        = { @JoinColumn(name = "bpfk_book_id"),
//                                       @JoinColumn(name = "bpfk_publisher_id") },
//                inverseJoinColumns = @JoinColumn(name = "bppk_mapping_id"))
    private List<Publication> publications;


    @JsonIgnore
    public boolean isDeleted() { return deleted != null; }
}
