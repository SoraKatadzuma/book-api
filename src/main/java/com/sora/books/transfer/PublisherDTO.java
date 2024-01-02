package com.sora.books.transfer;

import java.io.Serializable;

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
public final class PublisherDTO implements Serializable {
    private Long id;
    private String name;
    private String locale;
}
