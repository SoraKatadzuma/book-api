package com.sora.books.transfer;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;


@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public final class BookDTO implements Serializable {
    private Long id;
    private String name;
    private Set<AuthorDTO> authors;
    private Set<PublicationDTO> publications;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime added;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deleted;

    @JsonIgnore
    public boolean isDeleted() { return deleted != null; }
}
