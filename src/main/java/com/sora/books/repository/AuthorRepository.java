package com.sora.books.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sora.books.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
