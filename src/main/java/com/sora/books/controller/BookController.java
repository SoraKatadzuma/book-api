package com.sora.books.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sora.books.service.BookService;
import com.sora.books.transfer.BookDTO;

import lombok.AllArgsConstructor;

import java.net.URISyntaxException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@AllArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping("api/v1")
public final class BookController {
    private BookService _bookService;

    @PostMapping("/book")
    public ResponseEntity<BookDTO> create(@RequestBody BookDTO input)
        throws URISyntaxException
    {
        var book = _bookService.create(input);
        if (book == null)
            return ResponseEntity.badRequest().build();

        var uri = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(book.getId())
            .toUri();

        return ResponseEntity
            .created(uri)
            .body(book);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookDTO> read(@PathVariable("id") Long bookId)
        throws JsonProcessingException
    {
        var book = _bookService.read(bookId);
        return book == null
            ? ResponseEntity.notFound().build()
            : book.isDeleted()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(book);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<BookDTO> update(@PathVariable("id") Long bookId,
                                          @RequestBody BookDTO updated) {
        var book = _bookService.update(bookId, updated);
        return book != null ? ResponseEntity.ok(book)
                            : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/book/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long bookId) {
        _bookService.delete(bookId);
        return ResponseEntity.noContent().build();
    }
}