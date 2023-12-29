package com.sora.books.service;

import com.sora.books.entity.Book;
import com.sora.books.repository.BookRepository;
import com.sora.books.transfer.BookInformation;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public final class BookService {
    @Autowired
    private BookRepository _bookRepository;


    public Book create(BookInformation input) {
        var book = new Book();
        return _bookRepository.save(book);
    }


    public Book read(Long bookId)
        throws NoSuchElementException
    {
        return _bookRepository
            .findById(bookId)
            .orElseThrow();
    }


    public Book update(Long bookId, BookInformation updated)
        throws NoSuchElementException
    {
        var toUpdate = _bookRepository
            .findById(bookId)
            .orElseThrow();
        
        toUpdate.id(bookId);
        if (updated.name()         != null) toUpdate.name(updated.name());
        if (updated.authors()      != null) toUpdate.authors(updated.authors());
        if (updated.publications() != null) toUpdate.publications(updated.publications());
        return _bookRepository.save(toUpdate);
    }


    public void delete(Long bookId) {
        _bookRepository.deleteById(bookId);
    }
}
