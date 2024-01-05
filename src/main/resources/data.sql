-- For some reason, 
INSERT INTO library.author (
    author_id,
    author_full_name,
    author_date_of_birth,
    author_date_of_death
) VALUES (0, 'Herman Melville', '1819-08-01', '1891-09-28');

INSERT INTO library.author (
    author_id,
    author_full_name,
    author_date_of_birth,
    author_date_of_death
) VALUES (1, 'Jane Austen', '1775-12-16', '1817-07-18');

INSERT INTO library.book (
    book_id,
    book_name,
    book_added,
    book_deleted
) VALUES (0, 'Moby Dick', CURRENT_TIMESTAMP, NULL);

INSERT INTO library.book (
    book_id,
    book_name,
    book_added,
    book_deleted
) VALUES (1, 'Pride and Prejudice', CURRENT_TIMESTAMP, NULL);

INSERT INTO library.publisher (
    publisher_id,
    publisher_name,
    publisher_locale
) VALUES (0, 'Richard Bentley', 'UK');

INSERT INTO library.publisher (
    publisher_id,
    publisher_name,
    publisher_locale
) VALUES (1, 'Harper & Brothers', 'US');

INSERT INTO library.publisher (
    publisher_id,
    publisher_name,
    publisher_locale
) VALUES (2, 'Thomas Egerton', 'UK');

INSERT INTO library.author_book (
    abpk_mapping_id,
    abfk_author_id,
    abfk_book_id
) VALUES (0, 0, 0);

INSERT INTO library.author_book (
    abpk_mapping_id,
    abfk_author_id,
    abfk_book_id
) VALUES (1, 1, 1);

INSERT INTO library.author_pseudonym (
    apfk_author_id,
    apvc_pseudonym
) VALUES (0, 'L.A.V.');

INSERT INTO library.author_pseudonym (
    apfk_author_id,
    apvc_pseudonym
) VALUES (0, 'Salvator R. Tarnmoor');

INSERT INTO library.author_pseudonym (
    apfk_author_id,
    apvc_pseudonym
) VALUES (1, 'A. Lady');

INSERT INTO library.book_publication (
    bpfk_book_id,
    bpfk_publisher_id,
    bpti_edition,
    bpdt_copyright
) VALUES (0, 0, 1, '1851-10-18');

INSERT INTO library.book_publication (
    bpfk_book_id,
    bpfk_publisher_id,
    bpti_edition,
    bpdt_copyright
) VALUES (0, 1, 1, '1851-11-14');

INSERT INTO library.book_publication (
    bpfk_book_id,
    bpfk_publisher_id,
    bpti_edition,
    bpdt_copyright
) VALUES (1, 2, 1, '1813-01-28');


-- Need to do this to avoid primary key issues later.
ALTER TABLE library.author ALTER COLUMN author_id RESTART WITH 2;
ALTER TABLE library.book ALTER COLUMN book_id RESTART WITH 2;
ALTER TABLE library.publisher ALTER COLUMN publisher_id RESTART WITH 3;
ALTER TABLE library.author_book ALTER COLUMN abpk_mapping_id RESTART WITH 2;
