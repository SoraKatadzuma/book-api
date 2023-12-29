package com.sora.books.transfer;

import java.io.Serializable;
import java.util.List;

import com.sora.books.entity.Author;
import com.sora.books.entity.Publication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;


@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public final class BookInformation implements Serializable {
    private String name;
    private List<Author> authors;
    private List<Publication> publications;
}
