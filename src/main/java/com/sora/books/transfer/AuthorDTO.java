package com.sora.books.transfer;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;


@Data
@Builder
@ToString
@Jacksonized
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public final class AuthorDTO implements Serializable {
    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
    private Set<String> pseudonyms;
}
