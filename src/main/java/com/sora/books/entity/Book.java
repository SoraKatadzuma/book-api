package com.sora.books.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sora.books.transfer.BookDTO;

import jakarta.persistence.CascadeType;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@Entity
@Builder
@ToString
@EqualsAndHashCode
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name     = "book_id",
            unique   = true, 
            nullable = false)
    private Long id;


    @Column(name     = "book_name",
            unique   = false,
            nullable = false,
            length   = 1024)
    private String name;


    @Column(name     = "book_added",
            unique   = false,
            nullable = false)
    private LocalDateTime added;


    @Column(name     = "book_deleted",
            unique   = false,
            nullable = true)
    private LocalDateTime deleted;


    @Builder.Default
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    @ManyToMany(cascade = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    @JoinTable(name               = "author_book",
               schema             = "library",
               joinColumns        = @JoinColumn(name = "abfk_book_id"),
               inverseJoinColumns = @JoinColumn(name = "abfk_author_id"))
    private Set<Author> authors = new HashSet<>();

    
//     @JsonSerialize
//     @Builder.Default
//     @Getter(value = AccessLevel.NONE)
//     @Setter(value = AccessLevel.NONE)
//     @OneToMany(mappedBy      = "book",
//                orphanRemoval = true,
//                cascade       = {
//         CascadeType.PERSIST,
//         CascadeType.MERGE
//     })
//     private Set<Publication> publications = new HashSet<>();


    @JsonIgnore
    public boolean isDeleted() { return deleted != null; }


    public static Book fromDTO(BookDTO dto) {
        var remappedAuthors =
            dto.getAuthors()
               .stream()
               .map(Author::fromDTO)
               .collect(Collectors.toSet());

        // var remappedPublications =
        //     dto.getPublications()
        //        .stream()
        //        .map(Publication::fromDTO)
        //        .collect(Collectors.toSet());

        return Book.builder()
            .id(dto.getId())
            .name(dto.getName())
            .added(dto.getAdded())
            .deleted(dto.getDeleted())
            .authors(remappedAuthors)
        //     .publications(remappedPublications)
            .build();
    }


    public static BookDTO toDTO(Book book) {
        var remappedAuthors =
            book.authors()
                .stream()
                .map(Author::toDTO)
                .collect(Collectors.toSet());

        // var remappedPublications =
        //     book.publications()
        //         .stream()
        //         .map(Publication::toDTO)
        //         .collect(Collectors.toSet());

        return BookDTO.builder()
            .id(book.id())
            .name(book.name())
            .added(book.added())
            .deleted(book.deleted())
            .authors(remappedAuthors)
        //     .publications(remappedPublications)
            .build();
    }


    public Set<Author> authors() {
        return authors;
    }

//     public Set<Publication> publications() {
//         return publications;
//     }

    public void authors(Set<Author> newAuthors) {
        authors.clear();
        if (newAuthors != null)
            authors.addAll(newAuthors);
    }

//     public void publications(Set<Publication> newPublications) {
//         publications.clear();
//         if (newPublications != null)
//             publications.addAll(newPublications);
//     }
}
