package com.sora.books.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.sora.books.transfer.PublisherDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@Entity
@Builder
@ToString
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name     = "publisher_id",
            unique   = true,
            nullable = false)
    private Long id;


    @Column(name     = "publisher_name",
            unique   = true,
            nullable = false,
            length   = 1024)
    private String name;


    @Column(name     = "publisher_locale",
            unique   = false,
            nullable = false,
            length   = 2)
    private String locale;


    @Builder.Default
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    @OneToMany(mappedBy      = "publisher",
               orphanRemoval = true,
               cascade       = {
        CascadeType.PERSIST,
        CascadeType.MERGE
    })
    private Set<Publication> publications = new HashSet<>();


    public static Publisher fromDTO(PublisherDTO dto) {
        return Publisher.builder()
            .id(dto.getId())
            .name(dto.getName())
            .locale(dto.getLocale())
            .build();
    }

    
    public static PublisherDTO toDTO(Publisher publisher) {
        return PublisherDTO.builder()
            .id(publisher.id())
            .name(publisher.name())
            .locale(publisher.locale())
            .build();
    }
}
