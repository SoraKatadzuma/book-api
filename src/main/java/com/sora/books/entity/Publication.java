package com.sora.books.entity;

import java.io.Serializable;
import java.time.LocalDate;

import com.sora.books.transfer.PublicationDTO;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@Table(name   = "book_publication",
       schema = "library",
       uniqueConstraints = {
    // @UniqueConstraint(columnNames = "bppk_mapping_id"),
    @UniqueConstraint(columnNames = "bpfk_book_id"),
    @UniqueConstraint(columnNames = "bpfk_publisher_id")
})
public final class Publication implements Serializable {
    // @Id
    // @JsonSerialize
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name     = "bppk_mapping_id",
    //         unique   = true,
    //         nullable = false)
    // private Long id;

    @EqualsAndHashCode.Exclude
    @EmbeddedId
    private PublicationKey publicationKey;


    // @EqualsAndHashCode.Exclude
    // @MapsId("bookId")
    // @ManyToOne(cascade = CascadeType.MERGE)
    // @JoinColumn(name = "bpfk_book_id")
    // private Book book;


    // @JsonSerialize
    // @EqualsAndHashCode.Exclude
    // @MapsId("publisherId")
    // @ManyToOne(cascade = CascadeType.MERGE)
    // @JoinColumn(name = "bpfk_publisher_id")
    // private Publisher publisher;


    @Column(name     = "bpti_edition",
            unique   = false,
            nullable = false)
    private Long edition;


    @Column(name     = "bpdt_copyright",
            unique   = false,
            nullable = false)
    private LocalDate copyright;


    public static Publication fromDTO(PublicationDTO dto) {
        var result = Publication.builder()
            .edition(dto.getEdition())
            .copyright(dto.getCopyright())
            .build();

        var remappedPublisher = Publisher.fromDTO(dto.getPublisher());
        result.publicationKey.setPublisher(remappedPublisher);
        return result;
    }

    
    public static PublicationDTO toDTO(Publication publication) {
        var publisher = publication.publicationKey.getPublisher();
        return PublicationDTO.builder()
            .publisher(Publisher.toDTO(publisher))
            .edition(publication.edition())
            .copyright(publication.copyright())
            .build();
    }
}
