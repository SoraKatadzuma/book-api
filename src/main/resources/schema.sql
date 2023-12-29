CREATE SCHEMA IF NOT EXISTS library;

CREATE TABLE IF NOT EXISTS library.author(
    author_id            BIGINT        NOT NULL AUTO_INCREMENT UNIQUE,
    author_full_name     VARCHAR(1024),  -- May be unknown
    author_date_of_birth DATE,           -- May be unknown
    author_date_of_death DATE,           -- May be unknown or still alive

    PRIMARY KEY(author_id)
);

CREATE TABLE IF NOT EXISTS library.book(
    book_id        BIGINT        NOT NULL AUTO_INCREMENT UNIQUE,
    book_name      VARCHAR(1024) NOT NULL,
    book_added     DATETIME      NOT NULL,
    book_deleted   DATETIME, -- May have not been deleted yet.
    book_copyright DATE,     -- May be unknown

    PRIMARY KEY(book_id)
);

CREATE TABLE IF NOT EXISTS library.publisher(
    publisher_id     BIGINT        NOT NULL AUTO_INCREMENT UNIQUE,
    publisher_name   VARCHAR(1024) NOT NULL                UNIQUE,
    publisher_locale VARCHAR(2)    NOT NULL,
    
    PRIMARY KEY(publisher_id)
);

CREATE TABLE IF NOT EXISTS library.author_book(
    abpk_mapping_id BIGINT NOT NULL AUTO_INCREMENT UNIQUE,
    abfk_author_id  BIGINT NOT NULL,
    abfk_book_id    BIGINT NOT NULL,

    PRIMARY KEY(abpk_mapping_id),
    FOREIGN KEY(abfk_author_id) REFERENCES library.author(author_id),
    FOREIGN KEY(abfk_book_id)   REFERENCES library.book(book_id)
);

CREATE TABLE IF NOT EXISTS library.author_pseudonym(
    appk_mapping_id BIGINT        NOT NULL AUTO_INCREMENT UNIQUE,
    apfk_author_id  BIGINT        NOT NULL,
    apvc_pseudonym  VARCHAR(1024) NOT NULL,

    PRIMARY KEY(appk_mapping_id),
    FOREIGN KEY(apfk_author_id) REFERENCES library.author(author_id)
);

CREATE TABLE IF NOT EXISTS library.book_publication(
    bpfk_book_id      BIGINT  NOT NULL,
    bpfk_publisher_id BIGINT  NOT NULL,
    bpti_edition      TINYINT NOT NULL,
    bpdt_copyright    DATE    NOT NULL,

    PRIMARY KEY(bpfk_book_id, bpfk_publisher_id),
    FOREIGN KEY(bpfk_book_id)      REFERENCES library.book(book_id),
    FOREIGN KEY(bpfk_publisher_id) REFERENCES library.publisher(publisher_id)
);