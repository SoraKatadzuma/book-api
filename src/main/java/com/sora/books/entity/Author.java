package com.sora.books.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.sora.books.transfer.AuthorDTO;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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
@Table(name   = "author",
       schema = "library",
       uniqueConstraints = {
    @UniqueConstraint(columnNames = "author_id")
})
public class Author implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name     = "author_id",
            unique   = true,
            nullable = false)
    private Long id;


    @Column(name     = "author_full_name",
            unique   = false,
            nullable = true,
            length   = 1024)
    private String fullName;


    @Column(name     = "author_date_of_birth",
            unique   = false,
            nullable = true)
    private LocalDate dateOfBirth;


    @Column(name     = "author_date_of_death",
            unique   = false,
            nullable = true)
    private LocalDate dateOfDeath;


    @Builder.Default
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    @ElementCollection
    @CollectionTable(name   = "author_pseudonym",
                     schema = "library",
                     joinColumns = {
        @JoinColumn(name = "apfk_author_id")
    })
    @Column(name = "apvc_pseudonym")
    private Set<String> pseudonyms = new HashSet<>();

    @Builder.Default
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();


    public static Author fromDTO(AuthorDTO dto) {
        return Author.builder()
            .id(dto.getId())
            .fullName(dto.getFullName())
            .dateOfBirth(dto.getDateOfBirth())
            .dateOfDeath(dto.getDateOfDeath())
            .pseudonyms(dto.getPseudonyms())
            .build();
    }


    public static AuthorDTO toDTO(Author author) {
        return AuthorDTO.builder()
            .id(author.id())
            .fullName(author.fullName())
            .dateOfBirth(author.dateOfBirth())
            .dateOfDeath(author.dateOfDeath())
            .pseudonyms(author.pseudonyms())
            .build();
    }


    public Set<String> pseudonyms() {
        return pseudonyms;
    }

    public Set<Book> books() {
        return books;
    }

    public void pseudonyms(Set<String> newPseudonyms) {
        pseudonyms.clear();
        if (newPseudonyms != null)
            pseudonyms.addAll(newPseudonyms);
    }

    public void books(Set<Book> newBooks) {
        books.clear();
        if (newBooks != null)
            books.addAll(newBooks);
    }
}
