package com.sora.books.service;

import com.sora.books.entity.Author;
import com.sora.books.entity.Book;
import com.sora.books.entity.Publication;
import com.sora.books.repository.BookRepository;
import com.sora.books.transfer.BookDTO;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class BookService {
    @Autowired
    private BookRepository _bookRepository;


    public BookDTO create(BookDTO input) {
        if (input.getName()         == null ||
            input.getAuthors()      == null ||
            input.getPublications() == null)
                return null;

        // Input should not set added or deleted.
        if (input.getAdded()   != null ||
            input.getDeleted() != null)
                return null;
        
        var book = Book.fromDTO(input);
        book.added(LocalDateTime.now());
        for (var publication : book.publications())
            publication.book(book);

        return Book.toDTO(_bookRepository.save(book));
    }


    public List<BookDTO> readAll() {
        var books = _bookRepository.findAll();
        return books
            .stream()
            .map(Book::toDTO)
            .collect(Collectors.toList());
    }


    public BookDTO read(Long bookId) throws NoSuchElementException {
        return Book.toDTO(_bookRepository
            .findById(bookId)
            .orElseThrow());
    }


    public BookDTO update(Long bookId, BookDTO updated)
        throws NoSuchElementException
    {
        // Input should not set added or deleted.
        if (updated.getAdded()   != null ||
            updated.getDeleted() != null)
                return null;

        var toUpdate = _bookRepository
            .findById(bookId)
            .orElseThrow();
        
        toUpdate.id(bookId);
        if (updated.getName() != null)
            toUpdate.name(updated.getName());

        if (updated.getAuthors() != null) {
            var remappedAuthors =
                updated.getAuthors()
                       .stream()
                       .map(Author::fromDTO)
                       .collect(Collectors.toSet());
            toUpdate.authors(remappedAuthors);
            for (var author : toUpdate.authors())
                author.books().add(toUpdate);
        }

        if (updated.getPublications() != null) {
            var remappedPublications =
                updated.getPublications()
                       .stream()
                       .map(Publication::fromDTO)
                       .collect(Collectors.toSet());
            toUpdate.publications(remappedPublications);
            for (var publication : toUpdate.publications()) {
                // Have to update key in order for this to work.
                publication.publicationKey().setBookId(toUpdate.id());
                publication.publicationKey().setPublisherId(publication.publisher().id());
                publication.book(toUpdate);
            }
        }

        return Book.toDTO(_bookRepository.save(toUpdate));
    }


    public void delete(Long bookId) {
        var optionalBook = _bookRepository
            .findById(bookId);

        if (!optionalBook.isPresent())
            return;

        var toUpdate = optionalBook.get();
        toUpdate.deleted(LocalDateTime.now());
        _bookRepository.save(toUpdate);
    }
}
