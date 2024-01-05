package com.sora.books.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PublicationKey implements Serializable {
    @Column(name     = "bpfk_book_id",
            unique   = false,
            nullable = false)
    private Long bookId;


    @Column(name     = "bpfk_publisher_id",
            unique   = false,
            nullable = false)
    private Long publisherId;
}
