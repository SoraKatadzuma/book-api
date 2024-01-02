package com.sora.books.transfer;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public final class PublicationDTO implements Serializable {
    private PublisherDTO publisher;
    private Long edition;
    private LocalDate copyright;
}
